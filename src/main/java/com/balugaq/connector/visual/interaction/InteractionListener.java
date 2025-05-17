package com.balugaq.connector.visual.interaction;

import com.balugaq.connector.Connector;
import com.balugaq.connector.visual.VisualCache;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class InteractionListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Interaction interaction)) {
            return;
        }

        UUID belongsTo = interaction.getPersistentDataContainer().get(new NamespacedKey(Connector.getInstance(), "c_interaction_belongs_to"), DataType.UUID);
        if (belongsTo == null) {
            return;
        }

        Entity entity = Bukkit.getEntity(belongsTo);
        if (entity == null) {
            return;
        }

        Integer handlerID = entity.getPersistentDataContainer().get(new NamespacedKey(Connector.getInstance(), "c_interact_handler"), PersistentDataType.INTEGER);
        if (handlerID == null) {
            return;
        }

        VisualCache.InteractHandler handler = VisualCache.getInteractHandler(handlerID);
        if (handler != null) {
            handler.onInteract(event, entity, interaction);
        }
    }
}
