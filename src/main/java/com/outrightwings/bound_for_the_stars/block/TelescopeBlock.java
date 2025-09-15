package com.outrightwings.bound_for_the_stars.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TelescopeBlock extends HorizontalDirectionalBlock {
    private static final VoxelShape BOUNDING = Block.box(4.5,0,4.5,11.5,12,11.5);

    protected TelescopeBlock(Properties properties) {
        super(properties);
    }
    public VoxelShape getShape(BlockState p_48816_, BlockGetter p_48817_, BlockPos p_48818_, CollisionContext p_48819_) {
        return BOUNDING;
    }

    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return (BlockState)this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(new Property[]{FACING});
    }
}
