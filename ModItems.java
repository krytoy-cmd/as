package com.alisa.rockets.registry;

import com.alisa.rockets.AlisaRocketsMod;
import com.alisa.rockets.item.RocketItem;
import com.alisa.rockets.item.RpgItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AlisaRocketsMod.MODID);

    public static final RegistryObject<Item> ROCKET_CHARGE = ITEMS.register("rocket_charge",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ROCKET = ITEMS.register("rocket",
            () -> new RocketItem(false, new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> TSAR_BOMB = ITEMS.register("tsar_bomb",
            () -> new RocketItem(true, new Item.Properties().stacksTo(8).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> RPG = ITEMS.register("rpg",
            () -> new RpgItem(new Item.Properties().durability(128)));
}
