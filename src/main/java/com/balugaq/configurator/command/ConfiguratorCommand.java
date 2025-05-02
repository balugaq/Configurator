package com.balugaq.configurator.command;

import com.balugaq.configurator.Configurator;
import com.balugaq.configurator.Items;
import com.balugaq.configurator.data.VisualNodeId;
import com.balugaq.configurator.data.relation.Node;
import com.balugaq.configurator.data.writer.YAMLWriter;
import com.balugaq.configurator.visual.NodeLink;
import com.balugaq.configurator.visual.VisualCache;
import com.balugaq.configurator.visual.VisualNode;
import com.balugaq.configurator.visual.movement.ControlVisualListener;
import com.jeff_media.morepersistentdatatypes.DataType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.NamespacedKey;
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
            new NodeLink(VisualCache.getVisualNode(new VisualNodeId(UUID.fromString(strings[1]))), VisualCache.getVisualNode(new VisualNodeId(UUID.fromString(strings[2]))));
        if (strings[0].equals("control"))
            player.getInventory().addItem(Items.CONTROL_WAND.clone());
        if (strings[0].equals("connect"))
            player.getInventory().addItem(Items.CONNECT_WAND.clone());
        if (strings[0].equals("sel"))
            ControlVisualListener.listenPlayer(player, player.getTargetEntity((int) ControlVisualListener.distance));
        if (strings[0].equals("put"))
            ControlVisualListener.unlistenPlayer(player);
        if (strings[0].equals("root")) {
            VisualNode root;
            if (strings.length == 1) {
                root = VisualCache.getVisualNode(new VisualNodeId(player.getTargetEntity(3).getPersistentDataContainer().get(new NamespacedKey(Configurator.getInstance(), "c_interaction_belongs_to"), DataType.UUID)));
            } else {
                root = VisualCache.getVisualNode(new VisualNodeId(UUID.fromString(strings[1])));
            }
            new YAMLWriter().write(root.getNode(), configuration -> {
                TextComponent component = new TextComponent();
                component.addExtra("点击复制到粘贴板");
                component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, configuration.saveToString()));
                player.sendMessage(component);
            });
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
