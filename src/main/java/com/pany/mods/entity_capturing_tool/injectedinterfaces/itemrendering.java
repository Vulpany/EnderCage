package com.pany.mods.entity_capturing_tool.injectedinterfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;

public interface itemrendering {
    default EntityRenderDispatcher GetEntityRender() {
        return null;
    }

    default void SetEntityRender(EntityRenderDispatcher render) {}

    default MinecraftClient GetClient() {
        return null;
    }

    default void SetClient(MinecraftClient client) {}
}
