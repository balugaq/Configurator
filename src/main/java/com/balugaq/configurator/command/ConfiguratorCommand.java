package com.balugaq.configurator.command;

import com.balugaq.configurator.Items;
import com.balugaq.configurator.data.relation.Node;
import com.balugaq.configurator.visual.NodeLink;
import com.balugaq.configurator.visual.VisualCache;
import com.balugaq.configurator.visual.VisualNode;
import com.balugaq.configurator.visual.movement.ControlVisualListener;
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
            new VisualNode(new Node(), player.getLocation());
        if (strings[0].equals("link"))
            new NodeLink(VisualCache.getVisualNode(UUID.fromString(strings[1])), VisualCache.getVisualNode(UUID.fromString(strings[2])));
        if (strings[0].equals("control"))
            player.getInventory().addItem(Items.CONTROL_WAND.clone());
        if (strings[0].equals("connect"))
            player.getInventory().addItem(Items.CONNECT_WAND.clone());
        if (strings[0].equals("sel"))
            ControlVisualListener.listenPlayer(player, player.getTargetEntity((int) ControlVisualListener.distance));
        if (strings[0].equals("put"))
            ControlVisualListener.unlistenPlayer(player);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
