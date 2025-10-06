package com.outrightwings.bound_for_the_stars.network;

import com.outrightwings.bound_for_the_stars.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.outrightwings.bound_for_the_stars.Main.MODID;

public class ModPackets {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MODID, "packet_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, F5Packet.class,
                F5Packet::encode,
                F5Packet::decode,
                F5Packet::handle);
        CHANNEL.registerMessage(id++, FirstPersonPacket.class,
                FirstPersonPacket::encode,
                FirstPersonPacket::decode,
                FirstPersonPacket::handle);
    }
}
