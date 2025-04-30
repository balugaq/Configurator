package com.balugaq.configurator.visual.lib;

import java.util.UUID;

/**
 * @author metamechanists
 * @author Idra
 */
public abstract class ComplexCustomId extends CustomId {
    protected ComplexCustomId() {
        super();
    }
    protected ComplexCustomId(final CustomId id) {
        super(id);
    }
    protected ComplexCustomId(final String uuid) {
        super(uuid);
    }
    protected ComplexCustomId(final UUID uuid) {
        super(uuid);
    }

    public abstract boolean isValid();
}
