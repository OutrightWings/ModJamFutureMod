package com.outrightwings.bound_for_the_stars.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Alien extends Mob implements GeoEntity {
    protected Alien(EntityType<? extends Mob> type, Level level) {
        super(type, level);
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
        controllerRegistrar.add(new AnimationController<>(this,"Idle",1,this::idleAnimController));
    }
    protected <E extends Alien> PlayState idleAnimController(final AnimationState<E> event){
        return event.setAndContinue(IDLE_ANIM);
    }
}
