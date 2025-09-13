package com.outrightwings.bound_for_the_stars.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> SPACESHIP_ITEM = ITEMS.register("spaceship_item", () -> new SpaceshipItem(new Item.Properties()));




    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> MOD_TAB = CREATIVE_TABS.register("mod_tab", () -> CreativeModeTab.builder()
        .icon(() -> SPACESHIP_ITEM.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(SPACESHIP_ITEM.get());
        }).build());
}
