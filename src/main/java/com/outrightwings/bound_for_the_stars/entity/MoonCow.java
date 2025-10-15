package com.outrightwings.bound_for_the_stars.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
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
