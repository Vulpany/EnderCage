package com.pany.mods.entity_capturing_tool.entities;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class unknownentitymodel extends EntityModel<Unknownentity> {
    private final ModelPart modelmain;
    public unknownentitymodel(ModelPart root) {
        this.modelmain = root.getChild("unknownmodeldisplay");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bb_main = modelPartData.addChild("unknownmodeldisplay", ModelPartBuilder.create().uv(0, 12).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 4).cuboid(-1.0F, -8.0F, -1.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -13.0F, -1.0F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 8).cuboid(1.0F, -11.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 8).cuboid(-1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 16, 16);
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        modelmain.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(Unknownentity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}