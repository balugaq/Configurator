package com.balugaq.configurator;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Configurator extends JavaPlugin {
    private static Configurator instance;
    private Map<UUID, VisualNode> visualNodeCache = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        // 从PDC加载数据
        PersistentDataContainer pdc = this.getPersistentDataContainer();
        loadVisualNodesFromPDC(pdc);
    }

    @Override
    public void onDisable() {
        // 保存所有VisualNode数据到PDC
        PersistentDataContainer pdc = this.getPersistentDataContainer();
        saveVisualNodesToPDC(pdc);
    }

    public static Configurator getInstance() {
        return instance;
    }

    private void loadVisualNodesFromPDC(PersistentDataContainer pdc) {
        // 遍历PDC中的所有数据并加载VisualNode
        // 这里需要根据实际存储方式实现
    }

    private void saveVisualNodesToPDC(PersistentDataContainer pdc) {
        // 遍历visualNodeCache并保存每个VisualNode
        for (VisualNode visualNode : visualNodeCache.values()) {
            visualNode.saveToPDC(pdc);
        }
    }

    public void addVisualNode(VisualNode visualNode) {
        visualNodeCache.put(visualNode.getNode().getUuid(), visualNode);
    }

    public void removeVisualNode(UUID uuid) {
        visualNodeCache.remove(uuid);
    }

    public VisualNode getVisualNode(UUID uuid) {
        return visualNodeCache.get(uuid);
    }
}