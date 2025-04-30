package com.balugaq.configurator.visual;

import com.balugaq.configurator.Configurator;
import com.balugaq.configurator.data.relation.Node;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

@Data
public class VisualNode {
    private Node node;
    private Location location;
    private ItemDisplay itemDisplay;

    public VisualNode(Node node, Location location) {
        this.node = node;
        this.location = location;
        this.itemDisplay = createItemDisplay(location);
    }

    private ItemDisplay createItemDisplay(Location location) {
        // 创建ItemDisplay实体并设置其属性
        ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setCustomName(node.getKey()); // 设置实体上方显示的名称为配置键名
        itemDisplay.setCustomNameVisible(true); // 显示名称
        return itemDisplay;
    }

    // 将VisualNode数据保存到PDC
    public void saveToPDC(PersistentDataContainer pdc) {
        node.saveToPDC(pdc); // 保存Node数据
        pdc.set(new NamespacedKey(Configurator.getInstance(), "location_" + node.getUuid()), PersistentDataType.STRING, locationToString(location));
    }

    // 从PDC加载VisualNode数据
    public static VisualNode loadFromPDC(PersistentDataContainer pdc, UUID uuid) {
        Node node = Node.loadFromPDC(pdc, uuid); // 加载Node数据
        String locationStr = pdc.get(new NamespacedKey(Configurator.getInstance(), "location_" + uuid), PersistentDataType.STRING);
        Location location = stringToLocation(locationStr);
        return new VisualNode(node, location);
    }

    private String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }

    private static Location stringToLocation(String locationStr) {
        String[] parts = locationStr.split(",");
        if (parts.length == 4) {
            String worldName = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            return new Location(Configurator.getInstance().getServer().getWorld(worldName), x, y, z);
        }
        return null;
    }
}