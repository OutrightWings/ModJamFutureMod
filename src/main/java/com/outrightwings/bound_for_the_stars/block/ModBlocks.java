package com.outrightwings.bound_for_the_stars.block;

import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

public class ModBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        public static final RegistryObject<Block> TELESCOPE = BLOCKS.register("telescope",()->new TelescopeBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)));
        public static final RegistryObject<BlockItem> TELESCOPE_ITEM = createBlockItem(TELESCOPE,"telescope");

        private static RegistryObject<BlockItem> createBlockItem(RegistryObject<Block> block, String itemName){
                return ModItems.ITEMS.register(itemName,()->new BlockItem(block.get(),new Item.Properties()));
        }
}
