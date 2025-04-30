package com.balugaq.configurator.visual.lib;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author metamechanists
 * @author Idra
 */
@SuppressWarnings("OverlyCoupledClass")
@UtilityClass
public class QuapticCache {
    private final Map<DirectBeamId, DirectBeam> directBeams = new ConcurrentHashMap<>();
    private void garbageCollect(final @NotNull Map<? extends ComplexCustomId, ?> map) {
        map.keySet().stream().filter(x -> !x.isValid()).forEach(map::remove);
    }
    public void garbageCollect() {
        garbageCollect(directBeams);
    }

    public Optional<DirectBeam> getBeam(final DirectBeamId id) {
        if (!directBeams.containsKey(id)) {
            directBeams.put(id, new DirectBeam(id));
        }
        return Optional.ofNullable(directBeams.get(id));
    }
}
