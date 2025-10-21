package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class MoonCow extends Cow implements GeoEntity {
    public MoonCow(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }
    @Nullable
    public MoonCow getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return ModEntities.MOON_COW_ENTITY.get().create(level);
    }
    public boolean isFood(ItemStack p_27600_) {
        return p_27600_.is(ModItems.MOON_MELON_SLICE.get());
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, (double)2.0F));
        this.goalSelector.addGoal(2, new BreedGoal(this, (double)1.0F));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.25F, Ingredient.of(ModItems.MOON_MELON_SLICE.get()), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, (double)1.25F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }
    public static boolean checkMobSpawnRules(EntityType<? extends Mob> mob, LevelAccessor levelAccessor, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return !levelAccessor.getBlockState(pos.below()).isAir() && Mob.checkMobSpawnRules(mob,levelAccessor,type,pos,rand);
    }
    //Geckolib
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("moon_cow.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("moon_cow.walk");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("moon_cow.run");

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"Idle",3,this::walkAnimController));
    }
    protected <E extends MoonCow> PlayState walkAnimController(final AnimationState<E> event){
        if(event.isMoving()){
            if(this.getDeltaMovement().length() > .13f){
                return event.setAndContinue(RUN_ANIM);
            }
            else{
                event.setControllerSpeed(1.2f);
                return event.setAndContinue(WALK_ANIM);
            }
        }
        return event.setAndContinue(IDLE_ANIM);
    }
}
