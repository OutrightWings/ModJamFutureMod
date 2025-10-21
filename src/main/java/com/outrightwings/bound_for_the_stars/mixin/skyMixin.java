package com.outrightwings.bound_for_the_stars.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.dimension.ModDimensions;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class skyMixin {
    private static final ResourceLocation EARTH_LOCATION = ResourceLocation.fromNamespaceAndPath(Main.MODID,"textures/environment/earth.png");
    private static final ResourceLocation EARTH_SINGLE_LOCATION = ResourceLocation.fromNamespaceAndPath(Main.MODID,"textures/environment/earth_single.png");
    @ModifyArg(method = "renderSky",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
            index = 1
    )
    public ResourceLocation injectSky(ResourceLocation r){
        LevelRenderer thisObject = (LevelRenderer) (Object)this;

        if(thisObject.level != null){
            if(thisObject.level.dimension() == ModDimensions.MOON && r == LevelRenderer.MOON_LOCATION){
                return EARTH_LOCATION;
            }
            if(thisObject.level.dimension() == ModDimensions.SPACE && r ==  LevelRenderer.SUN_LOCATION){
                return EARTH_SINGLE_LOCATION;
            }
        }
        return r;
    }
    @Inject(method = "renderClouds", at = @At("HEAD"),cancellable = true)
    public void removeClouds(PoseStack p_254145_, Matrix4f p_254537_, float p_254364_, double p_253843_, double p_253663_, double p_253795_, CallbackInfo ci){
        LevelRenderer thisObject = (LevelRenderer) (Object)this;

        if(thisObject.level != null){
            if(thisObject.level.dimension() == ModDimensions.MOON || thisObject.level.dimension() == ModDimensions.SPACE){
                ci.cancel();
            }
        }
    }
}
