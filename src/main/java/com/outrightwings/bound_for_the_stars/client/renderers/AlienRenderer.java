package com.outrightwings.bound_for_the_stars.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.client.models.AlienGeo;
import com.outrightwings.bound_for_the_stars.entity.Alien;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.joml.Matrix4f;
import software.bernie.example.client.renderer.entity.GremlinRenderer;
import software.bernie.example.entity.DynamicExampleEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

public class AlienRenderer extends GeoEntityRenderer<Alien> {
    protected ItemStack mainHandItem;
    protected ItemStack offhandItem;
    private static final String LEFT_HAND = "armL";
    private static final String RIGHT_HAND = "armR";
    public AlienRenderer(EntityRendererProvider.Context context) {
        super(context, new AlienGeo());
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, Alien animatable) {
                // Retrieve the items in the entity's hands for the relevant bone
                return switch (bone.getName()) {
                    case LEFT_HAND -> animatable.isLeftHanded() ?
                            AlienRenderer.this.mainHandItem : AlienRenderer.this.offhandItem;
                    case RIGHT_HAND -> animatable.isLeftHanded() ?
                            AlienRenderer.this.offhandItem : AlienRenderer.this.mainHandItem;
                    default -> null;
                };
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, Alien animatable) {
                // Apply the camera transform for the given hand
                return switch (bone.getName()) {
                    case LEFT_HAND, RIGHT_HAND -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    default -> ItemDisplayContext.NONE;
                };
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, Alien animatable,
                                              MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == AlienRenderer.this.mainHandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem)
                        poseStack.translate(0, 0.125, -0.25);
                }
                else if (stack == AlienRenderer.this.offhandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0, 0.125, 0.25);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    }
                }

                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }
    public ResourceLocation getTextureLocation(Alien entity) {
        int color = entity.skinColor();
        return switch (color) {
            case 1 -> ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/alien2.png");
            case 2 -> ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/alien1.png");
            default -> ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/alien.png");
        };
    }

    public void preRender(PoseStack poseStack, Alien animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack,animatable,model,bufferSource,buffer,isReRender,partialTick,packedLight,packedOverlay,red,green,blue,alpha);
        this.mainHandItem = animatable.getMainHandItem();
        this.offhandItem = animatable.getOffhandItem();
    }
    public void renderRecursively(PoseStack poseStack, Alien alien, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        // cape
        if(bone.getName().equals("cape")){
            if (!alien.hasCape()) {
                bone.setHidden(true);
            } else {
                bone.setHidden(false);
            }
        }

        // antena
        if(bone.getName().startsWith("antena")){
            int count = alien.antennaCount();
            bone.setHidden(false);
            if(count == 0) bone.setHidden(true);
            if(count == 1 && bone.getName().startsWith("antenaA")) bone.setHidden(true);
            if(count == 2 && bone.getName().startsWith("antenaB")) bone.setHidden(true);
        }

        super.renderRecursively(poseStack,alien,bone,renderType,bufferSource,buffer,isReRender,partialTick,packedLight,packedOverlay,red,green,blue,alpha);
    }

}
