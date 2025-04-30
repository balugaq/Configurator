package com.balugaq.configurator.visual;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;

public class VisualReloader implements Listener {
    @EventHandler
    public void onLoad(EntitiesLoadEvent event) {
        // 检测是否是 Node 生成的实体，如果是，则实例化数据
    }

    @EventHandler
    public void onUnload(EntitiesUnloadEvent event) {
        VisualSaver.save(event.getEntities());
    }
}
