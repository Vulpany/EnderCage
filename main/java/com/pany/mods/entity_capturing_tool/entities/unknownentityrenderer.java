package com.pany.mods.entity_capturing_tool.entities;

import com.pany.mods.entity_capturing_tool.EntityCapturingTool;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class unknownentityrenderer extends MobEntityRenderer<Unknownentity,unknownentitymodel> {

    public unknownentityrenderer(EntityRendererFactory.Context context, unknownentitymodel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Override
    public Identifier getTexture(Unknownentity entity) {
        return EntityCapturingTool.EnderCageIdentifier("textures/entity/unknowntexture.png");
    }
}
