package com.outrightwings.bound_for_the_stars.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class MoonPathBlock extends DirtPathBlock implements MoonDustParticles{
    public MoonPathBlock(Properties prop) {
        super(prop);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? Block.pushEntitiesUp(this.defaultBlockState(), ModBlocks.MOON_DUST.getA().get().defaultBlockState(), context.getLevel(), context.getClickedPos()) : this.defaultBlockState();
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        BlockState blockstate = pushEntitiesUp(state, ModBlocks.MOON_DUST.getA().get().defaultBlockState(), level, pos);
        level.setBlockAndUpdate(pos, blockstate);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, blockstate));
    }
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        stepOnDust(level,pos,state,entity);
    }
}
