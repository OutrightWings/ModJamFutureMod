package com.outrightwings.bound_for_the_stars.block;

import com.outrightwings.bound_for_the_stars.Main;
import com.outrightwings.bound_for_the_stars.particle.ModParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class MoonDustBlock extends GravelBlock {
    public MoonDustBlock(Properties properties) {
        super(properties);
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof Player player && player.getDeltaMovement().length() != 0){
            Vec3 standingAt = new Vec3(player.getX(),player.getY(),player.getZ());
            spawnParticles(level,standingAt,entity.getLookAngle());
        }
    }
    private static void spawnParticles(Level level, Vec3 pos,Vec3 looking ) {
        RandomSource random = level.random;
        //Dust
        float dx = Mth.lerp(random.nextFloat(),-.5f,.5f);
        float dz = Mth.lerp(random.nextFloat(),-.5f,.5f);
        level.addParticle(new DustParticleOptions(Vec3.fromRGB24(0x959aa9).toVector3f(),1), pos.x()+dx, pos.y(), pos.z()+dz, 0.0F, 0.0F, 0.0F);

        //Footprint
        long time = level.getGameTime();
        if(time % 5 == 0){
            Vec3 axis = new Vec3(0,1,0).cross(looking).normalize();
            float offset = 0.2f;
            axis = axis.multiply(offset,0,offset);

            float dy = Mth.lerp(random.nextFloat(),0.0001f,0.01f);

            if(time % 2 != 0) //right foot
                level.addParticle(ModParticle.FOOTPRINT.get(),pos.x+axis.x(),pos.y+dy,pos.z+axis.z(),looking.x,looking.y,looking.z);
            else //left foot
                level.addParticle(ModParticle.FOOTPRINT.get(),pos.x-axis.x(),pos.y+dy,pos.z-axis.z(),looking.x,looking.y,looking.z);
        }
    }
}
