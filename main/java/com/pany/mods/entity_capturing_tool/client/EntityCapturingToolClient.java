package com.pany.mods.entity_capturing_tool.client;

import com.pany.mods.entity_capturing_tool.EntityCapturingTool;
import com.pany.mods.entity_capturing_tool.blocks.endercageblock.EnderCageRender;
import com.pany.mods.entity_capturing_tool.entities.unknownentitymodel;
import com.pany.mods.entity_capturing_tool.entities.unknownentityrenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntityCapturingToolClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_CUBE_LAYER = new EntityModelLayer(EntityCapturingTool.EnderCageIdentifier("unknownentitydisplay"), "main");

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(EntityCapturingTool.EnderCageBlockEntity, EnderCageRender::new);

        EntityRendererRegistry.register(EntityCapturingTool.UnknownEntityType, (context) -> {
            return new unknownentityrenderer(context,new unknownentitymodel(unknownentitymodel.getTexturedModelData().createModel()),2f );
        });


        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, unknownentitymodel::getTexturedModelData);
    }
}
