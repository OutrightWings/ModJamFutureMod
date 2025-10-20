package com.outrightwings.bound_for_the_stars.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

public class ModDimensions {
    public static final ResourceKey<Level> MOON = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(MODID,"the_moon"));
    public static final ResourceKey<Level> SPACE = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(MODID,"space"));
}
