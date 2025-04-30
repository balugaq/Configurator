package com.balugaq.configurator.visual.lib;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author metamechanists
 * @author Idra
 */
public abstract class CustomId {
    private final UUID uuid;

    protected CustomId() {
        this.uuid = UUID.randomUUID();
    }
    protected CustomId(final String uuid) {
        this.uuid = UUID.fromString(uuid);
    }
    protected CustomId(final @NotNull CustomId id) {
        this.uuid = id.uuid;
    }
    protected CustomId(final UUID uuid) {
        this.uuid = uuid;
    }

    @SuppressWarnings("unused")
    public abstract Object get();

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof final CustomId customId && uuid.equals(customId.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
