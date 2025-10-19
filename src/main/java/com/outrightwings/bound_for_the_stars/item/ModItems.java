package com.outrightwings.bound_for_the_stars.item;

import com.outrightwings.bound_for_the_stars.block.ModBlocks;
import com.outrightwings.bound_for_the_stars.entity.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> SPACESHIP_ITEM = ITEMS.register("spaceship_item", () -> new SpaceshipItem(new Item.Properties()));
    public static final RegistryObject<Item> ALIEN_SPAWN_EGG = ITEMS.register("alien_spawn_egg",()-> new ForgeSpawnEggItem(ModEntities.ALIEN_ENTITY,0x48594d,0x4d4859, new Item.Properties()));
    public static final RegistryObject<Item> BLASTER = ITEMS.register("blaster", () -> new Blaster(new Item.Properties().stacksTo(1).durability(128)));
    public static final RegistryObject<Item> MOON_COW_SPAWN_EGG = ITEMS.register("moon_cow_spawn_egg",()-> new ForgeSpawnEggItem(ModEntities.MOON_COW_ENTITY,0x61438a,0x310e61, new Item.Properties()));
    public static final RegistryObject<Item> TINY_TARDIGRADE_SPAWN_EGG = ITEMS.register("tiny_tardigrade_spawn_egg",()-> new ForgeSpawnEggItem(ModEntities.TINY_TARDIGRADE_ENTITY,0xd8acf2,0xdcf0a1, new Item.Properties()));
    public static final RegistryObject<Item> MOON_MELON_SLICE = ITEMS.register("moon_melon_slice", () -> new Item(new Item.Properties().food(Foods.MELON_SLICE)));
    public static final RegistryObject<Item> MOON_MELON_SEEDS = ITEMS.register("moon_melon_seeds",() ->  new ItemNameBlockItem(ModBlocks.MOON_MELON_STEM.get(), new Item.Properties()));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> MOD_TAB = CREATIVE_TABS.register(MODID, () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + MODID))
        .icon(() -> SPACESHIP_ITEM.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            ITEMS.getEntries().stream().filter(o -> o.get().getDescriptionId().contains(MODID)).forEach((object ->
                    output.accept(object.get().asItem())
            ));
        }).build());
}
