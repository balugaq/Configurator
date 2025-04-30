package com.balugaq.configurator.visual.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class InteractionId extends CustomId {
    public InteractionId() {
        super();
    }
    public InteractionId(final CustomId id) {
        super(id);
    }
    public InteractionId(final String uuid) {
        super(uuid);
    }
    public InteractionId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public Optional<Interaction> get() {
        return (Bukkit.getEntity(getUUID()) instanceof final Interaction interaction)
                ? Optional.of(interaction)
                : Optional.empty();
    }
}
