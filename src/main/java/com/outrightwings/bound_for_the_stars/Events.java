package com.outrightwings.bound_for_the_stars;

import com.outrightwings.bound_for_the_stars.block.ModBlocks;
import com.outrightwings.bound_for_the_stars.dimension.ModDimensions;
import com.outrightwings.bound_for_the_stars.entity.ModEntities;
import com.outrightwings.bound_for_the_stars.entity.Spawning;
import com.outrightwings.bound_for_the_stars.item.ModItems;
import com.outrightwings.bound_for_the_stars.network.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Random;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Events {
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event){
        event.enqueueWork(()->{
            ModPackets.register();
            Spawning.registerSpawnPlacements();
        });
    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents{
        @SubscribeEvent
        public static void breathing(LivingBreatheEvent event){
            if(event.getEntity() instanceof Player player){
                Level level = player.level();
                if(!level.isClientSide){
                    if(level.dimension() == ModDimensions.MOON || level.dimension() == ModDimensions.SPACE){

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
        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            Level world = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getEntity();
            ItemStack heldItem = event.getItemStack();

            if (world.getBlockState(pos).is(Blocks.CAULDRON) && heldItem.getItem() == Items.MILK_BUCKET) {
                if (!world.isClientSide) {
                    player.setItemInHand(event.getHand(), new ItemStack(Items.BUCKET));
                    world.setBlock(pos, ModBlocks.MILK_CAULDRON.get().defaultBlockState(), 3);
                }
                event.setCanceled(true);
            }
        }
    }
}
