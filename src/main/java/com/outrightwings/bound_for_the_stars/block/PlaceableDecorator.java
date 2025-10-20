package com.outrightwings.bound_for_the_stars.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PlaceableDecorator extends PinkPetalsBlock {
    public PlaceableDecorator(Properties properties) {
        super(properties);
    }

    public boolean isValidBonemealTarget(LevelReader p_272968_, BlockPos p_273762_, BlockState p_273662_, boolean p_273778_) {
        return false;
    }
    protected boolean mayPlaceOn(BlockState p_51042_, BlockGetter p_51043_, BlockPos p_51044_) {
        return !p_51042_.is(Blocks.AIR) && !p_51042_.is(ModBlocks.MOON_ROCKS.getA().get());
    }
}
