package com.balugaq.configurator.data.writer;

import com.balugaq.configurator.data.relation.Node;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.function.Consumer;

public interface IWriter {
    default void write(Node node, Consumer<FileConfiguration> out) {
        write(new YamlConfiguration(), node, out);
    }
    void write(FileConfiguration configuration, Node node, Consumer<FileConfiguration> out);
}