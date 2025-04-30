package com.balugaq.configurator.visual.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;

import java.util.Optional;
import java.util.UUID;

/**
 * @author metamechanists
 * @author Idra
 */
@SuppressWarnings("unused")
public class DirectBeamId extends ComplexCustomId {
    public DirectBeamId() {
        super();
    }
    public DirectBeamId(final CustomId id) {
        super(id);
    }
    public DirectBeamId(final String uuid) {
        super(uuid);
    }
    public DirectBeamId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof BlockDisplay;
    }
    @Override
    public Optional<DirectBeam> get() {
        return isValid()
                ? QuapticCache.getBeam(this)
                : Optional.empty();
    }
}
