package com.balugaq.configurator.data.relation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
public class Node {
    private String key = "Not set";
    private Object value = "Not set";
    private List<Node> children;
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