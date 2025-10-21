package com.outrightwings.bound_for_the_stars.entity;

import com.google.common.collect.Lists;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.dimension.ModDimensions;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import com.outrightwings.bound_for_the_stars.network.F5Packet;
import com.outrightwings.bound_for_the_stars.network.FirstPersonPacket;
import com.outrightwings.bound_for_the_stars.network.ModPackets;
import com.outrightwings.bound_for_the_stars.util.Teleport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Function;

public class Spaceship extends Animal implements GeoEntity, PlayerRideableJumping{
    public Spaceship(EntityType<? extends Animal> type, Level level) {
        super(type, level);

        this.noCulling = true;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, (double)3.0F);
    }
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {return null;}
    //Data
    private static final EntityDataAccessor<Float> PITCH_DATA = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> CURR_SPEED_DATA = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> CURR_TICKS_SPEED_DATA = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Boolean> TAKE_OFF_DATA = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> THRUST_ON_DATA = SynchedEntityData.defineId(Spaceship.class, EntityDataSerializers.BOOLEAN);
    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PITCH_DATA,0f);
        this.entityData.define(CURR_SPEED_DATA,0f);
        this.entityData.define(CURR_TICKS_SPEED_DATA,0L);
        this.entityData.define(TAKE_OFF_DATA,true);
        this.entityData.define(THRUST_ON_DATA,false);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.entityData.set(PITCH_DATA,tag.getFloat("pitch"));
        this.entityData.set(CURR_SPEED_DATA,tag.getFloat("curr_speed"));
        this.entityData.set(CURR_TICKS_SPEED_DATA,tag.getLong("curr_ticks_speed"));
        this.entityData.set(TAKE_OFF_DATA,tag.getBoolean("take_off"));
        this.entityData.set(THRUST_ON_DATA,tag.getBoolean("thrust_on"));
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        tag.putFloat("pitch",this.entityData.get(PITCH_DATA));
        tag.putFloat("curr_speed",this.entityData.get(CURR_SPEED_DATA));
        tag.putFloat("curr_ticks_speed",this.entityData.get(CURR_TICKS_SPEED_DATA));
        tag.putBoolean("take_off",this.entityData.get(TAKE_OFF_DATA));
        tag.putBoolean("thrust_on",this.entityData.get(THRUST_ON_DATA));
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

            if (player instanceof ServerPlayer sp) {
                ModPackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new F5Packet());
            }

            return super.mobInteract(player, hand);
        }

        return super.mobInteract(player, hand);
    }
    protected void playStepSound(BlockPos pos, BlockState block) {}

    private static float turnRate = 3.0f;
    private static float pitchRate = 1.5f;
    private static float maxPitch = 135f;
    private static float minPitch = -0f;
    private static float maxSpeed = 1f;
    private static float ticksToSpeed = 25f;
    private static float ticksToZero = 50f;

    private boolean playerJumping;

    @Override
    public void travel(Vec3 ignored) {
        if (!this.isAlive() || !this.isVehicle()) return;

        LivingEntity rider = getControllingPassenger();
        if (rider == null) return;

        if(level().dimension().equals(ModDimensions.SPACE) ){
            spaceMovement(rider);
        }
        else {
            nonSpaceMovement(rider);
        }
    }
    private void spaceMovement(LivingEntity rider){
        // Yaw
        this.setYRot(this.getYRot() - (rider.xxa * turnRate));
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();

        // Pitch
        this.entityData.set(PITCH_DATA,Mth.clamp(
                this.entityData.get(PITCH_DATA) + rider.zza * pitchRate,
                minPitch, maxPitch
        ) );

        // move
        Vec3 up = getUpVectorFromPitchYaw(this.entityData.get(PITCH_DATA), getYRot());
        float speed, mult;
        long time = level().getGameTime();
        if (this.entityData.get(THRUST_ON_DATA)) {
            mult = Mth.clamp((time - this.entityData.get(CURR_TICKS_SPEED_DATA)) / ticksToSpeed,0,1);
        } else {
            mult = 1f-Mth.clamp((time - this.entityData.get(CURR_TICKS_SPEED_DATA)) / ticksToZero,0,1);
        }
        speed = this.entityData.get(CURR_SPEED_DATA) * mult;
        setDeltaMovement(up.scale(speed));
        this.move(MoverType.SELF, getDeltaMovement());

        if(!level().noCollision(this,this.getBoundingBox().inflate(0.001))){
            causeFallDamage(0,0,null);
        }
    }
    private void nonSpaceMovement(LivingEntity rider){
        double gravity = this.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).getValue();
        // move
        Vec3 up = getUpVectorFromPitchYaw(this.entityData.get(PITCH_DATA), getYRot());
        float speed, mult;
        long time = level().getGameTime();
        if (this.entityData.get(THRUST_ON_DATA)) {
            mult = Mth.clamp((time - this.entityData.get(CURR_TICKS_SPEED_DATA)) / ticksToSpeed,0,1);
        } else {
            mult = 1f-Mth.clamp((time - this.entityData.get(CURR_TICKS_SPEED_DATA)) / ticksToZero,0,1);
        }
        speed = this.entityData.get(CURR_SPEED_DATA) * mult;
        Vec3 motion;
        if(this.entityData.get(THRUST_ON_DATA)){
            motion =  up.scale(speed);
        }
        else {
            //TODO make this nicer feeling. Maybe the player must orient the ship nose up to land? or the ship breaks?
            motion = this.getDeltaMovement().add((double)0.0F, -gravity / (double)4.0F, (double)0.0F);
        }
        setDeltaMovement(motion);
        this.move(MoverType.SELF, getDeltaMovement());
    }
    @Override
    public void tick() {
        super.tick();
        boolean onGround = isEffectivelyOnGround(0.5);
        Vec3 point = getThrusterWorldPos();

        if (!level().isClientSide) {
            Teleport.dimensionCheck(this);
        } else {
            if (!onGround && this.entityData.get(TAKE_OFF_DATA)) {
                spawnTakeoffBurst(point);
                this.entityData.set(TAKE_OFF_DATA, false);
            }
            if (this.entityData.get(THRUST_ON_DATA) && getDeltaMovement().lengthSqr() > 0.1) {
                spawnTrailSmoke(point);
            }
        }
    }

    private boolean isEffectivelyOnGround(double threshold) {
        if(this.entityData.get(PITCH_DATA) == 0){
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
        }
        return false;
    }
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        this.entityData.set(THRUST_ON_DATA,false);
        setDeltaMovement(Vec3.ZERO);
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
        this.entityData.set(THRUST_ON_DATA,!this.entityData.get(THRUST_ON_DATA));
        this.entityData.set(CURR_TICKS_SPEED_DATA,level().getGameTime());

        if(this.entityData.get(THRUST_ON_DATA)){
            this.entityData.set(CURR_SPEED_DATA,maxSpeed * (jumpPower/100f));
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
        return this.entityData.get(PITCH_DATA);
    }
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof LivingEntity entity ? entity : null;
    }
    @Override
    public boolean isControlledByLocalInstance() {
        return this.getControllingPassenger() instanceof Player player && player.isLocalPlayer();
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

        if (entity instanceof ServerPlayer sp) {
            ModPackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new FirstPersonPacket());
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
    protected static final RawAnimation LAUNCH_ANIM = RawAnimation.begin().thenPlay("spaceship.feetup"); //Put feet up
    protected static final RawAnimation CLOSE_ANIM = RawAnimation.begin().thenPlay("spaceship.doorclose");  //Close the doors
    protected static final RawAnimation LAND_ANIM = RawAnimation.begin().thenPlay("spaceship.feetdown");    //put feet down
    protected static final RawAnimation OPEN_ANIM = RawAnimation.begin().thenPlay("spaceship.dooropen");    //put feet down

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"door",1,this::doorAnimController));
        controllerRegistrar.add(new AnimationController<>(this,"feet",1,this::feetAnimController));
    }
    protected <E extends Spaceship> PlayState doorAnimController(final AnimationState<E> event){
        if(!this.isVehicle()){ // player in ship
            return event.setAndContinue(OPEN_ANIM);
        }
        return event.setAndContinue(CLOSE_ANIM);
    }
    protected <E extends Spaceship> PlayState feetAnimController(final AnimationState<E> event){
        if(isEffectivelyOnGround(0.5)){ //On ground
            return event.setAndContinue(LAND_ANIM);
        }
        return event.setAndContinue(LAUNCH_ANIM);
    }
}
