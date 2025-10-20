package com.outrightwings.bound_for_the_stars;

import com.outrightwings.bound_for_the_stars.dimension.ModDimensions;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import com.outrightwings.bound_for_the_stars.network.ModPackets;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Random;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Events {
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(()->{
            ModPackets.register();
        });
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents{
        @SubscribeEvent
        public static void breathing(LivingBreatheEvent event)
        {
            if(event.getEntity() instanceof Player player){
                Level level = player.level();
                if(!level.isClientSide){
                    if(level.dimension() == ModDimensions.MOON){

                        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.SPACE_HELMET.get();

                        if(player.isCreative() || player.isSpectator() || hasHelmet || player.getEffect(MobEffects.WATER_BREATHING) != null){
                            event.setCanBreathe(true);
                            event.setCanRefillAir(true);
                            return;
                        }
                        event.setCanRefillAir(false);
                        event.setCanBreathe(false);
                        event.setConsumeAirAmount(2);
                    }
                }
            }
        }
    }
}
