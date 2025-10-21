package com.outrightwings.bound_for_the_stars.util;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.dimension.ModDimensions;
import com.outrightwings.bound_for_the_stars.network.F5Packet;
import com.outrightwings.bound_for_the_stars.network.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.network.PacketDistributor;

import java.awt.*;
import java.util.function.Function;

public class Teleport {
    //TODO THIS IS REALLY BUGGY
    public static void teleportPlayerWithShip(ServerPlayer player, ResourceKey<Level> targetDim, double x, double y, double z) {
        if (player.level().isClientSide) return;
        MinecraftServer server = player.getServer();
        if (server == null) return;
        ServerLevel destination = server.getLevel(targetDim);
        if (destination == null) return;

        Entity vehicle = player.getVehicle();
        boolean wasRiding = vehicle != null;

        if (wasRiding) {
            player.stopRiding();
        }

        player.changeDimension(destination, new SimpleTeleporter(x, y, z));
        destination.getServer().execute(() -> {
            player.teleportTo(destination, x, y, z, player.getYRot(), player.getXRot());
            player.connection.teleport(x, y, z, player.getYRot(), player.getXRot());
        });
        if (wasRiding && vehicle != null) {
            Entity newShip = vehicle.getType().create(destination);
            if (newShip != null) {
                newShip.restoreFrom(vehicle);
                newShip.moveTo(x, y, z, vehicle.getYRot(), vehicle.getXRot());
                destination.addFreshEntity(newShip);
                vehicle.discard();
                destination.getServer().execute(() -> player.startRiding(newShip, true));
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
            ModPackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new F5Packet());
            return;
        }
        else if (current.dimension() == ModDimensions.SPACE && entity.getY() < 10) {
            ServerLevel target = player.getServer().getLevel(Level.OVERWORLD);
            if (target != null) {
                int x = entity.getBlockX();
                int z = entity.getBlockZ();
                int surfaceY = target.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                Teleport.teleportPlayerWithShip(player, Level.OVERWORLD, x, surfaceY, z);
            }
            ModPackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new F5Packet());
            return;
        }
        else if (current.dimension() == ModDimensions.SPACE && entity.getY() > 350) {
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
                System.out.println("Teleporting to Moon at: " + x + ", " + surfaceY + ", " + z);

                Teleport.teleportPlayerWithShip(player, ModDimensions.MOON, x, surfaceY, z);
            }
            ModPackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new F5Packet());
            return;
        }
        else if (current.dimension() == ModDimensions.MOON && entity.getY() > 400) {
            ServerLevel target = player.getServer().getLevel(ModDimensions.SPACE);
            if (target != null) {
                int x = entity.getBlockX();
                int z = entity.getBlockZ();
                Teleport.teleportPlayerWithShip(player, ModDimensions.SPACE, x, 300, z);
            }
            ModPackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new F5Packet());
            return;
        }

    }

    public static class SimpleTeleporter implements ITeleporter {
        private final Vec3 pos;

        public SimpleTeleporter(double x, double y, double z) {
            this.pos = new Vec3(x, y, z);
        }

        @Override
        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destinationWorld,
                                  float yaw, Function<Boolean, Entity> repositionEntity) {
            Entity newEntity = repositionEntity.apply(true);
            newEntity.moveTo(pos.x, pos.y, pos.z, yaw, entity.getXRot());
            return newEntity;
        }

        @Override
        public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
            return false;
        }
    }
}
