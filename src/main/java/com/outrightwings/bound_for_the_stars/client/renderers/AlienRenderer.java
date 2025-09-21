package com.outrightwings.bound_for_the_stars.client.renderers;

import com.outrightwings.bound_for_the_stars.client.models.AlienGeo;
import com.outrightwings.bound_for_the_stars.client.models.SpaceshipGeo;
import com.outrightwings.bound_for_the_stars.entity.Alien;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AlienRenderer extends GeoEntityRenderer<Alien> {
    public AlienRenderer(EntityRendererProvider.Context context) {
        super(context, new AlienGeo());
    }
}
