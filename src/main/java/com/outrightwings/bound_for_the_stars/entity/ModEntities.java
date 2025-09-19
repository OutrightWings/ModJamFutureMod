package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


import static com.outrightwings.bound_for_the_stars.Main.MODID;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<Spaceship>> SPACESHIP_ENTITY = ENTITY_TYPES.register("spaceship",()->
            EntityType.Builder.of(Spaceship::new,  MobCategory.MISC)
                    .sized(1.75f,3.625f) //28px wide 58px tall
                    .fireImmune()
                    .build(new ResourceLocation(MODID, "spaceship").toString())
            );

    public static final ResourceKey<Level> MOON = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MODID,"the_moon"));

}
