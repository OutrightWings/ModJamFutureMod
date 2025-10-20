package com.outrightwings.bound_for_the_stars.client;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.outrightwings.bound_for_the_stars.client.renderers.*;
import com.outrightwings.bound_for_the_stars.entity.ModEntities;
import com.outrightwings.bound_for_the_stars.entity.Spaceship;
import com.outrightwings.bound_for_the_stars.particle.FootprintParticle;
import com.outrightwings.bound_for_the_stars.particle.ModParticle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntities.SPACESHIP_ENTITY.get(), SpaceshipRenderer::new);
        event.registerEntityRenderer(ModEntities.ALIEN_ENTITY.get(), AlienRenderer::new);
        event.registerEntityRenderer(ModEntities.MOON_COW_ENTITY.get(), MoonCowRenderer::new);
        event.registerEntityRenderer(ModEntities.TINY_TARDIGRADE_ENTITY.get(), TinyTardigradeRenderer::new);
        event.registerEntityRenderer(ModEntities.BLASTER_PROJECTILE.get(), BlasterProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.GIANT_TARDIGRADE_ENTITY.get(), GiantTardigradeRenderer::new);
        event.registerEntityRenderer(ModEntities.UFO_ENTITY.get(), UfoRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(ModParticle.FOOTPRINT.get(), FootprintParticle.Factory::new);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeClientEvents {

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
