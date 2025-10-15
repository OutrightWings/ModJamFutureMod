package com.outrightwings.bound_for_the_stars.client.renderers;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.entity.Alien;
import com.outrightwings.bound_for_the_stars.entity.MoonCow;
import com.outrightwings.bound_for_the_stars.entity.TinyTardigrade;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@OnlyIn(Dist.CLIENT)
public class TinyTardigradeRenderer extends GeoEntityRenderer<TinyTardigrade> {
    public TinyTardigradeRenderer(EntityRendererProvider.Context context) {
        super(context, new TinyTardigradeGeo());

    }
    public ResourceLocation getTextureLocation(TinyTardigrade entity) {
        int color = entity.skinColor();
        return switch (color) {
            case 1 -> ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/tiny_tardigrade2.png");
            case 2 -> ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/tiny_tardigrade1.png");
            default -> ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/tiny_tardigrade.png");
        };
    }
    public static class TinyTardigradeGeo extends DefaultedEntityGeoModel<TinyTardigrade> {
        public TinyTardigradeGeo() {
            super(ResourceLocation.fromNamespaceAndPath(Main.MODID,"tiny_tardigrade"),false);
        }
    }
}
