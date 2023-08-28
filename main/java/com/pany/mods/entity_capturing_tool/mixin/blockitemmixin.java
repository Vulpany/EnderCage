package com.pany.mods.entity_capturing_tool.mixin;

import com.pany.mods.entity_capturing_tool.EntityCapturingTool;
import com.pany.mods.entity_capturing_tool.Helpers.ContainedObject;
import com.pany.mods.entity_capturing_tool.blocks.endercageblock.EnderCage;
import com.pany.mods.entity_capturing_tool.blocks.endercageblock.EnderCageEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static com.pany.mods.entity_capturing_tool.EntityCapturingTool.EnderCageItem;
import static com.pany.mods.entity_capturing_tool.mixin.MixinConfig.MixinPriority;

@Mixin(value = BlockItem.class, priority = MixinPriority )
public class blockitemmixin {
    @Inject(method = "useOnBlock",at = @At("HEAD"),cancellable = true)
    private void endercage$useonblock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = context.getStack();
        if (context.getHand() == Hand.MAIN_HAND && Objects.equals(stack.getItem(), EnderCageItem) ) {
            if (!context.getWorld().isClient) {
                ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
                if (!player.isSneaking()) {
                    ContainedObject StackObject = new ContainedObject(stack);
                    BlockState TargetBlock = context.getWorld().getBlockState(context.getBlockPos());
                    if (StackObject.ContainsEntity() && !(TargetBlock.getBlock() instanceof EnderCage) ) {
                        Direction direction = context.getSide();
                        EntityType<?> entityType = StackObject.GetEntityType();
                        context.getWorld().playSound(null,context.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN,SoundCategory.BLOCKS,2,1.5f);
                        StackObject.ReleaseEntity(context.getHitPos().add( new Vec3d(direction.getOffsetX()*(entityType.getWidth()/2),-entityType.getHeight()/2 + direction.getOffsetY()*(entityType.getHeight()/2+0.1),direction.getOffsetZ()*(entityType.getWidth()/2) ) ),context.getWorld(),context.getPlayer() );
                    }
                    cir.setReturnValue(ActionResult.PASS);
                }
            } else {
                ClientPlayerEntity player = (ClientPlayerEntity)context.getPlayer();
                if (!player.isSneaking()) {
                    cir.setReturnValue(ActionResult.PASS);
                }
            }
        } else if (context.getHand() == Hand.MAIN_HAND && stack.getItem().equals(Blocks.NOTE_BLOCK.asItem()) && context.getWorld().getBlockState(context.getBlockPos()).getBlock().equals(EntityCapturingTool.EnderCageBlock) ) {
            EnderCageEntity cageEntity = (EnderCageEntity)context.getWorld().getBlockEntity(context.getBlockPos());
            PlayerEntity player = context.getPlayer();
            if (!player.isSneaking() && cageEntity.GetSilentProperty()) {
                if (!context.getWorld().isClient) {
                    context.getPlayer().getActiveItem().decrement(1);
                    cageEntity.SetSilentProperty(false);
                    context.getWorld().playSound(null,context.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value(), SoundCategory.BLOCKS,1f,1f);
                    Vec3d Center = context.getBlockPos().toCenterPos();
                    ( (ServerWorld)context.getWorld()).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK,Blocks.NOTE_BLOCK.getDefaultState()),Center.getX(),Center.getY(),Center.getZ(),30,0.5,0.5,0.5,0);
                    cir.setReturnValue(ActionResult.PASS);
                } else {
                    cir.setReturnValue(ActionResult.PASS);
                    cir.cancel();
                }

            }
        }
    }
}
