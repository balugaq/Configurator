package com.balugaq.configurator.visual;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class VisualCache {
    private static final Map<UUID, VisualNode> visualNodes = new HashMap<>();
    private static final Map<UUID, NodeLink> nodeLinks = new HashMap<>();

    public static void addVisualNode(VisualNode visualNode) {
        visualNodes.put(visualNode.getDisplay().getUniqueId(), visualNode);
    }

    public static void addNodeLink(NodeLink nodeLink) {
        nodeLinks.put(nodeLink.getUniqueId(), nodeLink);
    }

    public static VisualNode getVisualNode(UUID uuid) {
        return visualNodes.get(uuid);
    }

    public static NodeLink getNodeLink(UUID uuid) {
        return nodeLinks.get(uuid);
    }

    public static Map<UUID, VisualNode> getAllVisualNodes() {
        return visualNodes;
    }
}
