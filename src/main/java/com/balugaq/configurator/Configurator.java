package com.balugaq.configurator;

import com.balugaq.configurator.command.CreateCommand;
import com.balugaq.configurator.visual.VisualCache;
import com.balugaq.configurator.visual.VisualReloader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Configurator extends JavaPlugin {
    private static Configurator instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new VisualReloader(), this);
        getCommand("configurator").setExecutor(new CreateCommand());
    }

    @Override
    public void onDisable() {
        VisualCache.getAllVisualNodes().forEach((uuid, visualNode) -> {
            visualNode.saveToPDC(visualNode.getDisplay().getPersistentDataContainer());
        });
    }

    public static Configurator getInstance() {
        return instance;
    }

}