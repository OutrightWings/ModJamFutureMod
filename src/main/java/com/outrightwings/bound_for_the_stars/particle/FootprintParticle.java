package com.outrightwings.bound_for_the_stars.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.outrightwings.bound_for_the_stars.Main;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FootprintParticle extends TextureSheetParticle {
    Vec3 facing;
    protected FootprintParticle(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);

        RandomSource random = level.random;
        this.lifetime = 200 + random.nextInt(0,40);
        this.quadSize = 0.25f;
        facing = new Vec3(xSpeedIn,ySpeedIn,zSpeedIn);
    }
    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z());

        float yaw = (float) Math.atan2(facing.x, facing.z);
        Quaternionf flat = new Quaternionf().rotationY((float)Math.toRadians(90.0));
        flat.rotateY(yaw-(float)Math.toRadians(90.0));

        Vector3f[] verts = new Vector3f[] {
                new Vector3f(-1.0F, 0.0F, -1.0F),
                new Vector3f(-1.0F, 0.0F,  1.0F),
                new Vector3f( 1.0F, 0.0F,  1.0F),
                new Vector3f( 1.0F, 0.0F, -1.0F)
        };

        float size = this.getQuadSize(partialTicks);

        for (Vector3f v : verts) {
            v.rotate(flat);
            v.mul(size);
            v.add(x, y, z);
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);

        buffer.vertex(verts[0].x(), verts[0].y(), verts[0].z()).uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(verts[1].x(), verts[1].y(), verts[1].z()).uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(verts[2].x(), verts[2].y(), verts[2].z()).uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(verts[3].x(), verts[3].y(), verts[3].z()).uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    @Override
    public void tick() {
        if (this.age++ >= this.lifetime)
            this.remove();
        float percent_age = ((float) this.age /this.lifetime);
        float a = -2 * percent_age + 2;
        this.alpha = Mth.clamp(a,0,1);
    }
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FootprintParticle op = new FootprintParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            op.pickSprite(this.spriteSet);
            return op;
        }
    }
}
