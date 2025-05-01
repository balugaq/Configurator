package com.balugaq.configurator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {
    public static final ItemStack CONTROL_WAND = Converter.getItem(
            Material.BLAZE_ROD,
            "&6校准棒",
            "&b右键实体开始校准"
    );

    public static final ItemStack CONNECT_WAND = Converter.getItem(
            Material.STICK,
            "&6连接棒",
            "&b右键两个实体以连接"
    );

    public static String getDisplayName(ItemStack itemStack) {
        var meta = itemStack.getItemMeta();
        if (meta == null) {
            return "Air";
        }

        return meta.getDisplayName();
    }
}
