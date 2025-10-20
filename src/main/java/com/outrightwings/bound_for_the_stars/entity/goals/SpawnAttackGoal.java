package com.outrightwings.bound_for_the_stars.entity.goals;

import com.outrightwings.bound_for_the_stars.entity.GiantTardigrade;
import com.outrightwings.bound_for_the_stars.entity.TinyTardigrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpawnAttackGoal<T extends Mob  & RangedAttackMob> extends Goal {
    private final T mob;
    private final  EntityType<?> type;
    private final int maxSpawns;
    private final float radius;
    private final int attackIntervalMin;
    private int attackTime;
    private int seeTime;

    public SpawnAttackGoal(T mob, EntityType<?> type, int maxSpawns, int interval, float radius){
        this.mob = mob;
        this.maxSpawns = maxSpawns;
        this.attackTime = -1;
        this.attackIntervalMin = interval;
        this.radius = radius ;
        this.type = type;
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

    @Override
    public boolean canUse() {
        BlockPos center = mob.getOnPos();
        Level level = mob.level();
        AABB area = new AABB(
                center.getX() - radius, center.getY() - radius, center.getZ() - radius,
                center.getX() + radius, center.getY() + radius, center.getZ() + radius
        );

        // Get all entities of this type inside the area
        List<? extends Entity> entities = level.getEntitiesOfClass(type.getBaseClass(), area,
                (e) -> e.distanceToSqr(center.getX(), center.getY(), center.getZ()) <= radius * radius);

        // Return true if fewer than maxSpawns are present
        return entities.size() < maxSpawns;
    }
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) return;

        boolean canSee = this.mob.getSensing().hasLineOfSight(target);
        if (canSee) seeTime++;
        else seeTime = 0;


        // Fire if visible and cooldown ready
        if (seeTime > 20 && --attackTime <= 0 && canUse()) {
            this.mob.performRangedAttack(target, 1.0f);
            attackTime = attackIntervalMin;
        }

    }
}
