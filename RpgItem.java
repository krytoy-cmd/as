package com.alisa.rockets.item;

import com.alisa.rockets.entity.RocketProjectile;
import com.alisa.rockets.registry.ModEntities;
import com.alisa.rockets.registry.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RpgItem extends Item {
    public RpgItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            boolean creative = player.getAbilities().instabuild;
            if (!creative && !player.getInventory().contains(new ItemStack(ModItems.ROCKET_CHARGE.get()))) {
                return InteractionResultHolder.fail(stack);
            }
            if (!creative) {
                player.getInventory().clearOrCountMatchingItems(
                        s -> s.getItem() == ModItems.ROCKET_CHARGE.get(), 1, false);
            }
            Vec3 look = player.getLookAngle();
            RocketProjectile rocket = new RocketProjectile(ModEntities.ROCKET.get(), false,
                    player.getX(), player.getEyeY() - 0.1, player.getZ(),
                    look.x * 3.0, look.y * 3.0 + 0.2, look.z * 3.0, level);
            rocket.setOwner(player);
            level.addFreshEntity(rocket);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.5f, 0.9f);
            if (!creative) {
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            player.getCooldowns().addCooldown(this, 10);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
