package com.outrightwings.bound_for_the_stars.network;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class F5Packet {
    public static void encode(F5Packet msg, FriendlyByteBuf buf) {}
    public static F5Packet decode(FriendlyByteBuf buf) { return new F5Packet(); }

    public static void handle(F5Packet msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        });
        ctx.get().setPacketHandled(true);
    }
}
