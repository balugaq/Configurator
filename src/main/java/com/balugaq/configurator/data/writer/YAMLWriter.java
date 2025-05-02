package com.balugaq.configurator.data.writer;

import com.balugaq.configurator.data.relation.Node;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class YAMLWriter implements IWriter {
    @Override
    public void write(FileConfiguration configuration, Node node, Consumer<FileConfiguration> out) {
        saveNodeRecursive(configuration, "", node, new HashSet<>());
        out.accept(configuration);
    }

    private void saveNodeRecursive(FileConfiguration config, String path, Node node, Set<Node> visited) {
        if (visited.contains(node)) {
            return;
        }

        visited.add(node);

        if (node.getChildren().isEmpty()) {
            config.set(path + node.getKey(), node.getValue());
        } else {
            for (Node child : node.getChildren()) {
                saveNodeRecursive(config, path + node.getKey() + ".", child, visited);
            }
        }
    }
}