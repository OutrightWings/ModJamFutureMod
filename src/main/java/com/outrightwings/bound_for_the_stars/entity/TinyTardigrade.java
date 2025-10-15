package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class TinyTardigrade extends Cow implements GeoEntity {
    private static final EntityDataAccessor<Integer> SKIN_COLOR = SynchedEntityData.defineId(TinyTardigrade.class, EntityDataSerializers.INT);
    public TinyTardigrade(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SKIN_COLOR, 0);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("color", skinColor());
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("color")) {
            this.entityData.set(SKIN_COLOR, tag.getInt("color"));
        }
    }
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        data = super.finalizeSpawn(level, difficulty, reason, data, tag);

        this.entityData.set(SKIN_COLOR, this.random.nextInt(3)); // 0–2, three colors

        return data;
    }
    public int skinColor(){return this.entityData.get(SKIN_COLOR);}
    //Geckolib
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("tiny_tardigrade.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("tiny_tardigrade.walk");
    //protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("moon_cow.run");

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"Idle",3,this::walkAnimController));
    }
    protected <E extends TinyTardigrade> PlayState walkAnimController(final AnimationState<E> event){
        if(event.isMoving()){

            event.setControllerSpeed(1.2f);
            return event.setAndContinue(WALK_ANIM);
        }
        return event.setAndContinue(IDLE_ANIM);
    }
}
