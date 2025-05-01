package com.balugaq.configurator.data.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.balugaq.configurator.Configurator;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Node {
    private String key = "Not set"; // 配置项的键名
    private Object value = "Not set"; // 配置项的值
    private List<Node> children; // 子节点，支持嵌套结构
    private transient boolean dirty = true;

    public Node(String key, Object value) {
        this.key = key;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if (!children.isEmpty()) {
            map.put(key, children.stream().map(Node::serialize).toList());
        } else {
            map.put(key, value);
        }
        return map;
    }

    public void dirty() {
        this.dirty = true;
    }

    public void update() {
        this.dirty = false;
    }
}