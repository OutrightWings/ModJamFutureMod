package com.outrightwings.bound_for_the_stars.client.models;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.entity.Spaceship;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;
@OnlyIn(Dist.CLIENT)
public class SpaceshipGeo extends GeoModel<Spaceship> {
    private final ResourceLocation model = new ResourceLocation(Main.MODID, "geo/spaceship.geo.json");
    private final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/entity/spaceship.png");
    private final ResourceLocation animations = new ResourceLocation(Main.MODID, "animations/spaceship.animation.json");

    @Override
    public ResourceLocation getModelResource(Spaceship spaceship) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(Spaceship spaceship) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(Spaceship spaceship) {
        return animations;
    }
}
