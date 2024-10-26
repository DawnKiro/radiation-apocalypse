package net.voxelden.radiationApocalypse.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;

import java.util.function.Function;

public class Items {
    public static final Item GUN = register("gun", GunItem::new, new Item.Settings());

    public static void register() {
        RadiationApocalypse.LOGGER.info("Registering items.");
    }

    public static Item register(String name, Function<Item.Settings, Item> constructor, Item.Settings settings) {
        Identifier id = RadiationApocalypse.id(name);
        return Registry.register(Registries.ITEM, id, constructor.apply(settings));
    }
}
