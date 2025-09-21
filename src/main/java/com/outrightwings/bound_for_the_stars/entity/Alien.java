package com.outrightwings.bound_for_the_stars.entity;

import com.outrightwings.bound_for_the_stars.Main;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Alien extends PathfinderMob implements GeoEntity {
    protected Alien(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, (double)10.0F).add(Attributes.MOVEMENT_SPEED, (double)0.23F);
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
    protected static final RawAnimation IDLE_ANIM2 = RawAnimation.begin().thenLoop("alien.idle2");
    protected static final RawAnimation WALK_ANIM2 = RawAnimation.begin().thenLoop("alien.walk2");
    protected static final RawAnimation RUN_ANIM2 = RawAnimation.begin().thenLoop("alien.run2");
    protected static final RawAnimation SHOOT_ANIM = RawAnimation.begin().thenLoop("alien.shoot");

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
        return event.setAndContinue(IDLE_ANIM);
    }
}
