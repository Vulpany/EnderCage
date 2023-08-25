package com.pany.mods.entity_capturing_tool.mixin;

import com.pany.mods.entity_capturing_tool.EntityCapturingTool;
import com.pany.mods.entity_capturing_tool.Helpers.ContainedObject;
import com.pany.mods.entity_capturing_tool.Helpers.ContainmentHandler;
import com.pany.mods.entity_capturing_tool.injectedinterfaces.soundgetting;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.function.Function;

@Mixin(MobEntity.class)
public class mobentitymixin implements soundgetting {
    @Shadow
    protected SoundEvent getAmbientSound() {return null;};

    @Override
    public SoundEvent GetAmbientPublic() {
        return getAmbientSound();
    }

    @Inject(method = "interact",at = @At("HEAD"),cancellable = true)
    private void endercage$interaction(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        MobEntity entity = (MobEntity)(Object)this;
        if (entity.isAlive()) {
            ItemStack stack = player.getStackInHand(hand);
            ContainedObject containedObject = new ContainedObject(stack);
            if (Objects.equals(EntityCapturingTool.EnderCageBlock.asItem(),stack.getItem()) && !containedObject.ContainsEntity() ) {
                if (entity.getWorld().isClient) {
                    cir.setReturnValue(ActionResult.PASS);
                    cir.cancel();
                    return;
                }
                boolean Success = containedObject.CaptureEntity(entity,entity.getWorld(),player);
                if (Success) {
                    player.getWorld().playSound(null,entity.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS,2,1.75f);
                    cir.setReturnValue(ActionResult.PASS);
                }
            }
        }
    }
}
