package com.balugaq.configurator.visual;

import com.balugaq.configurator.Configurator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
public class VisualCache {
    private static final Map<UUID/*VisualNode ID*/, VisualNode>               visualNodes    = new HashMap<>();
    private static final Map<UUID/*VisualNode ID*/, NodeLink>                 nodeLinks      = new HashMap<>();
    private static final Map<UUID/*VisualNode ID*/, Set<UUID/*NodeLink ID*/>> connectedNodes = new HashMap<>();
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

        connectedNodes.get(source.getUniqueId()).add(nodeLink.getUniqueId());

        if (!connectedNodes.containsKey(destination.getUniqueId())) {
            connectedNodes.put(destination.getUniqueId(), new HashSet<>());
        }

        connectedNodes.get(destination.getUniqueId()).add(nodeLink.getUniqueId());
    }

    public static void setInteractHandler(Integer id, InteractHandler interactHandler) {
        interactHandlers.put(id, interactHandler);
    }

    public static VisualNode getVisualNode(UUID uuid) {
        return visualNodes.get(uuid);
    }

    public static void removeNodeLink(NodeLink nodeLink) {
        nodeLinks.remove(nodeLink.getSource().getUniqueId());
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

    public static Map<UUID, Set<UUID>> getAllConnectedNodes() {
        return connectedNodes;
    }

    public static void removeVisualNode(VisualNode visualNode) {
        visualNodes.remove(visualNode.getUniqueId());
    }

    public static List<NodeLink> getConnectedNodeLinks(VisualNode node) {
        var connected = connectedNodes.get(node.getUniqueId());
        if (connected == null) {
            return new ArrayList<>();
        }

        return connected.stream().map(nodeLinks::get).filter(Objects::nonNull).toList();
    }

    public static void gc() {
        Bukkit.getScheduler().runTask(Configurator.getInstance(), () -> {
            var invalids1 = visualNodes.keySet().stream().filter(uuid -> Bukkit.getEntity(uuid) == null).toList();
            for (var invalid : invalids1) {
                visualNodes.remove(invalid);
                connectedNodes.remove(invalid);
            }

            var invalids2 = nodeLinks.keySet().stream().filter(uuid -> Bukkit.getEntity(uuid) == null).toList();
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
        void onInteract(PlayerInteractEntityEvent event, Entity belongsTo, Interaction interaction);
    }
}
