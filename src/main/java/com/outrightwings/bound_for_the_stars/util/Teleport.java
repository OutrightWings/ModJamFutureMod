package com.outrightwings.bound_for_the_stars.util;

import com.outrightwings.bound_for_the_stars.dimension.ModDimensions;
import com.outrightwings.bound_for_the_stars.network.F5Packet;
import com.outrightwings.bound_for_the_stars.network.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.network.PacketDistributor;

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

    public static void dimensionCheck(Entity entity){
        if (!(entity.getControllingPassenger() instanceof ServerPlayer player)) return;

        ServerLevel current = player.serverLevel();

        if (current.dimension() == Level.OVERWORLD && entity.getY() > 300) {
            ServerLevel target = player.getServer().getLevel(ModDimensions.SPACE);
            if (target != null) {
                int x = entity.getBlockX();
                int z = entity.getBlockZ();
                Teleport.teleportPlayerWithShip(player, ModDimensions.SPACE, x, 20, z);
            }
        }
        else if (current.dimension() == ModDimensions.SPACE && entity.getY() < 10) {
            ServerLevel target = player.getServer().getLevel(Level.OVERWORLD);
            if (target != null) {
                int x = entity.getBlockX();
                int z = entity.getBlockZ();
                int surfaceY = target.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                Teleport.teleportPlayerWithShip(player, Level.OVERWORLD, x, surfaceY, z);
            }
        }
        else if (current.dimension() == ModDimensions.SPACE && entity.getY() > 500) {
            ServerLevel target = player.getServer().getLevel(ModDimensions.MOON);
            if (target != null) {
                int x = entity.getBlockX();
                int z = entity.getBlockZ();
                //int surfaceY = target.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                int surfaceY = target.getMaxBuildHeight() - 1;
                while (surfaceY > target.getMinBuildHeight() && target.isEmptyBlock(new BlockPos(x, surfaceY, z))) {
                    surfaceY--;
                }
                surfaceY += 10;
                //System.out.println("Teleporting to Moon at: " + x + ", " + surfaceY + ", " + z);

                Teleport.teleportPlayerWithShip(player, ModDimensions.MOON, x, surfaceY, z);
            }
        }
        else if (current.dimension() == ModDimensions.MOON && entity.getY() > 300) {
            ServerLevel target = player.getServer().getLevel(ModDimensions.SPACE);
            if (target != null) {
                int x = entity.getBlockX();
                int z = entity.getBlockZ();
                Teleport.teleportPlayerWithShip(player, ModDimensions.SPACE, x, 400, z);
            }
        }
        ModPackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new F5Packet());
    }
}
