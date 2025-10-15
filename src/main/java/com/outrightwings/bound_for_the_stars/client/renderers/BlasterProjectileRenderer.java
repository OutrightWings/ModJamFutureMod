package com.outrightwings.bound_for_the_stars.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.item.Blaster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class BlasterProjectileRenderer extends GeoEntityRenderer<Blaster.BlasterProjectile> {
    public BlasterProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new BlasterProjectileGeo());
    }
    protected void applyRotations(Blaster.BlasterProjectile animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        poseStack.mulPose(Axis.YP.rotationDegrees(animatable.getYRot()));           // Yaw
        poseStack.mulPose(Axis.XP.rotationDegrees(-animatable.getXRot()));          // Pitch
    }
    public static class BlasterProjectileGeo extends DefaultedEntityGeoModel<Blaster.BlasterProjectile> {
        public BlasterProjectileGeo() {
            super(ResourceLocation.fromNamespaceAndPath(Main.MODID,"blaster_projectile"),false);
        }
    }
}
