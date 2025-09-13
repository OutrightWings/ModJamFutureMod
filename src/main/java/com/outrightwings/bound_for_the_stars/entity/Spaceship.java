package com.outrightwings.bound_for_the_stars.entity;

import com.google.common.collect.Lists;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class Spaceship extends Entity implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.FLOAT);

    public Spaceship(EntityType<? extends Entity> type, Level level) {
        super(type, level);
    }

    //Collide
    public boolean canCollideWith(Entity entity) {
        return canVehicleCollide(this, entity);
    }
    public static boolean canVehicleCollide(Entity entityA, Entity entityB) {
        return (entityB.canBeCollidedWith() || entityB.isPushable()) && !entityA.isPassengerOfSameVehicle(entityB);
    }
    public boolean canBeCollidedWith() {
        return true;
    }
    public boolean isPushable() {
        return false;
    }

    //Breakable
    public boolean hurt(DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else if (!this.level().isClientSide && !this.isRemoved()) {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.setDamage(this.getDamage() + amount * 10.0F);
            this.markHurt();
            this.gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
            boolean flag = damageSource.getEntity() instanceof Player && ((Player)damageSource.getEntity()).getAbilities().instabuild;
            if (flag || this.getDamage() > 40.0F) {
                if (!flag && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.destroy(damageSource);
                }

                this.discard();
            }

            return true;
        } else {
            return true;
        }
    }
    public void setDamage(float p_38312_) {
        this.entityData.set(DATA_ID_DAMAGE, p_38312_);
    }
    public float getDamage() {
        return (Float)this.entityData.get(DATA_ID_DAMAGE);
    }
    public void setHurtTime(int p_38355_) {
        this.entityData.set(DATA_ID_HURT, p_38355_);
    }
    public int getHurtTime() {
        return (Integer)this.entityData.get(DATA_ID_HURT);
    }
    public void setHurtDir(int p_38363_) {
        this.entityData.set(DATA_ID_HURTDIR, p_38363_);
    }
    public int getHurtDir() {
        return (Integer)this.entityData.get(DATA_ID_HURTDIR);
    }
    protected void destroy(DamageSource damageSource) {
        this.spawnAtLocation(ModItems.SPACESHIP_ITEM.get());
    }
    public ItemStack getPickResult() {
        return ModItems.SPACESHIP_ITEM.get().getDefaultInstance();
    }

    //Rideable
    public boolean isPickable(){ return !this.isRemoved(); }
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else {
            if (!this.level().isClientSide) {
                //TODO make player camera go f5?
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }
    protected boolean canAddPassenger(Entity entity) {
        return this.getPassengers().isEmpty();
    }
    protected void positionRider(Entity entity, MoveFunction moveFunction) {
        //TODO Make passenger rotate when they enter ship
        if (this.hasPassenger(entity)) {
            float f1 = (float)((this.isRemoved() ? (double)0.01F : this.getPassengersRidingOffset()) + entity.getMyRidingOffset());

            Vec3 vec3 = (Vec3.ZERO.yRot(-this.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F)));
            moveFunction.accept(entity, this.getX() + vec3.x, this.getY() + (double)f1, this.getZ() + vec3.z);

        }

    }
    public double getPassengersRidingOffset() {
        return 1.25f;
    }
    public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
        AABB bounds = this.getBoundingBox();
        double baseY = bounds.minY;

        Vec3 escape = this.getCollisionHorizontalEscapeVector(
                (double)(this.getBbWidth() * Mth.SQRT_OF_TWO),
                (double)(entity.getBbWidth() * Mth.SQRT_OF_TWO),
                entity.getYRot()
        );
        double centerX = this.getX() + escape.x;
        double centerZ = this.getZ() + escape.z;

        List<Vec3> candidates = Lists.newArrayList();

        double[] steps = new double[] {0.0, 0.5, -0.5, 1.0, -1.0};
        for (double sx : steps) {
            for (double sz : steps) {
                double tryX = centerX + sx;
                double tryZ = centerZ + sz;

                BlockPos pos = BlockPos.containing(tryX, baseY, tryZ);
                // first try the block at the same Y
                double floor = this.level().getBlockFloorHeight(pos);
                if (DismountHelper.isBlockFloorValid(floor)) {
                    candidates.add(new Vec3(tryX, (double)pos.getY() + floor, tryZ));
                    continue;
                }
                // then try the block below (stairs / half-slab / etc.)
                BlockPos below = pos.below();
                double floorBelow = this.level().getBlockFloorHeight(below);
                if (DismountHelper.isBlockFloorValid(floorBelow)) {
                    candidates.add(new Vec3(tryX, (double)below.getY() + floorBelow, tryZ));
                }
            }
        }

        for (Pose pose : entity.getDismountPoses()) {
            for (Vec3 candidate : candidates) {
                if (DismountHelper.canDismountTo(this.level(), candidate, entity, pose)) {
                    entity.setPose(pose);
                    return candidate;
                }
            }
        }

        return super.getDismountLocationForPassenger(entity);
    }

    //Tags
    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ID_HURT, 0);
        this.entityData.define(DATA_ID_HURTDIR, 1);
        this.entityData.define(DATA_ID_DAMAGE, 0.0F);
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
