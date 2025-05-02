package com.balugaq.configurator.visual.chat;

import com.balugaq.configurator.Configurator;
import com.balugaq.configurator.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ChatListener implements Listener {
    private static final List<Pair<UUID, Consumer<String>>> listeningPlayer = new CopyOnWriteArrayList<>();
    @EventHandler
    public void onInput(AsyncPlayerChatEvent event) {
        for (Pair<UUID, Consumer<String>> pair : listeningPlayer) {
            if (pair.first.equals(event.getPlayer().getUniqueId())) {
                pair.second.accept(event.getMessage());
                listeningPlayer.remove(pair);
                event.setCancelled(true);
                break;
            }
        }
    }

    public static void awaitInput(Player player, Consumer<String> consumer) {
        listeningPlayer.add(new Pair<>(player.getUniqueId(), consumer));
    }
}
