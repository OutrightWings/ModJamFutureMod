package com.outrightwings.bound_for_the_stars.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static com.outrightwings.bound_for_the_stars.block.ModBlocks.MOON_DUST_PATH;

public class MoonDustBlock extends GravelBlock implements MoonDustParticles{
    public MoonDustBlock(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        // Only handle server side
        if (!level.isClientSide && heldItem.getItem() instanceof ShovelItem) {
            BlockState pathState = MOON_DUST_PATH.getA().get().defaultBlockState();

            // Check that the block above is air (same condition as vanilla)
            if (level.getBlockState(pos.above()).isAir()) {
                level.setBlock(pos, pathState, 11);

                // Damage the shovel
                heldItem.hurtAndBreak(1, player,
                        (p) -> p.broadcastBreakEvent(hand));

                // Play sound
                level.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN,
                        SoundSource.BLOCKS, 1.0F, 1.0F);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        stepOnDust(level,pos,state,entity);
    }
}
