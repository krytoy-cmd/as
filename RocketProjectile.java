package com.alisa.rockets.entity;

import com.alisa.rockets.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RocketProjectile extends AbstractHurtingProjectile implements ItemSupplier {
    private boolean tsar;

    public RocketProjectile(EntityType<? extends RocketProjectile> type, Level level) {
        super(type, level);
    }

    public RocketProjectile(EntityType<? extends RocketProjectile> type, boolean tsar,
                            double x, double y, double z, double ax, double ay, double az, Level level) {
        super(type, x, y, z, ax, ay, az, level);
        this.tsar = tsar;
    }

    public boolean isTsar() {
        return tsar;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(tsar ? Items.TNT : Items.FIREWORK_ROCKET);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            if (tickCount % 2 == 0) {
                level().addParticle(ParticleTypes.LARGE_SMOKE, getX(), getY(), getZ(), 0, 0.01, 0);
                level().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, -0.02, 0);
            }
        } else if (tickCount == 1 || tickCount % 8 == 0) {
            level().playSound(null, getX(), getY(), getZ(),
                    SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.5f, 1.3f);
        }
        if (tickCount > 1200) {
            discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            explode();
        }
        discard();
    }

    private void explode() {
        if (tsar) {
            explodeTsar(getX(), getY(), getZ());
        } else {
            explodeRocket(getX(), getY(), getZ());
        }
    }

    private void explodeRocket(double x, double y, double z) {
        level().explode(this, x, y, z, 10.0f, Level.ExplosionInteraction.TNT);
        for (int i = 0; i < 40; i++) {
            int dx = random.nextInt(9) - 4;
            int dz = random.nextInt(9) - 4;
            BlockPos pos = new BlockPos((int) x + dx, (int) y, (int) z + dz);
            if (level().getBlockState(pos).isAir()
                    && level().getBlockState(pos.below()).isSolidRender(level(), pos.below())) {
                level().setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
            }
        }
    }

    private void explodeTsar(double x, double y, double z) {
        level().explode(this, x, y, z, 22.0f, Level.ExplosionInteraction.TNT);

        for (int i = 0; i < 8; i++) {
            double ang = Math.PI * 2.0 * i / 8.0 + random.nextDouble();
            double dx = Math.cos(ang) * 12.0;
            double dz = Math.sin(ang) * 12.0;
            level().explode(this, x + dx, y, z + dz, 8.0f, Level.ExplosionInteraction.TNT);
        }

        for (Entity entity : level().getEntities(this,
                new AABB(x - 48.0, y - 24.0, z - 48.0, x + 48.0, y + 32.0, z + 48.0))) {
            double dist = entity.distanceTo(this);
            if (dist < 48.0) {
                float strength = (float) (1.0 - dist / 48.0);
                entity.hurt(level().damageSources().explosion(this), 200.0f * strength);
                Vec3 dir = entity.position().subtract(x, y, z).normalize();
                if (dir.lengthSqr() < 1.0E-4) {
                    dir = new Vec3(0, 1, 0);
                }
                entity.setDeltaMovement(dir.scale(2.5 * strength).add(0, 0.8 * strength + 0.2, 0));
                if (strength > 0.3) {
                    entity.setSecondsOnFire(20);
                }
            }
        }

        for (int i = 0; i < 250; i++) {
            double ang = random.nextDouble() * Math.PI * 2.0;
            double rr = Math.sqrt(random.nextDouble()) * 16.0;
            int bx = (int) Math.round(x + Math.cos(ang) * rr);
            int bz = (int) Math.round(z + Math.sin(ang) * rr);
            BlockPos found = null;
            for (int sy = (int) y + 8; sy >= (int) y - 4; sy--) {
                BlockPos p = new BlockPos(bx, sy, bz);
                if (level().getBlockState(p).isSolidRender(level(), p)
                        && level().getBlockState(p.above()).isAir()) {
                    found = p.above();
                    break;
                }
            }
            if (found != null) {
                level().setBlock(found, Blocks.FIRE.defaultBlockState(), 3);
            }
        }

        if (level() instanceof ServerLevel server) {
            for (int i = 0; i < 10; i++) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(server);
                if (bolt != null) {
                    bolt.moveTo(x + (random.nextDouble() - 0.5) * 30.0, y, z + (random.nextDouble() - 0.5) * 30.0);
                    server.addFreshEntity(bolt);
                }
            }

            for (int i = 0; i < 40; i++) {
                double yy = y + i * 1.1;
                double spread = Math.min(6.0, i * 0.25);
                server.sendParticles(ParticleTypes.LARGE_SMOKE,
                        x + (random.nextDouble() - 0.5) * spread, yy, z + (random.nextDouble() - 0.5) * spread,
                        3, 1.5, 0.6, 1.5, 0.01);
            }
            for (int i = 0; i < 120; i++) {
                double ang = random.nextDouble() * Math.PI * 2.0;
                double rr = 4.0 + random.nextDouble() * 14.0;
                server.sendParticles(ParticleTypes.CLOUD,
                        x + Math.cos(ang) * rr, y + 26 + (random.nextDouble() - 0.5) * 8.0, z + Math.sin(ang) * rr,
                        1, 2.0, 1.0, 2.0, 0.02);
            }
            for (double rr = 4.0; rr <= 40.0; rr += 6.0) {
                for (int a = 0; a < 24; a++) {
                    double ang = Math.PI * 2.0 * a / 24.0;
                    server.sendParticles(ParticleTypes.EXPLOSION,
                            x + Math.cos(ang) * rr, y + 1.0, z + Math.sin(ang) * rr, 1, 0, 0, 0, 0);
                }
            }

            level().playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 6.0f, 0.6f);
            level().playSound(null, x, y, z, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 4.0f, 0.7f);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Tsar", tsar);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        tsar = tag.getBoolean("Tsar");
    }
}
