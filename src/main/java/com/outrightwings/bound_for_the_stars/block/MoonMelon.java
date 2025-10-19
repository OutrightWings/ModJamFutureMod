package com.outrightwings.bound_for_the_stars.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static com.outrightwings.bound_for_the_stars.block.ModBlocks.STRIPPED_MOON_MELON;

public class MoonMelon extends StemGrownBlock {
    public MoonMelon(Properties prop) {
        super(prop);
    }

    public StemBlock getStem() {
        return (StemBlock)ModBlocks.MOON_MELON_STEM.get();
    }

    public AttachedStemBlock getAttachedStem() {
        return (AttachedStemBlock)ModBlocks.MOON_MELON_STEM_ATTACHED.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        // Only handle server side
        if (!level.isClientSide && heldItem.getItem() instanceof AxeItem) {
            BlockState newBlockState = STRIPPED_MOON_MELON.getA().get().defaultBlockState();

            level.setBlock(pos, newBlockState, 11);

            // Damage
            heldItem.hurtAndBreak(1, player,
                    (p) -> p.broadcastBreakEvent(hand));

            // Play sound
            level.playSound(null, pos, SoundEvents.AXE_STRIP,
                    SoundSource.BLOCKS, 1.0F, 1.0F);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
