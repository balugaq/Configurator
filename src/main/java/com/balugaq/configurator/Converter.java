package com.balugaq.configurator;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.function.Consumer;

@ApiStatus.Experimental
public class Converter {
    public static final ItemStack AIR = new ItemStack(Material.AIR);

    /**
     * Converts a Bukkit ItemStack to another Bukkit ItemStack.
     *
     * @param itemStack the Bukkit ItemStack to convert
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(ItemStack itemStack) {
        return new CustomItemStack(itemStack).asBukkit();
    }

    /**
     * Converts a Material to a Bukkit ItemStack.
     *
     * @param material the Material to convert
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(Material material) {
        return new CustomItemStack(material).asBukkit();
    }

    /**
     * Converts a Bukkit ItemStack to another Bukkit ItemStack with custom metadata.
     *
     * @param itemStack        the Bukkit ItemStack to convert
     * @param itemMetaConsumer the consumer to modify the item metadata
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull ItemStack itemStack, @NotNull Consumer<ItemMeta> itemMetaConsumer) {
        return new CustomItemStack(itemStack, itemMetaConsumer).asBukkit();
    }

    /**
     * Converts a Material to a Bukkit ItemStack with custom metadata.
     *
     * @param material the Material to convert
     * @param meta     the consumer to modify the item metadata
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull Material material, @NotNull Consumer<ItemMeta> meta) {
        return new CustomItemStack(material, meta).asBukkit();
    }

    /**
     * Converts a Bukkit ItemStack to another Bukkit ItemStack with a name and lore.
     *
     * @param itemStack the Bukkit ItemStack to convert
     * @param name      the name of the item
     * @param lore      the lore of the item
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull ItemStack itemStack, @Nullable String name, @NotNull String @NotNull ... lore) {
        return new CustomItemStack(itemStack, name, lore).asBukkit();
    }

    /**
     * Converts a Bukkit ItemStack to another Bukkit ItemStack with a color, name, and lore.
     *
     * @param itemStack the Bukkit ItemStack to convert
     * @param color     the color of the item
     * @param name      the name of the item
     * @param lore      the lore of the item
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull ItemStack itemStack, Color color, @Nullable String name, String @NotNull ... lore) {
        return new CustomItemStack(itemStack, color, name, lore).asBukkit();
    }

    /**
     * Converts a Material to a Bukkit ItemStack with a name and lore.
     *
     * @param material the Material to convert
     * @param name     the name of the item
     * @param lore     the lore of the item
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull Material material, String name, String... lore) {
        return new CustomItemStack(material, name, lore).asBukkit();
    }

    /**
     * Converts a Material to a Bukkit ItemStack with a name and lore.
     *
     * @param material the Material to convert
     * @param name     the name of the item
     * @param lore     the lore of the item
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull Material material, String name, @NotNull List<String> lore) {
        return new CustomItemStack(material, name, lore).asBukkit();
    }

    /**
     * Converts a Bukkit ItemStack to another Bukkit ItemStack with a list of lore.
     *
     * @param itemStack the Bukkit ItemStack to convert
     * @param list      the list of lore
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull ItemStack itemStack, @NotNull List<String> list) {
        return new CustomItemStack(itemStack, list).asBukkit();
    }

    /**
     * Converts a Material to a Bukkit ItemStack with a list of lore.
     *
     * @param material the Material to convert
     * @param list     the list of lore
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull Material material, @NotNull List<String> list) {
        return new CustomItemStack(material, list).asBukkit();
    }

    /**
     * Converts a Bukkit ItemStack to another Bukkit ItemStack with a specified amount.
     *
     * @param itemStack the Bukkit ItemStack to convert
     * @param amount    the amount of the item
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull ItemStack itemStack, @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
        return new CustomItemStack(itemStack, amount).asBukkit();
    }

    /**
     * Converts a Bukkit ItemStack to another Bukkit ItemStack with a specified Material.
     *
     * @param itemStack the Bukkit ItemStack to convert
     * @param material  the Material of the item
     * @return the converted Bukkit ItemStack
     */
    public static @NotNull ItemStack getItem(@NotNull ItemStack itemStack, @NotNull Material material) {
        return new CustomItemStack(itemStack, material).asBukkit();
    }
}
