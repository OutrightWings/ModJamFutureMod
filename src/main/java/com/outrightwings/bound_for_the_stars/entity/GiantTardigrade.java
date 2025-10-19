package com.outrightwings.bound_for_the_stars.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GiantTardigrade  extends PathfinderMob implements GeoEntity {
    protected GiantTardigrade(EntityType<? extends GiantTardigrade> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, (double)10.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.13F)
                .add(Attributes.ATTACK_DAMAGE,1);
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
}
