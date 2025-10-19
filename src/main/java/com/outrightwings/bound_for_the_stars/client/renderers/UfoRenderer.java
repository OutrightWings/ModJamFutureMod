package com.outrightwings.bound_for_the_stars.client.renderers;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.entity.TinyTardigrade;
import com.outrightwings.bound_for_the_stars.entity.Ufo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class UfoRenderer extends GeoEntityRenderer<Ufo> {
    public UfoRenderer(EntityRendererProvider.Context context) {
        super(context, new UfoGeo());

    }
    public static class UfoGeo extends DefaultedEntityGeoModel<Ufo> {
        public UfoGeo() {
            super(ResourceLocation.fromNamespaceAndPath(Main.MODID,"ufo"),false);
        }
    }
}
