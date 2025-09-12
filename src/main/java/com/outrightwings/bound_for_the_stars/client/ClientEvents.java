package com.outrightwings.bound_for_the_stars.client;

import com.outrightwings.bound_for_the_stars.client.models.SpaceshipGeo;
import com.outrightwings.bound_for_the_stars.client.renderers.SpaceshipRenderer;
import com.outrightwings.bound_for_the_stars.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntities.SPACESHIP_ENTITY.get(), SpaceshipRenderer::new);
    }
}
