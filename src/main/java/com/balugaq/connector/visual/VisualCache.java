package com.balugaq.connector.visual;

import com.balugaq.connector.Connector;
import com.balugaq.connector.data.NodeLinkId;
import com.balugaq.connector.data.VisualNodeId;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class VisualCache {
    private static final Map<VisualNodeId, VisualNode> visualNodes = new HashMap<>();
    private static final Map<NodeLinkId, NodeLink> nodeLinks = new HashMap<>();
    private static final Map<VisualNodeId, NodeLinkId> connectedNodes = new HashMap<>();
    private static final Map<Integer, InteractHandler> interactHandlers = new HashMap<>();

    public static void addVisualNode(VisualNode visualNode) {
        visualNodes.put(visualNode.getUniqueId(), visualNode);
    }

    public static void setNodeLink(NodeLink nodeLink) {
        nodeLinks.put(nodeLink.getUniqueId(), nodeLink);

        VisualNode source = nodeLink.getSource();
        connectedNodes.put(source.getUniqueId(), nodeLink.getUniqueId());
    }

    public static void setInteractHandler(Integer id, InteractHandler interactHandler) {
        interactHandlers.put(id, interactHandler);
    }

    public static VisualNode getVisualNode(VisualNodeId uuid) {
        return visualNodes.get(uuid);
    }

    public static void removeNodeLink(NodeLink nodeLink) {
        nodeLinks.remove(nodeLink.getUniqueId());
        connectedNodes.remove(nodeLink.getSource().getUniqueId());
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

    public static Map<VisualNodeId, NodeLinkId> getAllConnectedNodes() {
        return connectedNodes;
    }

    public static void removeVisualNode(VisualNode visualNode) {
        visualNodes.remove(visualNode.getUniqueId());
    }

    @Nullable
    public static NodeLink getConnectedNodeLink(VisualNode node) {
        var connected = connectedNodes.get(node.getUniqueId());
        if (connected == null) {
            return null;
        }

        return getNodeLink(connected);
    }

    public static boolean existsNodeLink(VisualNode source, VisualNode destination) {
        var linkId = connectedNodes.get(source.getUniqueId());
        if (linkId == null) {
            return false;
        }

        var link = getNodeLink(linkId);
        return link.getDestination().getLocation().equals(destination.getLocation());
    }

    public static void gc() {
        Bukkit.getScheduler().runTask(Connector.getInstance(), () -> {
            var invalids1 = visualNodes.keySet().stream().filter(uuid -> Bukkit.getEntity(uuid.getUuid()) == null).toList();
            for (var invalid : invalids1) {
                visualNodes.remove(invalid);
                connectedNodes.remove(invalid);
            }

            var invalids2 = nodeLinks.keySet().stream().filter(uuid -> Bukkit.getEntity(uuid.getUuid()) == null).toList();
            for (var invalid : invalids2) {
                nodeLinks.remove(invalid);
            }

            for (var entry : new HashMap<>(connectedNodes).entrySet()) {
                if (Bukkit.getEntity(entry.getValue().getUuid()) == null || Bukkit.getEntity(entry.getKey().getUuid()) == null) {
                    // remove
                    connectedNodes.remove(entry.getKey());
                }
            }
        });
    }

    @FunctionalInterface
    public interface InteractHandler {
        void onInteract(PlayerInteractEntityEvent event, Entity belongsTo, Interaction interaction);
    }
}
