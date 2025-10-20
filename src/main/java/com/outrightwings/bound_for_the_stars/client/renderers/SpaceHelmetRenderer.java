package com.outrightwings.bound_for_the_stars.client.renderers;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.item.SpaceHelmet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SpaceHelmetRenderer extends GeoArmorRenderer<SpaceHelmet> {
    public SpaceHelmetRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "armor/space_helmet")));
    }
    public RenderType getRenderType(SpaceHelmet animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
