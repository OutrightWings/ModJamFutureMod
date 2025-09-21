package com.outrightwings.bound_for_the_stars.client.models;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.entity.Alien;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AlienGeo extends DefaultedEntityGeoModel<Alien> {
    public AlienGeo() {
        super(new ResourceLocation(Main.MODID,"alien"));
    }
}
