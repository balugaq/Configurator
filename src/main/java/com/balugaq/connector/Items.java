package com.balugaq.connector;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {
    public static final ItemStack CONNECT_WAND = Converter.getItem(
            Material.STICK,
            "&6连接棒 ", // intentional space at end
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
