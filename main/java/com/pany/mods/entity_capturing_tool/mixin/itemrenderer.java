package com.pany.mods.entity_capturing_tool.mixin;

import com.pany.mods.entity_capturing_tool.EntityCapturingTool;
import com.pany.mods.entity_capturing_tool.Helpers.ContainedObject;
import com.pany.mods.entity_capturing_tool.Helpers.ContainmentRenderingObject;
import com.pany.mods.entity_capturing_tool.injectedinterfaces.itemrendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.pany.mods.entity_capturing_tool.mixin.MixinConfig.MixinPriority;

@Mixin(value = ItemRenderer.class, priority = MixinPriority )
public class itemrenderer implements itemrendering {
    @Unique
    public EntityRenderDispatcher entityrenderer = null;
    @Unique
    MinecraftClient minecraftClient = null;
    @Override
    public EntityRenderDispatcher GetEntityRender() {
        return entityrenderer;
    }

    @Override
    public void SetEntityRender(EntityRenderDispatcher render) {
        entityrenderer = render;
    }

    @Override
    public MinecraftClient GetClient() {
        return minecraftClient;
    }

    @Override
    public void SetClient(MinecraftClient client) {
        minecraftClient = client;
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    private void endercage$createrender(MinecraftClient client, TextureManager manager, BakedModelManager bakery, ItemColors colors, BuiltinModelItemRenderer builtinModelItemRenderer, CallbackInfo ci) {
        ItemRenderer ActualThis = (ItemRenderer)(Object)this;
        ActualThis.SetEntityRender(client.getEntityRenderDispatcher());
        ActualThis.SetClient(client);
        // client.getEntityRenderDispatcher()
    }
    @Inject(cancellable = true,at = @At(value = "HEAD"),method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V")
    private void endercage$itemframerender(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo cir) {
        if (!stack.isEmpty() && stack.getItem().equals(EntityCapturingTool.EnderCageItem) && stack.getFrame() != null ){
            int rendadd = 0;
            ItemRenderer ActualThis = ( (ItemRenderer)(Object)this );
            ContainmentRenderingObject containedRender = stack.GetContainedRender();
            Entity entity = containedRender.GetRenderEntity(ActualThis.GetClient().world);
            ItemFrameEntity Frame = stack.getFrame();
            boolean FacingUp = Math.round(Frame.getPitch()) == -90f;
            if (entity != null && ActualThis.GetEntityRender() != null) {
                matrices.push();
                float MobSize = 0.85f;
                float Scale = MobSize / Math.max(entity.getWidth(),entity.getHeight());
                String rendtext = "renddiff";
                if (FacingUp) {
                    rendtext = "rendup";
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
                } else {
                    matrices.translate(0,-(MobSize/2),0);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                }
                matrices.scale(Scale,Scale,Scale);
                //int OldMatriceSize = matrices.
                containedRender.Render(ActualThis.GetEntityRender(),entity,0.0f,0.0f,0.0f,0.0f,0,matrices,vertexConsumers,light);
                matrices.pop();
                cir.cancel();
            }
        }
    }
    @Inject(at = @At(value = "INVOKE",target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V",shift = At.Shift.BEFORE,ordinal = 1),method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V")
    private void endercage$itemrender(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        ItemRenderer ActualThis = ( (ItemRenderer)(Object)this );
        if (ActualThis.GetEntityRender() != null) {
            if (!stack.isEmpty() && stack.getItem().equals(EntityCapturingTool.EnderCageItem) && renderMode != ModelTransformationMode.GUI ) {
                ContainmentRenderingObject containedRender = stack.GetContainedRender();
                Entity entity = containedRender.GetRenderEntity(ActualThis.GetClient().world);
                if (entity != null) {
                    long ticks = ActualThis.GetClient().world.getTime();
                    if (renderMode.isFirstPerson()) {
                        matrices.translate(0.5,1.25 + 0.1 * Math.sin(ticks * (  (Math.PI*2)/150 ) ),0.5);
                        matrices.scale(0.4f,0.4f,0.4f);
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float)ticks * (float)(  (Math.PI*2)/100 )));
                        containedRender.Render(ActualThis.GetEntityRender(),entity,0.0f,0.0f,0.0f,0.0f,0,matrices,vertexConsumers,light);
                    } else {
                        float SizeMultiplier = 0.5f / (float)Math.max(entity.getWidth(),entity.getHeight());
                        matrices.translate(0.5,(1f/16f)*3f,0.5);
                        matrices.scale(SizeMultiplier,SizeMultiplier,SizeMultiplier);
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                        containedRender.Render(ActualThis.GetEntityRender(),entity,0.0f,0.0f,0.0f,0.0f,0,matrices,vertexConsumers,light);
                    }
                }
            }
        } else if (ActualThis.GetClient().getEntityRenderDispatcher() != null) {
            ActualThis.SetEntityRender( ActualThis.GetClient().getEntityRenderDispatcher() );
        }
    }
}
