package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.entity.goals.SpawnAttackGoal;
import com.outrightwings.bound_for_the_stars.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GiantTardigrade  extends PathfinderMob implements GeoEntity, RangedAttackMob {
    protected GiantTardigrade(EntityType<? extends GiantTardigrade> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.2F).add(Attributes.MAX_HEALTH, (double)20);
    }
    public static boolean checkMobSpawnRules(EntityType<? extends Mob> mob, LevelAccessor levelAccessor, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return !levelAccessor.getBlockState(pos.below()).isAir() && Mob.checkMobSpawnRules(mob,levelAccessor,type,pos,rand);
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.5, 1.5));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(3,new SpawnAttackGoal<>(this,ModEntities.TINY_TARDIGRADE_ENTITY.get(),5,50, 15.0F));
    }
    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        triggerAnim("attack", "attack");
        level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                ModSounds.GIANT_TARDIGRADE_SPAWN.get(),
                this.getSoundSource(),
                1.0F, // volume
                1F  // pitch
        );
        int radius = 4;
        BlockPos center = this.getOnPos();
        // Try up to 10 random positions to find a valid air spot
        for (int i = 0; i < 10; i++) {
            int dx = level().getRandom().nextInt(radius * 2 + 1) - radius;
            int dz = level().getRandom().nextInt(radius * 2 + 1) - radius;

            BlockPos pos = center.offset(dx, 0, dz);
            pos = findAirAbove(pos, level()); // Make sure mob spawns in air

            if (pos != null) {
                Mob mob = ModEntities.TINY_TARDIGRADE_ENTITY.get().create(level());
                if (mob != null) {
                    mob.moveTo(pos, level().getRandom().nextFloat() * 360F, 0F);

                    // Run vanilla spawn finalization (difficulty scaling, equipment, etc.)
                    level().addFreshEntity(mob);
                    return;
                }
            }
        }
    }
    private static BlockPos findAirAbove(BlockPos start, Level level) {
        BlockPos pos = start;
        for (int i = 0; i < 10; i++) { // search up to 10 blocks upward
            if (level.getBlockState(pos).isAir() &&
                    level.getBlockState(pos.above()).isAir()) {
                return pos;
            }
            pos = pos.above();
        }
        return null; // No air pocket found
    }

    //Geckolib
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("giant_tardigrade.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("giant_tardigrade.run");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("giant_tardigrade.spawn");

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"walk",3,this::walkAnimController));
        controllerRegistrar.add(new AnimationController<>(this, "attack", state -> PlayState.STOP)
                .triggerableAnim("attack", ATTACK_ANIM));
    }
    protected <E extends GiantTardigrade> PlayState walkAnimController(final AnimationState<E> event){
        if(event.isMoving()){
            //event.setControllerSpeed(1.75f);
            return event.setAndContinue(WALK_ANIM);
        }
        return event.setAndContinue(IDLE_ANIM);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource p_21239_) {
        return ModSounds.GIANT_TARDIGRADE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSounds.TARDIGRADE_DIE.get();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.GIANT_TARDIGRADE_IDLE.get();
    }
}
