package com.outrightwings.bound_for_the_stars.block;

import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

public class ModBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        //Blocks with auto items
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> TELESCOPE = createBlockWItem("telescope",()->new TelescopeBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOON_DUST = createBlockWItem("moon_dust",() -> new MoonDustBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOONSTONE_PILLAR = createBlockWItem("moonstone_pillar",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOONSTONE_TERRACOTTA = createBlockWItem("moonstone_terracotta",()->new GlazedTerracottaBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOON_DUST_ROCKY = createBlockWItem("moon_dust_rocky",() -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOONSTONE_PILLAR_END = createBlockWItem("moonstone_pillar_end",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> LAYERED_MOONSTONE = createBlockWItem("layered_moonstone",() -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOON_DUST_PATH = createBlockWItem("moon_dust_path",() -> new MoonPathBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> LUNITE_ORE = createBlockWItem("lunite_ore",() -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> STRANGE_WALL = createBlockWItem("strange_wall",() -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOON_ROCKS = createBlockWItem("moon_rocks",() -> new PlaceableDecorator(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noCollission().sound(SoundType.STONE).pushReaction(PushReaction.DESTROY)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> DUST_LAYER = createBlockWItem("dust_layer",() -> new MoonDustLayer(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> LUNITE_BLOCK = createBlockWItem("lunite_block",()->new GlazedTerracottaBlock(BlockBehaviour.Properties.copy(Blocks.GLOWSTONE).lightLevel((level) -> 8)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> MOON_MELON = createBlockWItem("moon_melon",()->new MoonMelon(BlockBehaviour.Properties.copy(Blocks.MELON)));
        public static final Tuple<RegistryObject<? extends Block>, RegistryObject<BlockItem>> STRIPPED_MOON_MELON = createBlockWItem("stripped_moon_melon",()->new Block(BlockBehaviour.Properties.copy(Blocks.MELON)));
        //Block sets
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> BLACK_COBBLE = createBaseBlockSet("black_cobble");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> BLACK_ROCK = createBaseBlockSet("black_rock");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> RED_COBBLE = createBaseBlockSet("red_cobble");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> RED_ROCK = createBaseBlockSet("red_rock");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> CHEESE = createBaseBlockSet("cheese");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> PURPLE_CHEESE = createBaseBlockSet("purple_cheese");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> MOON_COBBLE = createBaseBlockSet("moon_cobble");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> MOONSTONE = createBaseBlockSet("moonstone");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> PACKED_MOON_DUST = createBaseBlockSet("packed_moon_dust");
        public static final List<Tuple<RegistryObject<? extends Block>,RegistryObject<BlockItem>>> SMOOTH_MOONSTONE = createBaseBlockSet("smooth_moonstone");
        //Block
        public static final RegistryObject<? extends Block> MOON_MELON_STEM_ATTACHED = BLOCKS.register("moon_melon_stem_attached",()-> new MoonMelonPlantAttached((StemGrownBlock)MOON_MELON.getA().get(), ModItems.MOON_MELON_SEEDS, BlockBehaviour.Properties.copy(Blocks.ATTACHED_MELON_STEM)));
        public static final RegistryObject<? extends Block> MOON_MELON_STEM = BLOCKS.register("moon_melon_stem",()-> new MoonMelonPlantAttached.MoonMelonPlant((StemGrownBlock)MOON_MELON.getA().get(), ModItems.MOON_MELON_SEEDS,BlockBehaviour.Properties.copy(Blocks.MELON_STEM) ));
        public static final RegistryObject<? extends Block> CHEESE_CAULDRON = BLOCKS.register("cheese_cauldron",()-> new CheeseCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));
        public static final RegistryObject<? extends Block> MILK_CAULDRON = BLOCKS.register("milk_cauldron",()-> new CheeseCauldron.MilkCauldron(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));


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
