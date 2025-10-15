package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class Alien extends PathfinderMob implements GeoEntity {
    private static final EntityDataAccessor<Boolean> HAS_CAPE = SynchedEntityData.defineId(Alien.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANTENNAS = SynchedEntityData.defineId(Alien.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SKIN_COLOR = SynchedEntityData.defineId(Alien.class, EntityDataSerializers.INT);

    protected Alien(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, (double)10.0F).add(Attributes.MOVEMENT_SPEED, (double)0.23F);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAS_CAPE, false);
        this.entityData.define(ANTENNAS, 0);
        this.entityData.define(SKIN_COLOR, 0);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("cape", hasCape());
        tag.putInt("antenna", antennaCount());
        tag.putInt("color", skinColor());
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("cape")) {
            this.entityData.set(HAS_CAPE, tag.getBoolean("cape"));
        }
        if (tag.contains("antenna")) {
            this.entityData.set(ANTENNAS, tag.getInt("antenna"));
        }
        if (tag.contains("color")) {
            this.entityData.set(SKIN_COLOR, tag.getInt("color"));
        }
    }

    public int antennaCount(){return this.entityData.get(ANTENNAS);}
    public int skinColor(){return this.entityData.get(SKIN_COLOR);}
    public boolean hasCape(){return this.entityData.get(HAS_CAPE);}
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        data = super.finalizeSpawn(level, difficulty, reason, data, tag);

        this.entityData.set(HAS_CAPE, this.random.nextFloat() < 0.1f); // 10% chance
        this.entityData.set(ANTENNAS, this.random.nextInt(4)); // 0–3
        this.entityData.set(SKIN_COLOR, this.random.nextInt(3)); // 0–2, three colors

        if (this.random.nextFloat() < 0.25f) { // 25% chance for sword
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BLASTER.get()));
        }

        return data;
    }

    protected void registerGoals() {
        //this.eatBlockGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0F)); //Walk
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.5F)); //Run
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3,new FollowMobGoal(this,1.25f,2,16));
    }

    //Geckolib
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("alien.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("alien.walk");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("alien.run");
    protected static final RawAnimation IDLE_ANIM2 = RawAnimation.begin().thenLoop("alien.idleGun");
    protected static final RawAnimation WALK_ANIM2 = RawAnimation.begin().thenLoop("alien.walkGun");
    protected static final RawAnimation RUN_ANIM2 = RawAnimation.begin().thenLoop("alien.runGun");
    //protected static final RawAnimation SHOOT_ANIM = RawAnimation.begin().thenLoop("alien.shoot");

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"Idle",3,this::walkAnimController));
    }
    protected <E extends Alien> PlayState walkAnimController(final AnimationState<E> event){
        if(event.isMoving()){
            if(this.getDeltaMovement().length() > .13f){
                return event.setAndContinue(RUN_ANIM2);
            }
            else{
                event.setControllerSpeed(1.2f);
                return event.setAndContinue(WALK_ANIM2);
            }
        }
        return event.setAndContinue(IDLE_ANIM2);
    }
}
