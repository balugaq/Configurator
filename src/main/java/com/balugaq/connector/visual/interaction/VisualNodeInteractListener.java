package com.balugaq.connector.visual.interaction;

import com.balugaq.connector.Converter;
import com.balugaq.connector.Items;
import com.balugaq.connector.Keys;
import com.balugaq.connector.data.VisualNodeId;
import com.balugaq.connector.events.PlayerInteractVisualNodeEvent;
import com.balugaq.connector.visual.NodeLink;
import com.balugaq.connector.visual.VisualCache;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class VisualNodeInteractListener implements Listener {
    public static boolean hold(Player player, ItemStack itemStack) {
        return Items.getDisplayName(itemStack).equals(Items.getDisplayName(player.getInventory().getItemInMainHand()));
    }

    @EventHandler
    public void onInteractVisualNode(PlayerInteractVisualNodeEvent event) {
        Player player = event.getPlayer();
        if (hold(player, Items.CONNECT_WAND)) {
            var mainHand = player.getInventory().getItemInMainHand();
            var meta = mainHand.getItemMeta();
            if (!meta.getPersistentDataContainer().has(Keys.source)) {
                meta.getPersistentDataContainer().set(Keys.source, DataType.UUID, event.getVisualNode().getUniqueId().getUuid());
                mainHand.setItemMeta(meta);
            } else {
                UUID sourceUUID = meta.getPersistentDataContainer().get(Keys.source, DataType.UUID);
                var source = VisualCache.getVisualNode(new VisualNodeId(sourceUUID));
                var destination = event.getVisualNode();
                if (player.isSneaking()) {
                    // remove
                    var nodeLink = VisualCache.getConnectedNodeLink(source);
                    if (nodeLink != null && nodeLink.getDestination() == destination) {
                        nodeLink.remove();
                    }
                } else {
                    if (!VisualCache.existsNodeLink(source, destination)) {
                        new NodeLink(source, destination);
                    }
                }

                player.getInventory().setItemInMainHand(Converter.getItem(Items.CONNECT_WAND));
            }
        }
    }
}
