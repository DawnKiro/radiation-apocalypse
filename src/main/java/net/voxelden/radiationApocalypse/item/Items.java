package net.voxelden.radiationApocalypse.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.voxelden.radiationApocalypse.RadiationApocalypse;

public class Items {
    public static final Item GUN = register("gun", new GunItem(new Item.Settings()));

    public static void register() {
        RadiationApocalypse.LOGGER.info("Registering items.");
    }

    public static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, RadiationApocalypse.id(name), item);
    }
}
