package com.pany.mods.entity_capturing_tool.injectedinterfaces;

import net.minecraft.sound.SoundEvent;

public interface soundgetting {
    public default SoundEvent GetAmbientPublic() {
        return null;
    }
}
