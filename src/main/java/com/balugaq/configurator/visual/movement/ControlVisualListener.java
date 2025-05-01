package com.balugaq.configurator.visual.movement;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ControlVisualListener implements Listener {
    public static final double distance = 3;
    @Getter
    private static final Map<UUID, List<Entity>> listening = new HashMap<>();

    @Getter
    private static final Map<UUID, BiConsumer<PlayerMoveEvent, List<Entity>>> handler = new HashMap<>();


    @EventHandler
    public void onControl(PlayerMoveEvent event) { // todo: update interval = 20 game ticks
        Player player = event.getPlayer();
        List<Entity> entities = listening.get(player.getUniqueId());
        if (entities == null) {
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
        entities.forEach(entity -> entity.teleport(new Location(location.getWorld(), nx, ny, nz, yaw, pitch)));
        Optional.ofNullable(handler.get(player.getUniqueId())).ifPresent(h -> h.accept(event, entities));
    }

    public static void listenPlayer(Player player, Entity entity) {
        listening.put(player.getUniqueId(), List.of(entity));
    }

    public static void listenPlayer(Player player, List<Entity> entities) {
        listening.put(player.getUniqueId(), entities);
    }

    public static void addHandler(Player player, BiConsumer<PlayerMoveEvent, List<Entity>> handler) {
        ControlVisualListener.handler.put(player.getUniqueId(), handler);
    }

    public static void unlistenPlayer(Player player) {
        listening.remove(player.getUniqueId());
        handler.remove(player.getUniqueId());
    }
}
