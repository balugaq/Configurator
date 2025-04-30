package com.balugaq.configurator.visual.lib;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author metamechanists
 * @author Idra
 */
public final class DeprecatedBeamStorage {
    private static final Collection<Beam> beams = new ConcurrentLinkedQueue<>();

    private DeprecatedBeamStorage() {}

    public static void deprecate(final Beam beam) {
        beams.add(beam);
    }

    public static void tick() {
        beams.forEach(Beam::tick);
        beams.stream().filter(Beam::expired).forEach(DeprecatedBeamStorage::remove);
    }

    public static void remove(final @NotNull Beam beam) {
        beam.remove();
        beams.remove(beam);
    }
}
