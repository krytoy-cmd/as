package com.alisa.rockets.registry;

import com.alisa.rockets.AlisaRocketsMod;
import com.alisa.rockets.block.RocketStationBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AlisaRocketsMod.MODID);

    public static final RegistryObject<Block> ROCKET_STATION = BLOCKS.register("rocket_station",
            () -> new RocketStationBlock(BlockBehaviour.Properties.of()
                    .strength(6.0f, 1200.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Item> ROCKET_STATION_ITEM = ModItems.ITEMS.register("rocket_station",
            () -> new BlockItem(ROCKET_STATION.get(), new Item.Properties()));
}
