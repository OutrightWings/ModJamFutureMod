package com.outrightwings.bound_for_the_stars.client.models;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.entity.Spaceship;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
@OnlyIn(Dist.CLIENT)
public class SpaceshipGeo extends DefaultedEntityGeoModel<Spaceship> {
    public SpaceshipGeo() {
        super(new ResourceLocation(Main.MODID,"spaceship"));
    }

}
