package com.alisa.rockets.item;

import com.alisa.rockets.entity.RocketProjectile;
import com.alisa.rockets.registry.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RocketItem extends Item {
    private final boolean tsar;

    public RocketItem(boolean tsar, Properties properties) {
        super(properties);
        this.tsar = tsar;
    }

    public boolean isTsar() {
        return tsar;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            Vec3 look = player.getLookAngle();
            RocketProjectile rocket = new RocketProjectile(ModEntities.ROCKET.get(), tsar,
                    player.getX(), player.getEyeY() - 0.1, player.getZ(),
                    look.x * 2.0, look.y * 2.0, look.z * 2.0, level);
            rocket.setOwner(player);
            level.addFreshEntity(rocket);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 2.0f, tsar ? 0.6f : 1.0f);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
