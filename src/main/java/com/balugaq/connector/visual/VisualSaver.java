package com.balugaq.connector.visual;

import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;

public class VisualSaver {
    public static void save(Iterable<Entity> event) {
        for (Entity entity : event) {
            if (entity instanceof ItemDisplay) {
                VisualNode visualNode = VisualNode.loadFromPDC(entity.getPersistentDataContainer());
                if (visualNode == null) {
                    continue;
                }

                visualNode.saveToPDC(entity.getPersistentDataContainer());
                visualNode.getNode().update();
            } else if (entity instanceof BlockDisplay) {
                NodeLink nodeLink = NodeLink.loadFromPDC(entity.getPersistentDataContainer());
                if (nodeLink == null) {
                    continue;
                }

                nodeLink.saveToPDC(entity.getPersistentDataContainer());
            }
        }
    }
}
