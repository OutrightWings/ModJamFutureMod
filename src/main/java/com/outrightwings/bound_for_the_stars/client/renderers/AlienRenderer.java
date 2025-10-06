package com.outrightwings.bound_for_the_stars.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.client.models.AlienGeo;
import com.outrightwings.bound_for_the_stars.client.models.SpaceshipGeo;
import com.outrightwings.bound_for_the_stars.entity.Alien;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.loading.json.raw.Bone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AlienRenderer extends GeoEntityRenderer<Alien> {
    public AlienRenderer(EntityRendererProvider.Context context) {
        super(context, new AlienGeo());
    }
    public ResourceLocation getTextureLocation(Alien entity) {
        int color = entity.skinColor();
        return switch (color) {
            case 1 -> ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/alien2.png");
            case 2 -> ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/alien1.png");
            default -> ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/alien.png");
        };
    }
    public void renderRecursively(PoseStack poseStack, Alien alien, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        // cape
        if (bone.getName().equals("cape")) {
            if (!alien.hasCape()) {
                bone.setHidden(true);
            } else {
                bone.setHidden(false);
            }
        }

        // antena
        if (bone.getName().startsWith("antena")) {
            int count = alien.antennaCount();
            bone.setHidden(false);
            if(count == 0) bone.setHidden(true);
            if(count == 1 && bone.getName().startsWith("antenaA")) bone.setHidden(true);
            if(count == 2 && bone.getName().startsWith("antenaB")) bone.setHidden(true);
        }

        super.renderRecursively(poseStack,alien,bone,renderType,bufferSource,buffer,isReRender,partialTick,packedLight,packedOverlay,red,green,blue,alpha);
    }
}
