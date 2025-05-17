package com.balugaq.connector;

import com.balugaq.connector.command.ConfiguratorCommand;
import com.balugaq.connector.visual.VisualCache;
import com.balugaq.connector.visual.VisualLoader;
import com.balugaq.connector.visual.VisualSaver;
import com.balugaq.connector.visual.interaction.InteractionListener;
import com.balugaq.connector.visual.interaction.VisualNodeInteractListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Connector extends JavaPlugin {
    @Getter
    private static Connector instance;

    public static void saveVisual() {
        VisualCache.getAllVisualNodes().forEach((uuid, visualNode) -> {
            visualNode.saveToPDC(visualNode.getDisplay().getPersistentDataContainer());
        });
        VisualCache.getAllNodeLinks().forEach((uuid, nodeLink) -> {
            nodeLink.saveToPDC(nodeLink.getDisplay().getPersistentDataContainer());
        });
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new InteractionListener(), this);
        Bukkit.getPluginManager().registerEvents(new VisualNodeInteractListener(), this);
        getCommand("configurator").setExecutor(new ConfiguratorCommand());

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Connector::saveVisual, 0L, 20L * 60L * 5L);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (var player : Bukkit.getOnlinePlayers()) {
                var list = player.getNearbyEntities(5, 5, 5).stream().filter(entity -> entity instanceof ItemDisplay || entity instanceof BlockDisplay || entity instanceof Interaction).toList();
                VisualLoader.loadVisual(list);
                VisualSaver.save(list);
            }
        }, 0L, 20L);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, VisualCache::gc, 0L, 20L * 60L * 5L);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            var nodes = new ArrayList<>(VisualCache.getAllVisualNodes().values().stream().toList());
            for (var node : nodes) {
                node.updateDisplay();
            }
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        saveVisual();
    }

}