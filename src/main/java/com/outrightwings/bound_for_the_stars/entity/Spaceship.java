package com.outrightwings.bound_for_the_stars.entity;

import com.google.common.collect.Lists;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class Spaceship extends Animal implements GeoEntity, PlayerRideableJumping{
    public Spaceship(EntityType<? extends Animal> type, Level level) {
        super(type, level);

        this.noCulling = true;
    }
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {return null;}

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

    // Ride
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isVehicle()) {
            player.startRiding(this);

            return super.mobInteract(player, hand);
        }

        return super.mobInteract(player, hand);
    }
    protected void playStepSound(BlockPos pos, BlockState block) {}
    private boolean thrustOn = false;
    private boolean playerJumping;
    private float shipPitch = 0;
    private float turnRate = 3.0f;
    private float pitchRate = 1.5f;
    private float maxPitch = 0f;
    private float minPitch = -135f;
    private float maxSpeed = 1f;
    private float currSpeed = 0f;
    private float ticksToSpeed = 25f;
    private float ticksToZero = 25f;
    private long currTicksSpeed = 0;
    @Override
    public void travel(Vec3 ignored) {
        if (!this.isAlive() || !this.isVehicle()) return;

        LivingEntity rider = (LivingEntity) getControllingPassenger();
        if (rider == null) return;

        // --- yaw tank-controls ---
        this.setYRot(this.getYRot() + (-rider.xxa * turnRate));
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();

        // --- pitch (tilt) control with W/S ---
        this.shipPitch = Mth.clamp(
                this.shipPitch - rider.zza * pitchRate,
                minPitch, maxPitch
        );

        // --- vertical movement toggle ---
        Vec3 up = getUpVectorFromPitchYaw(shipPitch, getYRot());
        float speed, mult;
        long time = Minecraft.getInstance().level.getGameTime();
        if (thrustOn) {
            mult = Mth.clamp((time - currTicksSpeed) / ticksToSpeed,0,1);
        } else {
            mult = 1f-Mth.clamp((time - currTicksSpeed) / ticksToZero,0,1);
        }
        speed = currSpeed * mult;
        setDeltaMovement(up.scale(speed));
        this.move(MoverType.SELF, getDeltaMovement());
    }
    private static Vec3 getUpVectorFromPitchYaw(float pitch, float yaw) {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        float x = Mth.sin(yawRad) * Mth.sin(pitchRad);
        float y =  Mth.cos(pitchRad);
        float z =  -Mth.cos(yawRad) * Mth.sin(pitchRad);
        return new Vec3(x, y, z);
    }
    public void onPlayerJump(int jumpPower) {
        this.thrustOn = !this.thrustOn;
        currTicksSpeed = Minecraft.getInstance().level.getGameTime();
        if(thrustOn){
            currSpeed = maxSpeed * (jumpPower/100f);
        }
    }
    public boolean canJump() {
        return true;
    }
    public void handleStartJump(int jumpPower) {
        this.playerJumping = true;
    }
    public void handleStopJump() {
        this.playerJumping = false;
    }
    public float getShipPitch() {
        return shipPitch;
    }
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof LivingEntity entity ? entity : null;
    }
    public boolean isControlledByLocalInstance() { return true; }
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

    //Drop
    public ItemStack getPickResult() {
        return ModItems.SPACESHIP_ITEM.get().getDefaultInstance();
    }

    //Geo
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("spaceship.idle");

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"Idle",1,this::idleAnimController));
    }
    protected <E extends Spaceship> PlayState idleAnimController(final AnimationState<E> event){
        return event.setAndContinue(IDLE_ANIM);
    }
}
