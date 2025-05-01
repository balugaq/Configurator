package com.balugaq.configurator.visual;

import com.balugaq.configurator.Configurator;
import com.balugaq.configurator.data.relation.Node;
import com.balugaq.configurator.events.PlayerInteractVisualNodeEvent;
import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
public class VisualNode {
    public static final Integer interactHandlerID = 2 << 17;
    private Node node;
    private ItemDisplay display;
    private Interaction interaction;
    private ArrayList<UUID> children;

    static {
        VisualCache.setInteractHandler(interactHandlerID, ( event, belongsTo, interaction) -> {
            Configurator.getInstance().getLogger().info("Called VisualNode InteractHandler");
            VisualNode visualNode = VisualCache.getVisualNode(belongsTo.getUniqueId());
            if (visualNode != null) {
                Configurator.getInstance().getLogger().info("New PlayerInteractVisualNodeEvent");
                new PlayerInteractVisualNodeEvent(event.getPlayer(), visualNode).callEvent();
            }
        });
    }

    public VisualNode(Node node, Location location) {
        this.node = node;
        this.display = createItemDisplay(location);
        this.interaction = createInteraction(location);
        this.children = new ArrayList<>();
        VisualCache.addVisualNode(this);
        saveToPDC(getDisplay().getPersistentDataContainer());
    }

    public VisualNode(Node node, ItemDisplay display, Interaction interaction, ArrayList<UUID> children) {
        this.node = node;
        this.display = display;
        this.interaction = interaction;
        this.children = children;
        VisualCache.addVisualNode(this);
        saveToPDC(getDisplay().getPersistentDataContainer());
    }

    private ItemDisplay createItemDisplay(Location location) {
        ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(new ItemStack(Material.STONE));
        itemDisplay.setCustomName(node.getKey());
        itemDisplay.setCustomNameVisible(true);
        return itemDisplay;
    }

    private Interaction createInteraction(Location location) {
        Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.getPersistentDataContainer().set(new NamespacedKey(Configurator.getInstance(), "c_interaction_belongs_to"), DataType.UUID, display.getUniqueId());
        interaction.setInteractionHeight(display.getDisplayHeight() + 1);
        interaction.setInteractionWidth(display.getDisplayWidth() + 1);
        return interaction;
    }

    public void saveToPDC(PersistentDataContainer pdc) {
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_node_uuid"), DataType.UUID, display.getUniqueId());
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_node_children"), DataType.asArrayList(DataType.UUID), children);
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_node_key"), DataType.STRING, node.getKey());
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_node_value"), DataType.STRING, serializeValue(node.getValue()));
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_node_interaction"), DataType.UUID, interaction.getUniqueId());
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_interact_handler"), DataType.INTEGER, interactHandlerID);
    }

    public static VisualNode loadFromPDC(PersistentDataContainer pdc) {
        UUID uuid = pdc.get(new NamespacedKey(Configurator.getInstance(), "c_node_uuid"), DataType.UUID);
        if (uuid == null) {
            return null;
        }

        VisualNode loaded = VisualCache.getVisualNode(uuid);
        if (loaded != null) {
            return loaded;
        }

        ArrayList<UUID> children = pdc.get(new NamespacedKey(Configurator.getInstance(), "c_node_children"), DataType.asArrayList(DataType.UUID));
        if (children == null) {
            return null;
        }

        String key = pdc.get(new NamespacedKey(Configurator.getInstance(), "c_node_key"), DataType.STRING);
        if (key == null) {
            return null;
        }

        String valueString = pdc.get(new NamespacedKey(Configurator.getInstance(), "c_node_value"), DataType.STRING);
        if (valueString == null) {
            return null;
        }

        UUID interactionUUID = pdc.get(new NamespacedKey(Configurator.getInstance(), "c_node_interaction"), DataType.UUID);
        if (interactionUUID == null) {
            return null;
        }

        Entity interactionEntity = Bukkit.getEntity(interactionUUID);
        if (!(interactionEntity instanceof Interaction interaction)) {
            return null;
        }

        Object value = deserializeValue(valueString);
        Node node = new Node(key, value);
        Entity entity = Bukkit.getEntity(uuid);
        if (!(entity instanceof ItemDisplay itemDisplay)) {
            return null;
        }

        for (UUID child : children) {
            Entity childEntity = Bukkit.getEntity(child);
            if (!(childEntity instanceof ItemDisplay childItemDisplay)) {
                continue;
            }

            VisualNode childVisualNode = loadFromPDC(childItemDisplay.getPersistentDataContainer());
            if (childVisualNode == null){
                continue;
            }

            node.addChild(childVisualNode.getNode());
        }

        return new VisualNode(node, itemDisplay, interaction, children);
    }

    public Location getLocation() {
        return display.getLocation();
    }

    private static String serializeValue(Object value) {
        if (value == null) {
            return "!NULL";
        } else if (value instanceof Integer) {
            return value + "I";
        } else if (value instanceof Float) {
            return value + "F";
        } else if (value instanceof Double) {
            return value + "D";
        } else if (value instanceof Boolean) {
            return value + "b";
        } else if (value instanceof Byte) {
            return value + "B";
        } else if (value instanceof Short) {
            return value + "S";
        } else if (value instanceof Long) {
            return value + "L";
        } else if (value instanceof String) {
            return String.valueOf(value);
        } else {
            return String.valueOf(value);
        }
    }

    private static Object deserializeValue(String value) {
        try {
            if (Objects.equals(value, "!NULL")) {
                return null;
            } else if (value.endsWith("I")) {
                return Integer.parseInt(value.substring(0, value.length() - 1));
            } else if (value.endsWith("F")) {
                return Float.parseFloat(value.substring(0, value.length() - 1));
            } else if (value.endsWith("D")) {
                return Double.parseDouble(value.substring(0, value.length() - 1));
            } else if (value.endsWith("b")) {
                return Boolean.parseBoolean(value.substring(0, value.length() - 1));
            } else if (value.endsWith("B")) {
                return Byte.parseByte(value.substring(0, value.length() - 1));
            } else if (value.endsWith("S")) {
                return Short.parseShort(value.substring(0, value.length() - 1));
            } else if (value.endsWith("L")) {
                return Long.parseLong(value.substring(0, value.length() - 1));
            } else {
                return value;
            }
        } catch (NumberFormatException e) {
            // Quote string
            return value;
        }
    }

    public void updateDisplay() {
        if (node.isDirty()) {
            display.setCustomName(node.getKey());
            node.update();
        }
    }

    public void setKey(String key) {
        node.setKey(key);
    }

    public void setValue(Object value) {
        node.setValue(value);
    }

    public UUID getUniqueId() {
        return display.getUniqueId();
    }

    public void remove() {
        display.remove();
        interaction.remove();
        VisualCache.removeVisualNode(this);
    }

    public void reconnect() {
        var links = VisualCache.getConnectedNodeLinks(this);
        List<VisualNode> connects = links.parallelStream().map(link -> link.getSource().getUniqueId() == getUniqueId() ? link.getDestination() : link.getSource()).toList();
        for (VisualNode connect : connects) {
            new NodeLink(this, connect);
        }
        links.forEach(NodeLink::remove);
    }
}