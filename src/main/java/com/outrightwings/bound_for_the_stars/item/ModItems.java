package com.outrightwings.bound_for_the_stars.item;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
    public static final RegistryObject<CreativeModeTab> MOD_TAB = CREATIVE_TABS.register(MODID, () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + MODID))
        .icon(() -> SPACESHIP_ITEM.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            ForgeRegistries.ITEMS.getEntries().stream().filter(o -> o.getValue().getDescriptionId().contains(MODID)).forEach((object ->
                    output.accept(object.getValue().asItem())
            ));
        }).build());
}
