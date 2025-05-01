package com.balugaq.configurator.data.writer;

import com.balugaq.configurator.data.relation.Node;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class YAMLWriter implements IWriter {
    @Override
    public void write(File file, Node node) {
        var configuration = YamlConfiguration.loadConfiguration(file);
        saveNodeRecursive(configuration, "", node);
        try {
            configuration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 递归保存Node及其子节点
    private void saveNodeRecursive(YamlConfiguration config, String path, Node node) {
        if (node.getChildren().isEmpty()) {
            config.set(path + node.getKey(), node.getValue());
        } else {
            for (Node child : node.getChildren()) {
                saveNodeRecursive(config, path + node.getKey() + ".", child);
            }
        }
    }
}