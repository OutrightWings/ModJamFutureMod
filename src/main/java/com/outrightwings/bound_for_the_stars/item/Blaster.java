package com.outrightwings.bound_for_the_stars.item;

import com.outrightwings.bound_for_the_stars.entity.ModEntities;
import com.outrightwings.bound_for_the_stars.sound.ModSounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.nbt.CompoundTag;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Blaster extends Item {
    public Blaster(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if(!player.getCooldowns().isOnCooldown(this)){
            if (!level.isClientSide) {
                BlasterProjectile proj = BlasterProjectile.spawnAtPlayer(player,level);
                proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.25F, 0F);
                level.addFreshEntity(proj);

                player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                player.getCooldowns().addCooldown(this, 10); //20t = 1s

                level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        ModSounds.BLASTER_PEW.get(),
                        player.getSoundSource(),
                        1.0F, // volume
                        1F  // pitch
                );
            }
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(stack);
    }

    public static class BlasterProjectile extends Projectile implements GeoEntity {
        private int life;
        private final int MAX_LIFE = 40;
        public BlasterProjectile(EntityType<? extends BlasterProjectile> entityType, Level level) {
            super(entityType, level);
        }

        public static BlasterProjectile spawnAtPlayer(Entity player, Level level) {
            BlasterProjectile projectile = new BlasterProjectile(ModEntities.BLASTER_PROJECTILE.get(), level);
            projectile.setPos(player.getX(), player.getEyeY() - 0.1F, player.getZ());
            projectile.setOwner(player);
            return projectile;
        }

        @Override
        public void tick() {
            super.tick();

            Vec3 motion = this.getDeltaMovement();
            this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);

            EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(level(), this, this.position(), this.position().add(motion), this.getBoundingBox().expandTowards(motion).inflate(1.0D), (entity) -> entity instanceof LivingEntity && entity != this.getOwner());
            if (hitResult != null) {
                this.onHitEntity(hitResult);
            }

            if (!level().isClientSide) {
                life++;
                if (life > MAX_LIFE) this.discard();
            }
        }

        protected void onHitEntity(EntityHitResult hit) {
            if (hit.getEntity() instanceof LivingEntity living) {
                living.hurt(this.damageSources().thrown(this, this.getOwner()), 4.0F);
            }
            this.discard();
        }

        @Override
        protected void defineSynchedData() {
        }
        @Override
        protected void addAdditionalSaveData(CompoundTag tag) {
        }
        @Override
        protected void readAdditionalSaveData(CompoundTag tag) {
        }

        //Geckolib
        private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return geoCache;
        }

        public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        }
    }
}
