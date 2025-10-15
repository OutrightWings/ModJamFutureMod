package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.item.Blaster;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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
                    .sized(28/16f,58/16f) //28px wide 58px tall
                    .fireImmune()
                    .build(ResourceLocation.fromNamespaceAndPath(MODID, "spaceship").toString())
            );

    public static final RegistryObject<EntityType<Alien>> ALIEN_ENTITY = ENTITY_TYPES.register("alien",()->
            EntityType.Builder.of(Alien::new,  MobCategory.CREATURE)
                    .sized(8/16f,24/16f)
                    .build(ResourceLocation.fromNamespaceAndPath(MODID, "alien").toString())
    );
    public static final RegistryObject<EntityType<MoonCow>> MOON_COW_ENTITY = ENTITY_TYPES.register("moon_cow",()->
            EntityType.Builder.of(MoonCow::new,  MobCategory.CREATURE)
                    .sized(19/16f,25/16f)
                    .build(ResourceLocation.fromNamespaceAndPath(MODID, "moon_cow").toString())
    );
    public static final RegistryObject<EntityType<TinyTardigrade>> TINY_TARDIGRADE_ENTITY = ENTITY_TYPES.register("tiny_tradigrade",()->
            EntityType.Builder.of(TinyTardigrade::new,  MobCategory.CREATURE)
                    .sized(11/16f,5/16f)
                    .build(ResourceLocation.fromNamespaceAndPath(MODID, "tiny_tardigrade").toString())
    );
    public static final RegistryObject<EntityType<Blaster.BlasterProjectile>> BLASTER_PROJECTILE = ENTITY_TYPES.register("blaster_projectile",()->
            EntityType.Builder.of(Blaster.BlasterProjectile::new,  MobCategory.MISC)
                    .sized(4/16f,2/16f)
                    .build(ResourceLocation.fromNamespaceAndPath(MODID, "blaster_projectile").toString())
    );

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SPACESHIP_ENTITY.get(), Spaceship.createAttributes().build());
        event.put(ModEntities.ALIEN_ENTITY.get(),Alien.createAttributes().build());
        event.put(ModEntities.MOON_COW_ENTITY.get(),MoonCow.createAttributes().build());
        event.put(ModEntities.TINY_TARDIGRADE_ENTITY.get(),TinyTardigrade.createAttributes().build());
    }
}
