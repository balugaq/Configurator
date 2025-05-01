package com.balugaq.configurator.visual;

import com.balugaq.configurator.Configurator;
import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.persistence.PersistentDataContainer;
import org.metamechanists.displaymodellib.models.components.ModelLine;
import org.metamechanists.displaymodellib.transformations.TransformationUtils;

import java.util.UUID;

@Data
public class NodeLink {
    public static final Integer interactHandlerID = 2 << 25;
    private VisualNode source;
    private VisualNode destination;
    private BlockDisplay display;
    private Interaction interaction;

    static {
        VisualCache.setInteractHandler(interactHandlerID, ( event, clicked, interaction) -> {
            // no ideas yet
        });
    }

    public NodeLink(VisualNode source, VisualNode destination) {
        this.source = source;
        this.destination = destination;
        this.display = createLine(source.getLocation(), destination.getLocation());
        this.interaction = createInteraction(source.getLocation());
        VisualCache.addNodeLink(this);
        saveToPDC(getDisplay().getPersistentDataContainer());
    }

    public NodeLink(VisualNode source, VisualNode destination, Interaction interaction) {
        this.source = source;
        this.destination = destination;
        this.display = createLine(source.getLocation(), destination.getLocation());
        this.interaction = interaction;
        VisualCache.addNodeLink(this);
        saveToPDC(getDisplay().getPersistentDataContainer());
    }

    private BlockDisplay createLine(Location source, Location destination) {
        source = source.clone();
        source.setYaw(0.0f);
        source.setPitch(0.0f);
        destination = destination.clone();
        destination.setYaw(0.0f);
        destination.setPitch(0.0f);
        final Location midpoint = TransformationUtils.getMidpoint(source, destination);
        return new ModelLine()
                .from(TransformationUtils.getDisplacement(midpoint, source))
                .to(TransformationUtils.getDisplacement(midpoint, destination))
                .thickness(0.05f)
                .roll(0)
                .brightness(15)
                .material(Material.WHITE_CONCRETE)
                .build(midpoint);
    }

    private Interaction createInteraction(Location location) {
        Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.getPersistentDataContainer().set(new NamespacedKey(Configurator.getInstance(), "c_interaction_belongs_to"), DataType.UUID, display.getUniqueId());
        interaction.setInteractionHeight(display.getDisplayHeight() + 1);
        interaction.setInteractionWidth(display.getDisplayWidth() + 1);
        return interaction;
    }

    public void saveToPDC(PersistentDataContainer pdc) {
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_link_uuid"), DataType.UUID, display.getUniqueId());
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_link_source"), DataType.UUID, source.getUniqueId());
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_link_destination"), DataType.UUID, destination.getUniqueId());
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_link_interaction"), DataType.UUID, interaction.getUniqueId());
        pdc.set(new NamespacedKey(Configurator.getInstance(), "c_interact_handler"), DataType.INTEGER, interactHandlerID);
    }

    public static NodeLink loadFromPDC(PersistentDataContainer pdc) {
        UUID uuid = pdc.get(new NamespacedKey(Configurator.getInstance(), "c_link_uuid"), DataType.UUID);
        if (uuid == null) {
            return null;
        }

        UUID sourceUUID = pdc.get(new NamespacedKey(Configurator.getInstance(), "c_link_source"), DataType.UUID);
        if (sourceUUID == null) {
            return null;
        }

        UUID destinationUUID = pdc.get(new NamespacedKey(Configurator.getInstance(), "c_link_destination"), DataType.UUID);
        if (destinationUUID == null) {
            return null;
        }

        VisualNode source = VisualCache.getVisualNode(sourceUUID);
        if (source == null) {
            return null;
        }

        VisualNode destination = VisualCache.getVisualNode(destinationUUID);
        if (destination == null) {
            return null;
        }

        UUID interactionUUID = pdc.get(new NamespacedKey(Configurator.getInstance(), "c_link_interaction"), DataType.UUID);
        if (interactionUUID == null) {
            return null;
        }

        Entity interactionEntity = Bukkit.getEntity(interactionUUID);
        if (!(interactionEntity instanceof Interaction interaction)) {
            return null;
        }

        return new NodeLink(source, destination, interaction);
    }

    public UUID getUniqueId() {
        return display.getUniqueId();
    }

    public Location getLocation() {
        return display.getLocation();
    }

    public void remove() {
        display.remove();
        interaction.remove();
        VisualCache.removeNodeLink(this);
    }
}
