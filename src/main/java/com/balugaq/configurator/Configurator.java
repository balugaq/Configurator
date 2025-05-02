package com.balugaq.configurator;

import com.balugaq.configurator.command.ConfiguratorCommand;
import com.balugaq.configurator.visual.VisualCache;
import com.balugaq.configurator.visual.VisualLoader;
import com.balugaq.configurator.visual.VisualSaver;
import com.balugaq.configurator.visual.chat.ChatListener;
import com.balugaq.configurator.visual.interaction.InteractionListener;
import com.balugaq.configurator.visual.interaction.VisualNodeInteractListener;
import com.balugaq.configurator.visual.movement.ControlVisualListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Configurator extends JavaPlugin {
    @Getter
    private static Configurator instance;

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
        Bukkit.getPluginManager().registerEvents(new ControlVisualListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractionListener(), this);
        Bukkit.getPluginManager().registerEvents(new VisualNodeInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        getCommand("configurator").setExecutor(new ConfiguratorCommand());

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Configurator::saveVisual, 0L, 20L * 60L * 5L);

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