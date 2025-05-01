package com.balugaq.configurator.visual.interaction;

import com.balugaq.configurator.Configurator;
import com.balugaq.configurator.Converter;
import com.balugaq.configurator.Items;
import com.balugaq.configurator.data.VisualNodeId;
import com.balugaq.configurator.events.PlayerInteractVisualNodeEvent;
import com.balugaq.configurator.visual.NodeLink;
import com.balugaq.configurator.visual.VisualCache;
import com.balugaq.configurator.visual.movement.ControlVisualListener;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VisualNodeInteractListener implements Listener {
    public static boolean hold(Player player, ItemStack itemStack) {
        return Items.getDisplayName(itemStack).equals(Items.getDisplayName(player.getInventory().getItemInMainHand()));
    }

    @EventHandler
    public void onInteractVisualNode(PlayerInteractVisualNodeEvent event) {
        Configurator.getInstance().getLogger().info("Called onInteractVisualNode");
        Player player = event.getPlayer();
        if (hold(player, Items.CONTROL_WAND)) {
            Configurator.getInstance().getLogger().info("Called ControlWand");
            var visualNode = event.getVisualNode();
            ControlVisualListener.listenPlayer(player, List.of(visualNode.getDisplay(), visualNode.getInteraction()));
            ControlVisualListener.addHandler(player, (e, entities) -> {
                visualNode.reconnect();
            });
        } else if (hold(player, Items.CONNECT_WAND)) {
            Configurator.getInstance().getLogger().info("Called ConnectWand");
            var mainHand = player.getInventory().getItemInMainHand();
            var meta = mainHand.getItemMeta();
            if (!meta.getPersistentDataContainer().has(new NamespacedKey(Configurator.getInstance(), "source"))) {
                meta.getPersistentDataContainer().set(new NamespacedKey(Configurator.getInstance(), "source"), DataType.UUID, event.getVisualNode().getUniqueId().getUuid());
                mainHand.setItemMeta(meta);
            } else {
                UUID sourceUUID = meta.getPersistentDataContainer().get(new NamespacedKey(Configurator.getInstance(), "source"), DataType.UUID);
                var source = VisualCache.getVisualNode(new VisualNodeId(sourceUUID));
                var destination = event.getVisualNode();
                if (player.isSneaking()) {
                    // remove
                    for (var nodeLink : VisualCache.getConnectedNodeLinks(source)) {
                        if (nodeLink.getDestination() == destination) {
                            nodeLink.remove();
                            break;
                        }
                    }

                    for (var nodeLink : VisualCache.getConnectedNodeLinks(destination)) {
                        if (nodeLink.getSource() == source) {
                            nodeLink.remove();
                            break;
                        }
                    }
                } else {
                    new NodeLink(source, destination);
                }
                player.getInventory().setItemInMainHand(Converter.getItem(Items.CONNECT_WAND));
                player.sendMessage("已连接实体");
            }
        } else {
            event.getVisualNode().getDisplay().setItemStack(event.getPlayer().getInventory().getItemInMainHand());
            event.getVisualNode().getNode().setDirty(true);
        }
    }

    @EventHandler
    public void onStop(PlayerInteractEvent event) {
        onStop(event.getPlayer(), event.getAction());
    }

    @EventHandler
    public void onStop(PlayerItemHeldEvent event) {
        onStop(event.getPlayer(), Action.RIGHT_CLICK_AIR);
    }

    public void onStop(Player player, Action action) {
        if (hold(player, Items.CONTROL_WAND)) {
            if (player.isSneaking() && action.isRightClick()) {
                var list = ControlVisualListener.getListening().get(player.getUniqueId());
                if (list != null) {
                    for (var entity : list) {
                        var node = VisualCache.getVisualNode(new VisualNodeId(entity.getUniqueId()));
                        if (node == null) {
                            continue;
                        }

                        List<NodeLink> nodeLinks = VisualCache.getConnectedNodeLinks(node);
                        nodeLinks.forEach(NodeLink::remove);
                        node.remove();
                        VisualCache.gc();
                    }
                }
            }

            ControlVisualListener.unlistenPlayer(player);
        }
    }
}
