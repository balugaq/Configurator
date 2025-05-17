package com.balugaq.connector.visual;

import com.balugaq.connector.Keys;
import com.balugaq.connector.data.VisualNodeId;
import com.balugaq.connector.events.PlayerInteractVisualNodeEvent;
import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

@Data
public class VisualNode {
    public static final Integer interactHandlerID = 2 << 17;

    static {
        VisualCache.setInteractHandler(interactHandlerID, (event, belongsTo, interaction) -> {
            VisualNode visualNode = VisualCache.getVisualNode(new VisualNodeId(belongsTo.getUniqueId()));
            if (visualNode != null) {
                new PlayerInteractVisualNodeEvent(event.getPlayer(), visualNode).callEvent();
            }
        });
    }

    private ItemDisplay display;
    private Interaction interaction;
    private UUID child;

    public VisualNode(Location location) {
        this.display = createItemDisplay(location);
        this.interaction = createInteraction(location);
        this.child = null;
        VisualCache.addVisualNode(this);
        saveToPDC(getDisplay().getPersistentDataContainer());
    }

    public VisualNode(ItemDisplay display, Interaction interaction, UUID child) {
        this.display = display;
        this.interaction = interaction;
        this.child = child;
        VisualCache.addVisualNode(this);
        saveToPDC(getDisplay().getPersistentDataContainer());
    }

    public static VisualNode loadFromPDC(PersistentDataContainer pdc) {
        UUID uuid = pdc.get(Keys.c_node_uuid, DataType.UUID);
        if (uuid == null) {
            return null;
        }

        VisualNode loaded = VisualCache.getVisualNode(new VisualNodeId(uuid));
        if (loaded != null) {
            return loaded;
        }

        Entity entity = Bukkit.getEntity(uuid);
        if (!(entity instanceof ItemDisplay itemDisplay)) {
            return null;
        }

        UUID child = pdc.get(Keys.c_node_child, DataType.UUID);
        if (child == null) {
            return null;
        }

        UUID interactionUUID = pdc.get(Keys.c_node_interaction, DataType.UUID);
        if (interactionUUID == null) {
            return null;
        }

        Entity interactionEntity = Bukkit.getEntity(interactionUUID);
        if (!(interactionEntity instanceof Interaction interaction)) {
            return null;
        }

        Entity childEntity = Bukkit.getEntity(child);
        if (!(childEntity instanceof ItemDisplay childItemDisplay)) {
            return null;
        }

        VisualNode childVisualNode = loadFromPDC(childItemDisplay.getPersistentDataContainer());
        if (childVisualNode == null) {
            return null;
        }

        return new VisualNode(itemDisplay, interaction, child);
    }

    private ItemDisplay createItemDisplay(Location location) {
        ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(new ItemStack(Material.STONE));
        itemDisplay.setCustomNameVisible(true);
        return itemDisplay;
    }

    private Interaction createInteraction(Location location) {
        Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.getPersistentDataContainer().set(Keys.c_interaction_belongs_to, DataType.UUID, display.getUniqueId());
        interaction.setInteractionHeight(display.getDisplayHeight() + 1);
        interaction.setInteractionWidth(display.getDisplayWidth() + 1);
        return interaction;
    }

    public void saveToPDC(PersistentDataContainer pdc) {
        pdc.set(Keys.c_node_uuid, DataType.UUID, display.getUniqueId());
        pdc.set(Keys.c_node_child, DataType.UUID, child);
        pdc.set(Keys.c_node_interaction, DataType.UUID, interaction.getUniqueId());
        pdc.set(Keys.c_interact_handler, DataType.INTEGER, interactHandlerID);
    }

    public Location getLocation() {
        return display.getLocation();
    }


    public VisualNodeId getUniqueId() {
        return new VisualNodeId(display.getUniqueId());
    }

    public void remove() {
        display.remove();
        interaction.remove();
        VisualCache.removeVisualNode(this);
    }

    public void reconnect() {
        var link = VisualCache.getConnectedNodeLink(this);
        if (link != null) {
            link.remove();
            new NodeLink(this, link.getDestination());
        }
    }

    public void addChild(VisualNode child) {
        this.child = child.getUniqueId().getUuid();
        saveToPDC(display.getPersistentDataContainer());
    }

    public void removeChild(VisualNode child) {
        this.child = null;
        saveToPDC(display.getPersistentDataContainer());
    }
}