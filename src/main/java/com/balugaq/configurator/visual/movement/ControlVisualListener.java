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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ControlVisualListener implements Listener {
    public static final Map<UUID, Long> switchControlSince = new HashMap<>();

    public static final double distance = 3;
    @Getter
    private static final Map<UUID, List<Entity>> listening = new HashMap<>();

    @Getter
    private static final Map<UUID, BiConsumer<Player, List<Entity>>> handler = new HashMap<>();

    @Getter
    private static final Queue<Player> scheduled = new LinkedList<>();

    static {
        Bukkit.getScheduler().runTaskTimer(Configurator.getInstance(), () -> {
            Player player = scheduled.poll();
            if (player == null) {
                return;
            }

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
            Optional.ofNullable(handler.get(player.getUniqueId())).ifPresent(h -> h.accept(player, entities));
        }, 0L, 2L);
    }

    public static void listenPlayer(Player player, Entity entity) {
        listening.put(player.getUniqueId(), List.of(entity));
    }

    public static void listenPlayer(Player player, List<Entity> entities) {
        if (System.currentTimeMillis() - switchControlSince.getOrDefault(player.getUniqueId(), System.currentTimeMillis() - 501L) < 500L) {
            player.sendMessage("请稍后再拿起实体");
            return;
        }

        listening.put(player.getUniqueId(), entities);

        switchControlSince.put(player.getUniqueId(), System.currentTimeMillis());
        player.sendMessage("已拿起实体");
    }

    public static void addHandler(Player player, BiConsumer<Player, List<Entity>> handler) {
        ControlVisualListener.handler.put(player.getUniqueId(), handler);
    }

    public static void unlistenPlayer(Player player) {
        if (System.currentTimeMillis() - switchControlSince.getOrDefault(player.getUniqueId(), System.currentTimeMillis() - 501L) < 500L) {
            player.sendMessage("请稍后再放下实体");
            return;
        }

        if (listening.containsKey(player.getUniqueId())) {
            player.sendMessage("已放下实体");
        }

        listening.remove(player.getUniqueId());
        handler.remove(player.getUniqueId());

        switchControlSince.remove(player.getUniqueId());
    }

    @EventHandler
    public void onControl(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        List<Entity> entities = listening.get(player.getUniqueId());
        if (entities == null) {
            return;
        }

        scheduled.add(player);
    }
}
