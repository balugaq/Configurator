package com.balugaq.configurator.data.writer;

import com.balugaq.configurator.data.relation.Node;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

public class YAMLWriter implements IWriter {
    @Override
    public void write(File file, Node node) {
        var configuration = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> map = serialize(node);
        for (var entry : map.entrySet()) {
            configuration.set(entry.getKey(), entry.getValue());
        }
    }
}
