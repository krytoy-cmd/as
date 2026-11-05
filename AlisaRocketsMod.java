package com.alisa.rockets;

import com.alisa.rockets.registry.ModBlocks;
import com.alisa.rockets.registry.ModEntities;
import com.alisa.rockets.registry.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AlisaRocketsMod.MODID)
public class AlisaRocketsMod {
    public static final String MODID = "alisa_rockets";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AlisaRocketsMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModEntities.ENTITY_TYPES.register(bus);
    }
}
