package com.outrightwings.bound_for_the_stars.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CheeseCauldron extends Block {
    public CheeseCauldron(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.isEmpty()) {
            if (!world.isClientSide) {
                if(!player.addItem(new ItemStack(ModBlocks.CHEESE.get(0).getB().get()))){
                    player.drop(new ItemStack(ModBlocks.CHEESE.get(0).getB().get()),true);
                }

                world.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }

    public static class MilkCauldron extends Block {
        private static final int CHEESE_TIMER_TICKS = 20 * 60 * 5; // 5 minutes in ticks

        public MilkCauldron(Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            ItemStack heldItem = player.getItemInHand(hand);

            if (heldItem.getItem() == Items.BUCKET) {
                if (!world.isClientSide) {
                    // Give milk bucket
                    player.setItemInHand(hand, new ItemStack(Items.MILK_BUCKET));
                    // Replace with normal cauldron if emptied
                    world.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
                }
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
            return InteractionResult.PASS;
        }

        @Override
        public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
            if (!world.isClientSide) {
                // Schedule cheese conversion in 5 minutes
                world.scheduleTick(pos, this, CHEESE_TIMER_TICKS);
            }
        }

        @Override
        public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
            // Transform into cheese cauldron
            world.setBlock(pos, ModBlocks.CHEESE_CAULDRON.get().defaultBlockState(), 3);
        }
    }

}
