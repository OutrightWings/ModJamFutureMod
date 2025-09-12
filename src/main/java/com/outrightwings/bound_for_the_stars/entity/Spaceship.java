package com.outrightwings.bound_for_the_stars.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Spaceship extends Entity implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Spaceship(EntityType<? extends Entity> type, Level level) {
        super(type, level);
    }

    //Tags
    @Override
    protected void defineSynchedData() {

    }
    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }
    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    //Gecko
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
}
