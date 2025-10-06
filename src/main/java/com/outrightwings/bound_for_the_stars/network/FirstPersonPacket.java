package com.outrightwings.bound_for_the_stars.network;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FirstPersonPacket {
    public static void encode(FirstPersonPacket msg, FriendlyByteBuf buf) {}
    public static FirstPersonPacket decode(FriendlyByteBuf buf) { return new FirstPersonPacket(); }

    public static void handle(FirstPersonPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.options.setCameraType(CameraType.FIRST_PERSON);
        });
        ctx.get().setPacketHandled(true);
    }
}
