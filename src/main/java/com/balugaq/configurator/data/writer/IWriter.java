package com.balugaq.configurator.data.writer;

import com.balugaq.configurator.data.relation.Node;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public interface IWriter {
    public void write(File file, Node node);
    default Map<String, Object> serialize(Node node) {
        var map = new HashMap<String, Object>();
        var v = node.getValue();
        if (v != null) map.put(node.getKey(), v);

        for (var c : node.getChildren()) {
            map.put(c.getKey(), serialize(c));
        }

        return map;
    }
}
