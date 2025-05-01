package com.balugaq.configurator.visual.interaction;

import com.balugaq.configurator.Configurator;
import com.balugaq.configurator.visual.VisualCache;
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
            Configurator.getInstance().getLogger().info("Type: " + event.getRightClicked());
            return;
        }

        UUID belongsTo = interaction.getPersistentDataContainer().get(new NamespacedKey(Configurator.getInstance(), "c_interaction_belongs_to"), DataType.UUID);
        if (belongsTo == null) {
            Configurator.getInstance().getLogger().info("Interaction no c_interaction_belongs_to");
            return;
        }

        Entity entity = Bukkit.getEntity(belongsTo);
        if (entity == null) {
            Configurator.getInstance().getLogger().info("Entity not exist");
            return;
        }

        Integer handlerID = entity.getPersistentDataContainer().get(new NamespacedKey(Configurator.getInstance(), "c_interact_handler"), PersistentDataType.INTEGER);
        if (handlerID == null) {
            Configurator.getInstance().getLogger().info("Entity no c_interact_handler");
            return;
        }

        Configurator.getInstance().getLogger().info("Call interact handler");
        VisualCache.InteractHandler handler = VisualCache.getInteractHandler(handlerID);
        if (handler != null) {
            handler.onInteract(event, entity, interaction);
        }
    }
}
