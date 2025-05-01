package com.balugaq.configurator.visual.interaction;

import com.balugaq.configurator.Configurator;
import com.balugaq.configurator.Converter;
import com.balugaq.configurator.events.PlayerInteractVisualNodeEvent;
import com.balugaq.configurator.Items;
import com.balugaq.configurator.visual.NodeLink;
import com.balugaq.configurator.visual.VisualCache;
import com.balugaq.configurator.visual.VisualNode;
import com.balugaq.configurator.visual.movement.ControlVisualListener;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class VisualNodeInteractListener implements Listener {
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
                meta.getPersistentDataContainer().set(new NamespacedKey(Configurator.getInstance(), "source"), DataType.UUID, event.getVisualNode().getUniqueId());
                mainHand.setItemMeta(meta);
            } else {
                UUID sourceUUID = meta.getPersistentDataContainer().get(new NamespacedKey(Configurator.getInstance(), "source"), DataType.UUID);
                var source = VisualCache.getVisualNode(sourceUUID);
                var destination = event.getVisualNode();
                new NodeLink(source, destination);
                player.getInventory().setItemInMainHand(Converter.getItem(Items.CONNECT_WAND));
            }
        } else {
            return;
        }
    }

    @EventHandler
    public void onStop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (hold(player, Items.CONTROL_WAND)) {
            if (player.isSneaking() && event.getAction().isLeftClick()) {
                for (var entity : ControlVisualListener.getListening().get(player.getUniqueId())) {
                    var node = VisualCache.getVisualNode(entity.getUniqueId());
                    if (node == null) {
                        continue;
                    }

                    List<NodeLink> nodeLinks = VisualCache.getConnectedNodeLinks(node);
                    nodeLinks.forEach(NodeLink::remove);
                    node.remove();
                    VisualCache.gc();
                }
            }

            ControlVisualListener.unlistenPlayer(player);
        }
    }

    public static boolean hold(Player player, ItemStack itemStack) {
        return itemStack.getItemMeta().getDisplayName().equals(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
    }
}
