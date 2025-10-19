package com.outrightwings.bound_for_the_stars.client.renderers;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.entity.GiantTardigrade;
import com.outrightwings.bound_for_the_stars.entity.Ufo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class GiantTardigradeRenderer extends GeoEntityRenderer<GiantTardigrade> {
    public GiantTardigradeRenderer(EntityRendererProvider.Context context) {
        super(context, new GiantTardigradeGeo());

    }
    public static class GiantTardigradeGeo extends DefaultedEntityGeoModel<GiantTardigrade> {
        public GiantTardigradeGeo() {
            super(ResourceLocation.fromNamespaceAndPath(Main.MODID,"giant_tardigrade"),false);
        }
    }
}
