package com.outrightwings.bound_for_the_stars.client.renderers;

import com.outrightwings.bound_for_the_stars.client.models.SpaceshipGeo;
import com.outrightwings.bound_for_the_stars.entity.Spaceship;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SpaceshipRenderer extends GeoEntityRenderer<Spaceship> {
    public SpaceshipRenderer(EntityRendererProvider.Context context){
        super(context, new SpaceshipGeo());
    }
}
