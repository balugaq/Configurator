package com.balugaq.configurator.visual;

import org.bukkit.entity.Entity;

public class VisualLoader {
    public static void loadVisual(Iterable<Entity> entities) {
        for (Entity entity : entities) {
            VisualNode.loadFromPDC(entity.getPersistentDataContainer());
            NodeLink.loadFromPDC(entity.getPersistentDataContainer());
        }
    }
}
