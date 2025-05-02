package com.balugaq.configurator;

import com.balugaq.configurator.visual.chat.ChatListener;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ChatUtils {
    public static void awaitInput(Player player, String prompt, Consumer<String> consumer) {
        awaitInput(player, Component.text(prompt), consumer);
    }

    public static void awaitInput(Player player, Component prompt, Consumer<String> consumer) {
        player.sendMessage(prompt);
        ChatListener.awaitInput(player, consumer);
    }
}
