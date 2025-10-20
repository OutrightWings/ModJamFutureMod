package com.outrightwings.bound_for_the_stars.entity;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class Spawning {
    public static void registerSpawnPlacements() {
        SpawnPlacements.register(ModEntities.ALIEN_ENTITY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                Alien::checkMobSpawnRules);

        SpawnPlacements.register(ModEntities.MOON_COW_ENTITY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                MoonCow::checkMobSpawnRules);

        SpawnPlacements.register(ModEntities.TINY_TARDIGRADE_ENTITY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING,
                TinyTardigrade::checkMobSpawnRules);

        SpawnPlacements.register(ModEntities.GIANT_TARDIGRADE_ENTITY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING,
                GiantTardigrade::checkMobSpawnRules);
    }

}
