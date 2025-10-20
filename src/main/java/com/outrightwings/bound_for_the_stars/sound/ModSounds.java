package com.outrightwings.bound_for_the_stars.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

    public static final RegistryObject<SoundEvent> BLASTER_PEW = register("blaster_pew");

    public static final RegistryObject<SoundEvent> GIANT_TARDIGRADE_HURT = register("giant_tardigrade_hurt");
    public static final RegistryObject<SoundEvent> GIANT_TARDIGRADE_IDLE = register("giant_tardigrade_idle");
    public static final RegistryObject<SoundEvent> GIANT_TARDIGRADE_SPAWN = register("giant_tardigrade_spawn");
    public static final RegistryObject<SoundEvent> TINY_TARDIGRADE_HURT = register("tiny_tardigrade_hurt");
    public static final RegistryObject<SoundEvent> TINY_TARDIGRADE_IDLE = register("tiny_tardigrade_idle");
    public static final RegistryObject<SoundEvent> TINY_TARDIGRADE_ATTACK = register("tiny_tardigrade_attack");;

    public static final RegistryObject<SoundEvent> TARDIGRADE_DIE = register("tardigrade_die");

    private static RegistryObject<SoundEvent> register(String name){
        return SOUNDS.register(name,()-> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, name)));
    }
}
