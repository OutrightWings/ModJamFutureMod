package com.outrightwings.bound_for_the_stars.entity.goals;

import com.outrightwings.bound_for_the_stars.item.Blaster;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;

import java.util.EnumSet;

public class BlasterAttackGoal<T extends Mob & RangedAttackMob> extends Goal {
    private final T mob;
    private final double speedModifier;
    private int attackIntervalMin;
    private final float attackRadiusSqr;
    private int attackTime;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime;

    public BlasterAttackGoal(T mob, double speed, int interval, float radius) {
        this.attackTime = -1;
        this.strafingTime = -1;
        this.mob = mob;
        this.speedModifier = speed;
        this.attackIntervalMin = interval;
        this.attackRadiusSqr = radius * radius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public void setMinAttackInterval(int interval) {
        this.attackIntervalMin = interval;
    }

    public boolean canUse() {
        return this.mob.getTarget() == null ? false : this.isHoldingBlaster();
    }

    protected boolean isHoldingBlaster() {
        return this.mob.isHolding((is) -> is.getItem() instanceof Blaster);
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingBlaster();
    }

    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) return;

        boolean canSee = this.mob.getSensing().hasLineOfSight(target);
        if (canSee) seeTime++;
        else seeTime = 0;

        double distSqr = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());

        // Move towards target if too far
        if (distSqr > this.attackRadiusSqr) {
            this.mob.getNavigation().moveTo(target, speedModifier);
        } else {
            this.mob.getNavigation().stop();
        }

        // Fire if visible and cooldown ready
        if (seeTime > 20 && --attackTime <= 0 && this.isHoldingBlaster()) {
            this.mob.performRangedAttack(target, 1.0f);
            attackTime = attackIntervalMin;
        }

        // Always look at target
        this.mob.getLookControl().setLookAt(target, 30f, 30f);
    }

}