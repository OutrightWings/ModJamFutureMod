package com.outrightwings.bound_for_the_stars.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.dimension.ModDimensions;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class skyMixin {
    private static final ResourceLocation EARTH_LOCATION = ResourceLocation.fromNamespaceAndPath(Main.MODID,"textures/environment/earth.png");
    @Inject(method = "renderSky",
    at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V", shift = At.Shift.AFTER))
    public void injectSky(CallbackInfo ci){
        LevelRenderer thisObject = (LevelRenderer) (Object)this;

        if(thisObject.level != null){
            if(thisObject.level.dimension() == ModDimensions.MOON){
                RenderSystem.setShaderTexture(0, EARTH_LOCATION);
            }
        }
    }
}
