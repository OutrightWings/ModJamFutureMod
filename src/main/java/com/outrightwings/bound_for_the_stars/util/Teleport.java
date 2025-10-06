package com.outrightwings.bound_for_the_stars.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Teleport {
    public static void teleportPlayerWithShip(ServerPlayer player, ResourceKey<Level> targetDim, double x, double y, double z) {
        if (player.level().isClientSide) return;
        MinecraftServer server = player.getServer();
        if (server == null) return;
        ServerLevel destination = server.getLevel(targetDim);
        if (destination == null) return;

        Entity vehicle = player.getVehicle();
        boolean isRiding = vehicle != null;

        if (isRiding) {
            player.stopRiding();
        }

        player.teleportTo(destination, x, y, z, player.getYRot(), player.getXRot());

        if (isRiding && vehicle != null) {
            Entity newShip = vehicle.getType().create(destination);
            if (newShip != null) {
                newShip.restoreFrom(vehicle);
                newShip.moveTo(x, y, z, vehicle.getYRot(), vehicle.getXRot());
                destination.addFreshEntity(newShip);
                player.startRiding(newShip, true);
                vehicle.discard();
            }
        }
    }

}
