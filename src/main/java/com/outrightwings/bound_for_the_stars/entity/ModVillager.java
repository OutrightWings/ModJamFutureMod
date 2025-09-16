package com.outrightwings.bound_for_the_stars.entity;

import com.google.common.collect.ImmutableSet;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.block.ModBlocks;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModVillager {
    //POI
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, Main.MODID);
    public static RegistryObject<PoiType> ASTRONOMER_POI = POI.register("astronomer",()-> new PoiType(ImmutableSet.copyOf(ModBlocks.TELESCOPE.get().getStateDefinition().getPossibleStates()), 1, 1));

    //Profession
    public static final DeferredRegister<VillagerProfession> PROFESSION = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, Main.MODID);
    public static final RegistryObject<VillagerProfession> ASTRONOMER_PROFESSION = PROFESSION.register("astronomer", ModVillager::buildVillagerProfession);

    private static VillagerProfession buildVillagerProfession() {
        Predicate<Holder<PoiType>> heldJobSite = (poiType) -> {
            return poiType.is(ASTRONOMER_POI.getKey());
        };
        Predicate<Holder<PoiType>> acquirableJobSite = (poiType) -> {
            return poiType.is(ASTRONOMER_POI.getKey());
        };
        return new VillagerProfession("astronomer", heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), null);
    }

    //Trades
    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event){
        if(event.getType() == ASTRONOMER_PROFESSION.get()){
            List<VillagerTrades.ItemListing> level1 = new ArrayList<>();
            List<VillagerTrades.ItemListing> level2 = new ArrayList<>();
            List<VillagerTrades.ItemListing> level3 = new ArrayList<>();
            List<VillagerTrades.ItemListing> level4 = new ArrayList<>();
            List<VillagerTrades.ItemListing> level5 = new ArrayList<>();
            //Todo add trades
            level1.add(new BasicItemListing(25, ModItems.SPACESHIP_ITEM.get().getDefaultInstance(),5,5));

            event.getTrades().put(1, level1);
            event.getTrades().put(2, level2);
            event.getTrades().put(3, level3);
            event.getTrades().put(4, level4);
            event.getTrades().put(5, level5);
        }
    }
}
