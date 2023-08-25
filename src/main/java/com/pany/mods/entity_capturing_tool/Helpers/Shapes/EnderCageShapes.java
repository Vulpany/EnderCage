package com.pany.mods.entity_capturing_tool.Helpers.Shapes;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class EnderCageShapes {
    public static final VoxelShape BlockBreakShape = VoxelShapes.cuboid(0,0,0,1,1,1);
    public static VoxelShape EnderCageShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.9375, 0.25, 0.75, 1.0625, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.875, 0.0625, 0.9375, 0.9375, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.9375, 0.875, 0, 1, 1, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.875, 0, 0.0625, 1, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.875, 0.9375, 0.9375, 1, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.875, 0, 0.9375, 1, 0.0625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.9375, 0.1875, 0.8125, 1, 0.8125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0, 1, 0.1875, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.8125, 0, 1, 0.875, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.1875, 0.75, 0.125, 0.8125, 0.8125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.1875, 0.625, 0.125, 0.8125, 0.6875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.1875, 0.3125, 0.125, 0.8125, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.1875, 0.1875, 0.125, 0.8125, 0.25));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.1875, 0.4375, 0.125, 0.8125, 0.5625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.1875, 0.75, 0.9375, 0.8125, 0.8125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.1875, 0.625, 0.9375, 0.8125, 0.6875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.1875, 0.3125, 0.9375, 0.8125, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.1875, 0.1875, 0.9375, 0.8125, 0.25));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.1875, 0.4375, 0.9375, 0.8125, 0.5625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.1875, 0.0625, 0.125, 0.8125, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.1875, 0.0625, 0.25, 0.8125, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.1875, 0.0625, 0.375, 0.8125, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.625, 0.1875, 0.0625, 0.6875, 0.8125, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.75, 0.1875, 0.0625, 0.8125, 0.8125, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.1875, 0.0625, 0.9375, 0.8125, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0.1875, 0.0625, 0.5625, 0.8125, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.1875, 0.875, 0.125, 0.8125, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.1875, 0.875, 0.25, 0.8125, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.1875, 0.875, 0.375, 0.8125, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.625, 0.1875, 0.875, 0.6875, 0.8125, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.75, 0.1875, 0.875, 0.8125, 0.8125, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.1875, 0.875, 0.9375, 0.8125, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0.1875, 0.875, 0.5625, 0.8125, 0.9375));

        return shape;
    }
}
