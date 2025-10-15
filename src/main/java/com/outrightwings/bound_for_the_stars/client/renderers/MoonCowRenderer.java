package com.outrightwings.bound_for_the_stars.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.entity.Alien;
import com.outrightwings.bound_for_the_stars.entity.MoonCow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class MoonCowRenderer extends GeoEntityRenderer<MoonCow> {
    public MoonCowRenderer(EntityRendererProvider.Context context) {
        super(context, new MoonCowGeo());

    }

    @Override
    public void preRender(PoseStack poseStack, MoonCow animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if(animatable.isBaby()) {
            poseStack.scale (0.5f, 0.5f, 0.5f);
        } else {
            poseStack.scale (1f, 1f, 1f);
        }
    }
    public void renderRecursively(PoseStack poseStack, MoonCow alien, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        if(alien.isBaby()){
            if (bone.getName().equals("head")) {
                poseStack.scale(2.0f, 2.0f, 2.0f);
                poseStack.translate(0, -0.5f, .3);
            }
        }
        if(bone.getName().equals("hornsLarge")){
            bone.setHidden(alien.isBaby());
        }
        if(bone.getName().equals("hornsSmall"))
            bone.setHidden(!alien.isBaby());

        super.renderRecursively(poseStack,alien,bone,renderType,bufferSource,buffer,isReRender,partialTick,packedLight,packedOverlay,red,green,blue,alpha);
        poseStack.popPose();
    }

    public static class MoonCowGeo extends DefaultedEntityGeoModel<MoonCow> {
        public MoonCowGeo() {
            super(ResourceLocation.fromNamespaceAndPath(Main.MODID,"moon_cow"),true);
        }
    }
}
