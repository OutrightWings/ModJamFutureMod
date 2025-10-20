package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class TinyTardigrade extends PathfinderMob implements GeoEntity {
    private static final EntityDataAccessor<Integer> SKIN_COLOR = SynchedEntityData.defineId(TinyTardigrade.class, EntityDataSerializers.INT);
    public TinyTardigrade(EntityType<? extends TinyTardigrade> entityType, Level level) {
        super(entityType, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.2F).add(Attributes.MAX_HEALTH, (double)5);
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

        int color = this.random.nextInt(100);
        if(color < 80){
            this.entityData.set(SKIN_COLOR, 0);
        }
        else if(color < 98){
            this.entityData.set(SKIN_COLOR, 2);
        }
        else{
            this.entityData.set(SKIN_COLOR, 1);
        }

        return data;
    }
    public int skinColor(){return this.entityData.get(SKIN_COLOR);}

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
            public void stop() {
                super.stop();
                TinyTardigrade.this.setAggressive(false);
            }

            public void start() {
                super.start();
                TinyTardigrade.this.setAggressive(true);
            }
        });
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }
    public boolean doHurtTarget(Entity entity){
        triggerAnim("attack", "attack");
        level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                ModSounds.TINY_TARDIGRADE_ATTACK.get(),
                this.getSoundSource(),
                1.0F, // volume
                1F  // pitch
        );
        return super.doHurtTarget(entity);
    }


    //Geckolib
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("tiny_tardigrade.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("tiny_tardigrade.walk");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("tiny_tardigrade.attack");

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"walk",3,this::walkAnimController));
        controllerRegistrar.add(new AnimationController<>(this, "attack", state -> PlayState.STOP)
        .triggerableAnim("attack", ATTACK_ANIM));
    }
    protected <E extends TinyTardigrade> PlayState walkAnimController(final AnimationState<E> event){
        if(event.isMoving()){
            event.setControllerSpeed(1.75f);
            return event.setAndContinue(WALK_ANIM);
        }
        return event.setAndContinue(IDLE_ANIM);
    }

    @Override
    protected @org.jetbrains.annotations.Nullable SoundEvent getHurtSound(DamageSource p_21239_) {
        return ModSounds.TINY_TARDIGRADE_HURT.get();
    }

    @Override
    protected @org.jetbrains.annotations.Nullable SoundEvent getDeathSound() {
        return ModSounds.TARDIGRADE_DIE.get();
    }

    @Override
    protected @org.jetbrains.annotations.Nullable SoundEvent getAmbientSound() {
        return ModSounds.TINY_TARDIGRADE_IDLE.get();
    }
}
