package com.outrightwings.bound_for_the_stars.item;

import com.outrightwings.bound_for_the_stars.block.ModBlocks;
import com.outrightwings.bound_for_the_stars.entity.ModEntities;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> SPACESHIP_ITEM = ITEMS.register("spaceship_item", () -> new VehicleItem(new Item.Properties()));
    public static final RegistryObject<Item> ALIEN_SPAWN_EGG = ITEMS.register("alien_spawn_egg",()-> new ForgeSpawnEggItem(ModEntities.ALIEN_ENTITY,0x48594d,0x4d4859, new Item.Properties()));
    public static final RegistryObject<Item> BLASTER = ITEMS.register("blaster", () -> new Blaster(new Item.Properties().stacksTo(1).durability(128)));
    public static final RegistryObject<Item> MOON_COW_SPAWN_EGG = ITEMS.register("moon_cow_spawn_egg",()-> new ForgeSpawnEggItem(ModEntities.MOON_COW_ENTITY,0x61438a,0x310e61, new Item.Properties()));
    public static final RegistryObject<Item> TINY_TARDIGRADE_SPAWN_EGG = ITEMS.register("tiny_tardigrade_spawn_egg",()-> new ForgeSpawnEggItem(ModEntities.TINY_TARDIGRADE_ENTITY,0xd8acf2,0xdcf0a1, new Item.Properties()));
    public static final RegistryObject<Item> MOON_MELON_SLICE = ITEMS.register("moon_melon_slice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).effect(()->new MobEffectInstance(MobEffects.WATER_BREATHING,20*30,0),1).alwaysEat().build())));
    public static final RegistryObject<Item> MOON_MELON_SEEDS = ITEMS.register("moon_melon_seeds",() ->  new ItemNameBlockItem(ModBlocks.MOON_MELON_STEM.get(), new Item.Properties()));
    public static final RegistryObject<Item> GIANT_TARDIGRADE_SPAWN_EGG = ITEMS.register("giant_tardigrade_spawn_egg",()-> new ForgeSpawnEggItem(ModEntities.GIANT_TARDIGRADE_ENTITY,0xc542f5,0xff96e7, new Item.Properties()));
    public static final RegistryObject<Item> UFO_ITEM = ITEMS.register("ufo_item", () -> new VehicleItem(new Item.Properties()){ public EntityType<? extends Animal> getEntity(){return ModEntities.UFO_ENTITY.get();}});
    public static final RegistryObject<Item> SPACE_HELMET = ITEMS.register("space_helmet",()-> new SpaceHelmet(ArmorMaterials.IRON, ArmorItem.Type.HELMET,new Item.Properties().stacksTo(1).durability(256).fireResistant()));
    public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese_item", () -> new Item(new Item.Properties().food(Foods.APPLE)));
    public static final RegistryObject<Item> CHEESE_SANDWICH = ITEMS.register("cheese_sandwich", () -> new Item(new Item.Properties().food(Foods.COOKED_BEEF)));

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
