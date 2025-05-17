package com.balugaq.connector.command;

import com.balugaq.connector.Items;
import com.balugaq.connector.data.VisualNodeId;
import com.balugaq.connector.visual.NodeLink;
import com.balugaq.connector.visual.VisualCache;
import com.balugaq.connector.visual.VisualNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ConfiguratorCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp()) {
            return false;
        }

        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (strings[0].equals("node"))
            new VisualNode(player.getLocation());
        if (strings[0].equals("link"))
            new NodeLink(VisualCache.getVisualNode(new VisualNodeId(UUID.fromString(strings[1]))), VisualCache.getVisualNode(new VisualNodeId(UUID.fromString(strings[2]))));
        if (strings[0].equals("connect"))
            player.getInventory().addItem(Items.CONNECT_WAND.clone());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
