package com.alisa.rockets.registry;

import com.alisa.rockets.AlisaRocketsMod;
import com.alisa.rockets.entity.RocketProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AlisaRocketsMod.MODID);

    public static final RegistryObject<EntityType<RocketProjectile>> ROCKET = ENTITY_TYPES.register("rocket",
            () -> EntityType.Builder.<RocketProjectile>of(RocketProjectile::new, MobCategory.MISC)
                    .sized(0.7f, 0.7f)
                    .clientTrackingRange(10)
                    .updateInterval(1)
                    .build("rocket"));
}
