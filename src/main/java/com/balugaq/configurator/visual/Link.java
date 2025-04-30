package com.balugaq.configurator.visual;

import com.balugaq.configurator.visual.lib.DirectBeamId;
import lombok.Data;

@Data
public class Link {
    VisualNode A, B;
    DirectBeamId directBeamId;
    public void loadAB() {
        A.getLocation().getChunk().load();
        B.getLocation().getChunk().load();
    }
}
