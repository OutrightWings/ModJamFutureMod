package com.outrightwings.bound_for_the_stars.block;

import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

public class ModBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> TELESCOPE = createBlockWItem("telescope",()->new TelescopeBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOON_TERRACOTTA = createBlockWItem("moon_terracotta",()->new GlazedTerracottaBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOON_DUST = createBlockWItem("moon_dust",() -> new GravelBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOONSTONE_PILLAR = createBlockWItem("moonstone_pillar",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOONSTONE_TERRACOTTA = createBlockWItem("moonstone_terracotta",()->new GlazedTerracottaBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOON_DUST_ROCKY = createBlockWItem("moon_dust_rocky",() -> new GravelBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOONSTONE_PILLAR_END = createBlockWItem("moonstone_pillar_end",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

        //Block sets
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> BLACK_COBBLE = createBaseBlockSet("black_cobble");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> BLACK_ROCK = createBaseBlockSet("black_rock");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> MOON_ROCK = createBaseBlockSet("moon_rock");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> RED_COBBLE = createBaseBlockSet("red_cobble");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> RED_ROCK = createBaseBlockSet("red_rock");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> CHEESE = createBaseBlockSet("cheese");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> MOON_COBBLE = createBaseBlockSet("moon_cobble");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> MOONSTONE = createBaseBlockSet("moonstone");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> PACKED_MOON_DUST = createBaseBlockSet("packed_moon_dust");



        private static List<Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>>> createBaseBlockSet(String name){
                List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> blockSet = new ArrayList<>();
                var base = BLOCKS.register(name,()->new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
                var stair = BLOCKS.register(name+"_stairs",()->new StairBlock(base.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE)));
                var slab = BLOCKS.register(name+"_slab",()->new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
                var wall = BLOCKS.register(name+"_wall",()->new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
                blockSet.add(new Tuple<>(base,createBlockItem(base,name)));
                blockSet.add(new Tuple<>(base,createBlockItem(stair,name+"_stairs")));
                blockSet.add(new Tuple<>(base,createBlockItem(slab,name+"_slab")));
                blockSet.add(new Tuple<>(base,createBlockItem(wall,name+"_wall")));
                return blockSet;
        }
        private static Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> createBlockWItem(String name, Supplier<? extends Block> sup){
                var block = BLOCKS.register(name,sup);
                var item = createBlockItem(block,name);
                return new Tuple<>(block,item);
        }
        private static RegistryObject<BlockItem> createBlockItem(RegistryObject<? extends Block> block, String itemName){
                return ModItems.ITEMS.register(itemName,()->new BlockItem(block.get(),new Item.Properties()));
        }
}
