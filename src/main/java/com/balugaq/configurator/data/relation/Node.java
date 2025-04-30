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
    private String key; // 配置项的键名
    private Object value; // 配置项的值
    private List<Node> children; // 子节点，支持嵌套结构
    private UUID uuid; // 实体的唯一标识符

    public Node(String key, Object value) {
        this.key = key;
        this.value = value;
        this.children = new ArrayList<>();
        this.uuid = UUID.randomUUID(); // 生成唯一的UUID
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    // 序列化方法，将Node转换为Map
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if (!children.isEmpty()) {
            map.put(key, children.stream().map(Node::serialize).toList());
        } else {
            map.put(key, value);
        }
        return map;
    }

    // 将Node数据保存到PDC
    public void saveToPDC(PersistentDataContainer pdc) {
        pdc.set(new NamespacedKey(Configurator.getInstance(), "key_" + uuid), PersistentDataType.STRING, key);
        pdc.set(new NamespacedKey(Configurator.getInstance(), "value_" + uuid), PersistentDataType.STRING, value.toString());
        pdc.set(new NamespacedKey(Configurator.getInstance(), "uuid_" + uuid), PersistentDataType.STRING, uuid.toString());
        // 保存子节点
        for (int i = 0; i < children.size(); i++) {
            children.get(i).saveToPDC(pdc);
        }
    }

    // 从PDC加载Node数据
    public static Node loadFromPDC(PersistentDataContainer pdc, UUID uuid) {
        Node node = new Node();
        node.key = pdc.get(new NamespacedKey(Configurator.getInstance(), "key_" + uuid), PersistentDataType.STRING);
        node.value = pdc.get(new NamespacedKey(Configurator.getInstance(), "value_" + uuid), PersistentDataType.STRING);
        node.uuid = uuid;
        // 加载子节点
        // 这里需要递归加载子节点，具体实现根据实际存储方式决定
        return node;
    }
}