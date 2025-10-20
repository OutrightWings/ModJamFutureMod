package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.entity.goals.BlasterAttackGoal;
import com.outrightwings.bound_for_the_stars.item.Blaster;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import com.outrightwings.bound_for_the_stars.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class Alien extends PathfinderMob implements GeoEntity, RangedAttackMob {
    private static final EntityDataAccessor<Boolean> HAS_CAPE = SynchedEntityData.defineId(Alien.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANTENNAS = SynchedEntityData.defineId(Alien.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SKIN_COLOR = SynchedEntityData.defineId(Alien.class, EntityDataSerializers.INT);
    private final BlasterAttackGoal<Alien> BLASTER_GOAL = new BlasterAttackGoal<>(this, 1.0F, 20, 15.0F);
    private final AvoidEntityGoal<Player> FLEE_GOAL = new AvoidEntityGoal<>(this, Player.class, 16.0F, 2, 2);

    private boolean isAngry = false;
    private boolean isFlee = false;
    private int angerTime = 0;
    private static final int ANGER_DURATION = 200; // 10 seconds (20 ticks/sec)


    protected Alien(EntityType<? extends Alien> type, Level level) {
        super(type, level);
        reassessWeaponGoal();
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, (double)10.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.16F)
                .add(Attributes.ATTACK_DAMAGE,1);
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
        reassessWeaponGoal();
    }

    public int antennaCount(){return this.entityData.get(ANTENNAS);}
    public int skinColor(){return this.entityData.get(SKIN_COLOR);}
    public boolean hasCape(){return this.entityData.get(HAS_CAPE);}
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        data = super.finalizeSpawn(level, difficulty, reason, data, tag);

        this.entityData.set(HAS_CAPE, this.random.nextFloat() < 0.1f); // 10% chance

        int antenna = this.random.nextInt(100);
        if(antenna < 50){
            this.entityData.set(ANTENNAS,0); // 0–3
        }
        else if(antenna < 75){
            this.entityData.set(ANTENNAS, 1);
        }
        else if(antenna < 90){
            this.entityData.set(ANTENNAS, 2);
        }
        else {
            this.entityData.set(ANTENNAS, 3);
        }


        int color = this.random.nextInt(100);
        if(color < 85){
            this.entityData.set(SKIN_COLOR, 0);
        }
        else if(color < 95){
            this.entityData.set(SKIN_COLOR, 1);
        }
        else{
            this.entityData.set(SKIN_COLOR, 2);
        }

        if (this.random.nextFloat() < 0.25f) { // 25%
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BLASTER.get()));
        }
        reassessWeaponGoal();
        return data;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0F)); //Walk
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.75)); //Run
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10,new FollowMobGoal(this,1.25f,3,16));
        this.targetSelector.addGoal(5, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngry));
    }

    public boolean isAngry(LivingEntity entity) {
        return isAngry;
    }
    public boolean isFlee(LivingEntity entity) {
        return isFlee;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (!this.level().isClientSide && source.getEntity() instanceof Player) {
            this.becomeAngryAt(source.getEntity());
        }
        return result;
    }
    private void fleeFrom(LivingEntity target) {
        this.goalSelector.addGoal(1,FLEE_GOAL);
    }
    private void becomeAngryAt(Entity target) {

        List<? extends Alien> nearby = this.level().getEntitiesOfClass(
                this.getClass(),
                this.getBoundingBox().inflate(16.0D)
        );

        for (Alien mob : nearby) {
            if (mob.holdingGun()) {
                mob.isAngry = true;
                mob.angerTime = ANGER_DURATION;
                mob.setTarget((LivingEntity) target);
            } else {
                mob.isFlee = true;
                mob.angerTime = ANGER_DURATION;
                mob.fleeFrom((LivingEntity) target);
            }
        }
    }
    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isAngry||this.isFlee) {
            // If still has a target, refresh timer
            if (this.getTarget() != null && this.getTarget().isAlive()) {
                this.angerTime = ANGER_DURATION;
            } else {
                // Countdown
                if (this.angerTime > 0) {
                    this.angerTime--;
                } else {
                    // Time's up, calm down
                    this.isAngry = false;
                    this.isFlee = false;
                    this.goalSelector.removeGoal(this.FLEE_GOAL);
                    this.setTarget(null);
                }
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.level().isClientSide) return;
        Blaster.BlasterProjectile proj = Blaster.BlasterProjectile.spawnAtPlayer(this, this.level());
        level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                ModSounds.BLASTER_PEW.get(),
                this.getSoundSource(),
                1.0F, // volume
                1F  // pitch
        );
        double dx = target.getX() - this.getX();
        double dy = target.getEyeY() - proj.getY();
        double dz = target.getZ() - this.getZ();
        float speed = 1.25F;
        float inaccuracy = 0F;
        proj.shoot(dx, dy, dz, speed, inaccuracy);

        this.level().addFreshEntity(proj);

        this.level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                net.minecraft.sounds.SoundEvents.BLAZE_SHOOT,
                this.getSoundSource(),
                1.0F,
                2F
        );
    }
    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.BLASTER_GOAL);
            this.goalSelector.removeGoal(this.FLEE_GOAL);
            if (this.holdingGun()) {
                int i = 20;
                if (this.level().getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.BLASTER_GOAL.setMinAttackInterval(i);
                this.goalSelector.addGoal(1, this.BLASTER_GOAL);
            } else {
                //this.goalSelector.addGoal(1, this.FLEE_GOAL);
            }
        }

    }
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        super.setItemSlot(slot, stack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }

    }
    private boolean holdingGun(){
        return !this.getMainHandItem().is(Blocks.AIR.asItem());
    }
    //Geckolib
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("alien.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("alien.walk");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("alien.run");
    protected static final RawAnimation IDLE_ANIM2 = RawAnimation.begin().thenLoop("alien.idleGun");
    protected static final RawAnimation WALK_ANIM2 = RawAnimation.begin().thenLoop("alien.walkGun");
    protected static final RawAnimation RUN_ANIM2 = RawAnimation.begin().thenLoop("alien.runGun");

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"Idle",3,this::walkAnimController));
    }
    protected <E extends Alien> PlayState walkAnimController(final AnimationState<E> event){
        if(event.isMoving()){
            if(this.getDeltaMovement().length() > .10f){
                return event.setAndContinue(holdingGun()&&isAggressive() ? RUN_ANIM2 : RUN_ANIM);
            }
            else{
                event.setControllerSpeed(2f);
                return event.setAndContinue(holdingGun()&&isAggressive() ? WALK_ANIM2 : WALK_ANIM);
            }
        }
        return event.setAndContinue(holdingGun()&&isAggressive() ? IDLE_ANIM2 : IDLE_ANIM);
    }
}
