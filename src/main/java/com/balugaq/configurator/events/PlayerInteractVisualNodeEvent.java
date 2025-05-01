package com.balugaq.configurator.events;

import com.balugaq.configurator.visual.VisualNode;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerInteractVisualNodeEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final VisualNode visualNode;

    public PlayerInteractVisualNodeEvent(@NotNull Player who, @NotNull VisualNode visualNode) {
        super(who);
        this.visualNode = visualNode;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
