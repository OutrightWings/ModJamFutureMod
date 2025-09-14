package com.outrightwings.bound_for_the_stars.item;

import com.outrightwings.bound_for_the_stars.entity.ModEntities;
import com.outrightwings.bound_for_the_stars.entity.Spaceship;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SpaceshipItem extends Item {
    public SpaceshipItem(Properties prop) {
        super(prop);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS; // client side: just let it animate
        }

        ItemStack stack = ctx.getItemInHand();
        BlockPos clicked = ctx.getClickedPos();
        Direction face = ctx.getClickedFace();
        var clickedState = level.getBlockState(clicked);

        // Determine final spawn position (same logic you had)
        BlockPos placePos = clickedState.getCollisionShape(level, clicked).isEmpty()
                ? clicked
                : clicked.relative(face);

        Player placer = ctx.getPlayer();
        if (placer == null) return InteractionResult.PASS;

        EntityType<Spaceship> type = ModEntities.SPACESHIP_ENTITY.get();
        Spaceship ship = type.create(serverLevel);
        if (ship == null) return InteractionResult.PASS;

        // --- position & rotation BEFORE adding to world ---
        double x = placePos.getX() + 0.5;
        double y = placePos.getY();
        double z = placePos.getZ() + 0.5;
        float yaw = placer.getYRot() + 180.0F;

        ship.absMoveTo(x, y, z, yaw, 0.0F);
        ship.setYHeadRot(yaw);
        ship.setYBodyRot(yaw);
        ship.refreshDimensions();

        // collision check (like vanilla boats)
        if (!serverLevel.noCollision(ship, ship.getBoundingBox())) {
            return InteractionResult.PASS;
        }

        // run spawn initialization
        ship.finalizeSpawn(
                serverLevel,
                serverLevel.getCurrentDifficultyAt(placePos),
                MobSpawnType.SPAWN_EGG,
                null,
                null
        );

        // now add to world — this sends the correct position/rotation to clients
        serverLevel.addFreshEntity(ship);

        // consume item if not in creative
        if (!placer.getAbilities().instabuild) {
            stack.shrink(1);
        }

        // fire game event & stat
        level.gameEvent(placer, GameEvent.ENTITY_PLACE, clicked);
        placer.awardStat(Stats.ITEM_USED.get(this));

        return InteractionResult.CONSUME;
    }


}
