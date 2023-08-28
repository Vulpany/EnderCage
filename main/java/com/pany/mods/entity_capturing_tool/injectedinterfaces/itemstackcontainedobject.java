package com.pany.mods.entity_capturing_tool.injectedinterfaces;

import com.pany.mods.entity_capturing_tool.Helpers.ContainedObject;
import com.pany.mods.entity_capturing_tool.Helpers.ContainmentRenderingObject;

public interface itemstackcontainedobject {
    default ContainmentRenderingObject GetContainedRender() {
        return null;
    }
    default boolean GetIfRenderDirty() {
        return false;
    }
    default void SetIfRenderDirty(boolean Val) {}
}
