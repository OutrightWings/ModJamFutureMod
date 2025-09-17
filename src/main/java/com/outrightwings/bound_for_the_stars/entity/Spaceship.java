package com.outrightwings.bound_for_the_stars.entity;

import com.google.common.collect.Lists;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
    //Data
    private static final EntityDataAccessor<Float> PITCH_DATA = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> CURR_SPEED_DATA = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> CURR_TICKS_SPEED_DATA = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Boolean> TAKE_OFF_DATA = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.BOOLEAN);
    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PITCH_DATA,0f);
        this.entityData.define(CURR_SPEED_DATA,0f);
        this.entityData.define(CURR_TICKS_SPEED_DATA,0L);
        this.entityData.define(TAKE_OFF_DATA,true);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        shipPitch = tag.getFloat("pitch");
        currSpeed = tag.getFloat("curr_speed");
        currTicksSpeed = tag.getLong("curr_ticks_speed");
        takeOff = tag.getBoolean("take_off");
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        tag.putFloat("pitch",shipPitch);
        tag.putFloat("curr_speed",currSpeed);
        tag.putFloat("curr_ticks_speed",currTicksSpeed);
        tag.putBoolean("take_off",takeOff);
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

    // Ride
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isVehicle()) {
            player.startRiding(this);

            return super.mobInteract(player, hand);
        }

        return super.mobInteract(player, hand);
    }
    protected void playStepSound(BlockPos pos, BlockState block) {}

    private static float turnRate = 3.0f;
    private static float pitchRate = 1.5f;
    private static float maxPitch = 0f;
    private static float minPitch = -135f;
    private static float maxSpeed = 1f;
    private static float ticksToSpeed = 25f;
    private static float ticksToZero = 50f;

    private float currSpeed = 0f;
    private long currTicksSpeed = 0;
    private boolean takeOff = true;
    private boolean thrustOn = false;
    private boolean playerJumping;
    private float shipPitch = 0;

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
    @Override
    public void tick(){
        super.tick();
        if(!level().isClientSide) return;

        //Take off particles
        boolean onGround = isEffectivelyOnGround(0.5);
        Vec3 point = getThrusterWorldPos();
        if(!onGround && !takeOff){ //Have just left the ground
            takeOff = true;
            spawnTakeoffBurst(point);
        }
        if(onGround){ //Back on the ground
            takeOff = false;
        }

        //Movement particles
        if(getDeltaMovement().lengthSqr() > 0.1){
            spawnTrailSmoke(point);
        }
    }
    private boolean isEffectivelyOnGround(double threshold) {
        AABB bb = this.getBoundingBox();
        double minY = bb.minY;

        int minX = Mth.floor(bb.minX);
        int maxX = Mth.floor(bb.maxX);
        int minZ = Mth.floor(bb.minZ);
        int maxZ = Mth.floor(bb.maxZ);

        // base block y directly under bounding box
        int baseY = Mth.floor(minY - 0.001);

        int steps = Math.max(1, (int) Math.ceil(threshold)); // how many whole-block levels to sample
        for (int dy = 0; dy <= steps; dy++) {
            int checkY = baseY - dy;
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, checkY, z);
                    BlockState state = level().getBlockState(pos);

                    // skip air
                    if (state.isAir()) continue;

                    // approx top-of-block as pos.getY() + 1.0 (good enough for most blocks)
                    double blockTop = pos.getY() + 1.0;

                    // if the distance from bounding-box bottom to block top is <= threshold -> on ground
                    if (minY - blockTop <= threshold) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Vec3 getThrusterWorldPos() {
        Vec3 localOffset = new Vec3(0, -1, 0);

        double yawRad   = Math.toRadians(getYRot());
        double pitchRad = Math.toRadians(-getShipPitch());

        Vec3 rotated = localOffset
                .xRot((float) -pitchRad)   // pitch about X
                .yRot((float) -yawRad);    // yaw about Y

        double pivotY = getBoundingBox().minY + 1;
        Vec3 pivotPos = new Vec3(getX(), pivotY, getZ());

        return pivotPos.add(rotated);
    }
    private void spawnTakeoffBurst(Vec3 basePos) {
        for (int i = 0; i < 80; i++) {
            double dx = (random.nextDouble() - 0.5) * 2.0;
            double dy = 0.1 + random.nextDouble() * 0.5;
            double dz = (random.nextDouble() - 0.5) * 2.0;
            level().addParticle(ParticleTypes.CLOUD,
                    basePos.x, basePos.y, basePos.z,
                    dx * 0.3, dy, dz * 0.3);
        }
    }
    private void spawnTrailSmoke(Vec3 basePos) {
        double spread = 0.3;
        level().addParticle(ParticleTypes.SMOKE,
                basePos.x + (random.nextDouble() - 0.5) * spread,
                basePos.y,
                basePos.z + (random.nextDouble() - 0.5) * spread,
                0.0, 0.02, 0.0);
        level().addParticle(ParticleTypes.FLAME,
                basePos.x + (random.nextDouble() - 0.5) * spread,
                basePos.y,
                basePos.z + (random.nextDouble() - 0.5) * spread,
                0.0, 0.02, 0.0);
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
    protected static final RawAnimation LAUNCH_ANIM = RawAnimation.begin().thenLoop("spaceship.launch"); //Put feet up
    protected static final RawAnimation CLOSE_ANIM = RawAnimation.begin().thenLoop("spaceship.close");  //Close the doors
    protected static final RawAnimation LAND_ANIM = RawAnimation.begin().thenLoop("spaceship.land");    //put feet down
    //protected static final RawAnimation OPEN_ANIM = RawAnimation.begin().thenLoop("spaceship.open");    //put feet down

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //controllerRegistrar.add(new AnimationController<>(this,"Idle",1,this::idleAnimController));
    }
    protected <E extends Spaceship> PlayState idleAnimController(final AnimationState<E> event){
        return event.setAndContinue(LAND_ANIM);
    }
}
