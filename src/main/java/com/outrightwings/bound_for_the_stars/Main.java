package com.outrightwings.bound_for_the_stars;

import com.mojang.logging.LogUtils;
import com.outrightwings.bound_for_the_stars.block.ModBlocks;
import com.outrightwings.bound_for_the_stars.entity.ModEntities;
import com.outrightwings.bound_for_the_stars.entity.ModVillager;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import com.outrightwings.bound_for_the_stars.network.ModPackets;
import com.outrightwings.bound_for_the_stars.particle.ModParticle;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "bound_for_the_stars";
    public static final Logger LOGGER = LogUtils.getLogger();


//    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
//    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
//            .alwaysEat().nutrition(1).saturationMod(2f).build())));


    public Main()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModItems.CREATIVE_TABS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModVillager.POI.register(modEventBus);
        ModVillager.PROFESSION.register(modEventBus);
        ModParticle.PARTICLE_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
//        LOGGER.debug("Start here");
//        ForgeRegistries.ITEMS.getEntries().stream().filter(o -> o.getValue().getDescriptionId().contains(MODID)).forEach((object ->
//                LOGGER.debug(object.getValue().asItem().getDescriptionId())
//        ));
        event.enqueueWork(()->{
            ModPackets.register();
        });
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

}
//TODO milk cauldron to cheese
//TODO grilled cheese
//TODO eat cheese
//TODO walls tagged as walls
//TODO moondsut particles check for on block
//TODO Villager skin
//TODO moon skybox
//TODO moon structures

//TODO Alien noises
//TODO Alien walk animation controller
//TODO why alien not neutral?

//Lofty ideas?
//No gravity blocks in space
//  Astroids of gravel that dont fall