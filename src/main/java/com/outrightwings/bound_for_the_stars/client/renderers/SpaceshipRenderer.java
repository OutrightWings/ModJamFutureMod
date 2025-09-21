package com.outrightwings.bound_for_the_stars.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.client.models.SpaceshipGeo;
import com.outrightwings.bound_for_the_stars.entity.Spaceship;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SpaceshipRenderer extends GeoEntityRenderer<Spaceship> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/spaceship.png");
    public SpaceshipRenderer(EntityRendererProvider.Context context) {
        super(context, new SpaceshipGeo());
    }

    @Override
    public RenderType getRenderType(Spaceship animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    protected void applyRotations(Spaceship entity, PoseStack pose, float ageInTicks, float rotationYaw, float partialTicks) {
        // Keep the vanilla yaw rotation so it still faces correctly
        super.applyRotations(entity, pose, ageInTicks, rotationYaw, partialTicks);

        double pivotY = 1f;
        pose.translate(0, pivotY, 0);

        // Add our custom pitch tilt
        float pitch = entity.getShipPitch();
        pose.mulPose(Axis.XP.rotationDegrees(pitch));

        pose.translate(0, -pivotY, 0); // translate back after rotation
    }
}

