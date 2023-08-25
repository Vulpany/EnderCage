package com.pany.mods.entity_capturing_tool.mixin;

import com.pany.mods.entity_capturing_tool.EntityCapturingTool;
import com.pany.mods.entity_capturing_tool.Helpers.Shapes.EnderCageShapes;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ParticleManager.class)
public class particlemanagermixin {
    @Redirect(method = "addBlockBreakParticles",at = @At(value = "INVOKE",target = "Lnet/minecraft/block/BlockState;getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;"))
    private VoxelShape endercage$blockbreakshape(BlockState instance, BlockView blockView, BlockPos pos) {
        if (instance.getBlock().equals(EntityCapturingTool.EnderCageBlock)) {
            return EnderCageShapes.BlockBreakShape;
        }
        return instance.getOutlineShape(blockView,pos);
    }
}
