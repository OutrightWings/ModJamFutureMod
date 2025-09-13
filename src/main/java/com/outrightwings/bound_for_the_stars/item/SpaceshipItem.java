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

        // Determine final spawn position
        BlockPos placePos = clickedState.getCollisionShape(level, clicked).isEmpty()
                ? clicked
                : clicked.relative(face);

        EntityType<Spaceship> type = ModEntities.SPACESHIP_ENTITY.get();

        Spaceship ship = type.spawn(
                serverLevel,
                stack,
                ctx.getPlayer(),
                placePos,
                MobSpawnType.SPAWN_EGG,
                true,                             // align to surface
                !clicked.equals(placePos) && face == Direction.UP // same as spawn eggs
        );

        if (ship != null) {
            Player placer = ctx.getPlayer();
            if (placer != null) {
                float yaw = placer.getYRot() + 180.0F;
                ship.setYRot(yaw);
                ship.setYBodyRot(yaw);
                ship.setYHeadRot(yaw);
            }
            if (!ctx.getPlayer().getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.gameEvent(ctx.getPlayer(), GameEvent.ENTITY_PLACE, clicked);
            return InteractionResult.CONSUME;
        }

        ctx.getPlayer().awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.PASS;
    }
}
