package com.balugaq.configurator.visual;

import com.balugaq.configurator.Configurator;
import com.balugaq.configurator.data.NodeLinkId;
import com.balugaq.configurator.data.VisualNodeId;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
public class VisualCache {
    private static final Map<VisualNodeId, VisualNode> visualNodes = new HashMap<>();
    private static final Map<NodeLinkId, NodeLink> nodeLinks = new HashMap<>();
    /*                       Father      ->    Child      */
    private static final Map<VisualNodeId, Set<NodeLinkId>> connectedNodes = new HashMap<>();
    private static final Map<Integer, InteractHandler> interactHandlers = new HashMap<>();

    public static void addVisualNode(VisualNode visualNode) {
        visualNodes.put(visualNode.getUniqueId(), visualNode);
    }

    public static void addNodeLink(NodeLink nodeLink) {
        nodeLinks.put(nodeLink.getUniqueId(), nodeLink);

        VisualNode source = nodeLink.getSource();
        if (!connectedNodes.containsKey(source.getUniqueId())) {
            connectedNodes.put(source.getUniqueId(), new HashSet<>());
        }

        connectedNodes.get(source.getUniqueId()).add(nodeLink.getUniqueId());
    }

    public static void setInteractHandler(Integer id, InteractHandler interactHandler) {
        interactHandlers.put(id, interactHandler);
    }

    public static VisualNode getVisualNode(VisualNodeId uuid) {
        return visualNodes.get(uuid);
    }

    public static void removeNodeLink(NodeLink nodeLink) {
        nodeLinks.remove(nodeLink.getUniqueId());
        connectedNodes.get(nodeLink.getSource().getUniqueId()).remove(nodeLink.getUniqueId());
        nodeLink.getSource().removeChild(nodeLink.getDestination());
    }

    public static NodeLink getNodeLink(NodeLinkId uuid) {
        return nodeLinks.get(uuid);
    }

    public static InteractHandler getInteractHandler(Integer id) {
        return interactHandlers.get(id);
    }

    public static Map<VisualNodeId, VisualNode> getAllVisualNodes() {
        return visualNodes;
    }

    public static Map<NodeLinkId, NodeLink> getAllNodeLinks() {
        return nodeLinks;
    }

    public static Map<VisualNodeId, Set<NodeLinkId>> getAllConnectedNodes() {
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
            var invalids1 = visualNodes.keySet().stream().filter(uuid -> Bukkit.getEntity(uuid.getUuid()) == null).toList();
            for (var invalid : invalids1) {
                visualNodes.remove(invalid);
                connectedNodes.remove(invalid);
            }

            var invalids2 = nodeLinks.keySet().stream().filter(uuid -> Bukkit.getEntity(uuid.getUuid()) == null).toList();
            for (var invalid : invalids2) {
                nodeLinks.remove(invalid);
            }

            Set<VisualNodeId> empties = new HashSet<>();
            for (var entry : connectedNodes.entrySet()) {
                Set<NodeLinkId> connected = entry.getValue();
                Set<NodeLinkId> toRemove = new HashSet<>();
                for (var uuid : connected) {
                    if (Bukkit.getEntity(uuid.getUuid()) == null) {
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
