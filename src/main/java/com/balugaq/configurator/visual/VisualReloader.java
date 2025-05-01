package com.balugaq.configurator.visual;

import com.balugaq.configurator.Configurator;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;

import java.util.List;
import java.util.logging.Logger;

public class VisualReloader implements Listener {
    @EventHandler
    public void onLoad(EntitiesLoadEvent event) {
        loadVisual(event.getEntities());
    }

    @EventHandler
    public void onUnload(EntitiesUnloadEvent event) {
        VisualSaver.save(event.getEntities());
    }

    public static void loadVisual(List<Entity> entities) {
        for (Entity entity : entities) {
            VisualNode.loadFromPDC(entity.getPersistentDataContainer());
        }
    }
}
