package com.outrightwings.bound_for_the_stars.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MoonDustLayer extends SnowLayerBlock implements MoonDustParticles{
    public MoonDustLayer(Properties properties) {
        super(properties);
    }
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        stepOnDust(level,pos,state,entity);
    }
}
