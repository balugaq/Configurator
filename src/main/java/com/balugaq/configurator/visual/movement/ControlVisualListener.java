package com.balugaq.configurator.visual.movement;

import com.balugaq.configurator.Configurator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ControlVisualListener implements Listener {
    public static final double distance = 3;
    @Getter
    private static final Map<UUID, Entity> listening = new HashMap<>();

    @EventHandler
    public void onControl(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Entity entity = listening.get(player.getUniqueId());
        if (entity == null) {
            return;
        }

        var location = player.getLocation();
        var yaw = location.getYaw();
        var pitch = location.getPitch();
        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
        double dz = Math.cos(yawRad) * Math.cos(pitchRad);
        double dy = -Math.sin(pitchRad);

        double nx = location.getX() + distance * dx;
        double ny = location.getY() + distance * dy;
        double nz = location.getZ() + distance * dz;
        entity.teleport(new Location(location.getWorld(), nx, ny, nz, yaw, pitch));
    }

    public static void listenPlayer(Player player, Entity entity) {
        listening.put(player.getUniqueId(), entity);
    }

    public static void unlistenPlayer(Player player) {
        listening.remove(player.getUniqueId());
    }
}
