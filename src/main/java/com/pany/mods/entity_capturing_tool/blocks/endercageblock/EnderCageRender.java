package com.pany.mods.entity_capturing_tool.blocks.endercageblock;

import com.pany.mods.entity_capturing_tool.Helpers.ContainmentRenderingObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;

import static com.pany.mods.entity_capturing_tool.Helpers.MathStuff.*;

@Environment(EnvType.CLIENT)
public class EnderCageRender implements BlockEntityRenderer<EnderCageEntity> {
    private final EntityRenderDispatcher entityRenderDispatcher;
    public EnderCageRender(BlockEntityRendererFactory.Context ctx) {
        this.entityRenderDispatcher = ctx.getEntityRenderDispatcher();
    }

    @Override
    public void render(EnderCageEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        Entity contained = entity.getWorld() != null ? entity.ContainedEntity.GetRenderEntity(entity.getWorld()) : null;
        double X = entity.getPos().getX()+0.5;
        double Y = entity.getPos().getY()+0.5;
        double Z = entity.getPos().getZ()+0.5;
        ContainmentRenderingObject containedrender = entity.ContainedEntity.Renderer;
        float TargetYaw = containedrender.CageAnim.LastEntityYaw;;
        boolean PlayJumpAnim = false;
        float AnimProgress = containedrender.CageAnim.AnimProgress;
        if (entity.getWorld() != null) {
            PlayerEntity closeplayer = entity.getWorld().getClosestPlayer(X,Y,Z,10,false);
            if (closeplayer != null) {
                TargetYaw = (float)LerpPIAngle(TargetYaw,OffsetToPIAngle(closeplayer.getX()-X,closeplayer.getZ()-Z ),0.1);
                containedrender.CageAnim.LastEntityYaw = TargetYaw;
                if (GetDistance(closeplayer.getX(),closeplayer.getZ(),X,Z) < 2.5) {
                    PlayJumpAnim = true;
                }
            }
        }
        if (contained != null) {
            float Scale = 0.5f / Math.max(contained.getWidth(),contained.getHeight());
            double JumpProgress = 0;
            if (AnimProgress > 0 || PlayJumpAnim) {
                AnimProgress += (Math.PI/20);
                if (AnimProgress > Math.PI) {
                    AnimProgress = 0;
                }
            }
            if (AnimProgress > 0) {
                JumpProgress = 0.1*Math.sin(AnimProgress);
            }
            contained.setYaw(0);
            contained.setHeadYaw(0);
            contained.setPitch(0);
            //System.out.println(contained.getName().getString());
            matrices.translate(0.5,((1f/16f)*3f) + JumpProgress,0.5);
            //
            matrices.scale(Scale,Scale,Scale);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(TargetYaw));
            this.entityRenderDispatcher.render(contained,0.0f,0.0f,0.0f,0.0f,tickDelta,matrices,vertexConsumers,light);
            matrices.translate(0,3,0);
        } else {
            AnimProgress = 0;
        }
        containedrender.CageAnim.AnimProgress = AnimProgress;
        matrices.pop();
    }
}
