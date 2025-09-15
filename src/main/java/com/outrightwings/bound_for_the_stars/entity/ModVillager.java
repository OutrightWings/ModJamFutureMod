package com.outrightwings.bound_for_the_stars.entity;

import com.google.common.collect.ImmutableSet;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Predicate;

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

}
