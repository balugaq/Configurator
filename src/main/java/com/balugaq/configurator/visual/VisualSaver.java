package com.balugaq.configurator.visual;

import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;

import java.util.List;

public class VisualSaver {
    public static void save(List<Entity> event) {
        for (Entity entity : event) {
            if (!(entity instanceof ItemDisplay itemDisplay)) {
                continue;
            }

            VisualNode visualNode = VisualNode.loadFromPDC(itemDisplay.getPersistentDataContainer());
            if (visualNode == null) {
                continue;
            }

            visualNode.saveToPDC(itemDisplay.getPersistentDataContainer());
            visualNode.getNode().update();
        }
    }
}
