package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.Main;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


import static com.outrightwings.bound_for_the_stars.Main.MODID;
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<Spaceship>> SPACESHIP_ENTITY = ENTITY_TYPES.register("spaceship",()->
            EntityType.Builder.of(Spaceship::new,  MobCategory.MISC)
                    .sized(1.75f,3.625f) //28px wide 58px tall
                    .fireImmune()
                    .build(ResourceLocation.fromNamespaceAndPath(MODID, "spaceship").toString())
            );

    public static final RegistryObject<EntityType<Alien>> ALIEN_ENTITY = ENTITY_TYPES.register("alien",()->
            EntityType.Builder.of(Alien::new,  MobCategory.CREATURE)
                    .sized(.5f,1.5f) //8px wide 24px tall
                    .fireImmune()
                    .build(ResourceLocation.fromNamespaceAndPath(MODID, "alien").toString())
    );

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SPACESHIP_ENTITY.get(), Spaceship.createAttributes().build());
        event.put(ModEntities.ALIEN_ENTITY.get(),Alien.createAttributes().build());
    }
}
