package com.outrightwings.bound_for_the_stars.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class MoonMelonPlantAttached extends AttachedStemBlock {
    public MoonMelonPlantAttached(StemGrownBlock grownBlock, Supplier<Item> itemSupplier, Properties properties) {
        super(grownBlock, itemSupplier, properties);
    }
    protected boolean mayPlaceOn(BlockState p_48863_, BlockGetter p_48864_, BlockPos p_48865_) {
        return p_48863_.is(ModBlocks.MOON_DUST_PATH.getA().get());
    }

    public static class MoonMelonPlant extends StemBlock {
        protected static final VoxelShape SHAPE = Block.box((double)5.0F, (double)0.0F, (double)5.0F, (double)10.0F, (double)6.0F, (double)10.0F);
        public MoonMelonPlant(StemGrownBlock p_154728_, Supplier<Item> p_154729_, Properties p_154730_) {
            super(p_154728_, p_154729_, p_154730_);
        }
        public VoxelShape getShape(BlockState p_57047_, BlockGetter p_57048_, BlockPos p_57049_, CollisionContext p_57050_) {
            return SHAPE;
        }
        protected boolean mayPlaceOn(BlockState p_48863_, BlockGetter p_48864_, BlockPos p_48865_) {
            return p_48863_.is(ModBlocks.MOON_DUST_PATH.getA().get());
        }
    }
}
