package com.balugaq.configurator.visual;

import com.balugaq.configurator.Configurator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
public class VisualCache {
    private static final Map<UUID, VisualNode> visualNodes = new HashMap<>();
    private static final Map<UUID, NodeLink> nodeLinks = new HashMap<>();
    private static final Map<UUID, Set<UUID>> connectedNodes = new HashMap<>();
    private static final Map<Integer, InteractHandler> interactHandlers = new HashMap<>();

    public static void addVisualNode(VisualNode visualNode) {
        visualNodes.put(visualNode.getDisplay().getUniqueId(), visualNode);
    }

    public static void addNodeLink(NodeLink nodeLink) {
        nodeLinks.put(nodeLink.getUniqueId(), nodeLink);
        VisualNode source = nodeLink.getSource();
        VisualNode destination = nodeLink.getDestination();
        if (!connectedNodes.containsKey(source.getUniqueId())) {
            connectedNodes.put(source.getUniqueId(), new HashSet<>());
        }

        connectedNodes.get(source.getUniqueId()).add(destination.getUniqueId());

        if (!connectedNodes.containsKey(destination.getUniqueId())) {
            connectedNodes.put(destination.getUniqueId(), new HashSet<>());
        }

        connectedNodes.get(destination.getUniqueId()).add(source.getUniqueId());
    }

    public static void setInteractHandler(Integer id, InteractHandler interactHandler) {
        interactHandlers.put(id, interactHandler);
    }

    public static VisualNode getVisualNode(UUID uuid) {
        return visualNodes.get(uuid);
    }

    public static NodeLink getNodeLink(UUID uuid) {
        return nodeLinks.get(uuid);
    }

    public static InteractHandler getInteractHandler(Integer id) {
        return interactHandlers.get(id);
    }

    public static Map<UUID, VisualNode> getAllVisualNodes() {
        return visualNodes;
    }
    public static Map<UUID, NodeLink> getAllNodeLinks() {
        return nodeLinks;
    }

    public static void gc() {
        Bukkit.getScheduler().runTask(Configurator.getInstance(), () -> {
            var invalids1 = visualNodes.keySet().parallelStream().filter(uuid -> Bukkit.getEntity(uuid) == null).toList();
            for (var invalid : invalids1) {
                visualNodes.remove(invalid);
                connectedNodes.remove(invalid);
            }

            var invalids2 = nodeLinks.keySet().parallelStream().filter(uuid -> Bukkit.getEntity(uuid) == null).toList();
            for (var invalid : invalids2) {
                nodeLinks.remove(invalid);
            }

            Set<UUID> empties = new HashSet<>();
            for (var entry : connectedNodes.entrySet()) {
                Set<UUID> connected = entry.getValue();
                Set<UUID> toRemove = new HashSet<>();
                for (var uuid : connected) {
                    if (Bukkit.getEntity(uuid) == null) {
                        toRemove.add(uuid);
                    }
                }
                connected.removeAll(toRemove);
                if (connected.isEmpty()) {
                    empties.add(entry.getKey());
                }
            }

            for (var empty : empties) {
                connectedNodes.remove(empty);
            }
        });
    }

    @FunctionalInterface
    public interface InteractHandler {
        void onInteract(PlayerInteractEntityEvent event, Entity clicked, Interaction interaction);
    }
}
