package com.balugaq.configurator.visual;

import com.balugaq.configurator.data.relation.Node;
import com.balugaq.configurator.visual.lib.BlockDisplayId;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;

import java.util.List;

@Data
public class VisualNode {
    public VisualNode() {

    }

    public VisualNode(ItemDisplay itemDisplay) {
        this.display = itemDisplay;
        this.location = itemDisplay.getLocation();
        this.data = new Node();
    }

    public Node data;
    public ItemDisplay display;
    public Location location;
    public List<BlockDisplayId> links;
}
