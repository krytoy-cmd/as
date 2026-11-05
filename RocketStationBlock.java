package com.alisa.rockets.block;

import com.alisa.rockets.entity.RocketProjectile;
import com.alisa.rockets.item.RocketItem;
import com.alisa.rockets.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class RocketStationBlock extends Block {
    public RocketStationBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof RocketItem)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            boolean tsar = ((RocketItem) stack.getItem()).isTsar();
            RocketProjectile rocket = new RocketProjectile(ModEntities.ROCKET.get(), tsar,
                    pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 0.0, 1.8, 0.0, level);
            rocket.setOwner(player);
            level.addFreshEntity(rocket);
            level.playSound(null, pos, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.BLOCKS, 3.0f, 0.6f);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
