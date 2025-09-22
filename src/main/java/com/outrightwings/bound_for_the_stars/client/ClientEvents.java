package com.outrightwings.bound_for_the_stars.client;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.client.models.SpaceshipGeo;
import com.outrightwings.bound_for_the_stars.client.renderers.AlienRenderer;
import com.outrightwings.bound_for_the_stars.client.renderers.SpaceshipRenderer;
import com.outrightwings.bound_for_the_stars.entity.ModEntities;
import com.outrightwings.bound_for_the_stars.entity.Spaceship;
import com.outrightwings.bound_for_the_stars.particle.FootprintParticle;
import com.outrightwings.bound_for_the_stars.particle.ModParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntities.SPACESHIP_ENTITY.get(), SpaceshipRenderer::new);
        event.registerEntityRenderer(ModEntities.ALIEN_ENTITY.get(), AlienRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(ModParticle.FOOTPRINT.get(), FootprintParticle.Factory::new);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class RiderTiltHandler {

        @SubscribeEvent
        public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
            Player player = event.getEntity();
            if (!(player.getVehicle() instanceof Spaceship ship)) return;

            PoseStack pose = event.getPoseStack();
            pose.pushPose();
            float pitch = ship.getShipPitch();
            Vec3 lookDirection = ship.getLookAngle();
            Vec3 axisRotationVec = new Vec3(0,-1,0).cross(lookDirection);
            var axis = Axis.of(axisRotationVec.toVector3f());
            pose.mulPose(axis.rotationDegrees(pitch));
            //pose.popPose();
        }

        @SubscribeEvent
        public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
            Player player = event.getEntity();
            if (!(player.getVehicle() instanceof Spaceship)) return;

            event.getPoseStack().popPose();
        }
    }

}
