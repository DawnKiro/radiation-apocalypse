package net.voxelden.radiationApocalypse.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.voxelden.radiationApocalypse.Attributes;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import net.voxelden.radiationApocalypse.component.Components;
import net.voxelden.radiationApocalypse.component.WeaponAmmoComponent;
import net.voxelden.radiationApocalypse.component.WeaponAttachmentsComponent;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Items {
    public static final Item GUN = register("gun", GunItem::new, new Item.Settings().attributeModifiers(
            addAttributeModifiers(AttributeModifiersComponent.builder(), "weapon.gun",
            new BuilderAttribute(Attributes.PROJECTILE_BLOOM, 1),
            new BuilderAttribute(Attributes.PROJECTILE_DAMAGE, 2),
            new BuilderAttribute(Attributes.PROJECTILE_DAMAGE_FALLOFF, 1),
            new BuilderAttribute(Attributes.PROJECTILE_DRAG, 0),
            new BuilderAttribute(Attributes.PROJECTILE_GRAVITY, 0),
            new BuilderAttribute(Attributes.PROJECTILE_RANGE, 64),
            new BuilderAttribute(Attributes.PROJECTILE_SPEED, 64),
            new BuilderAttribute(Attributes.PROJECTILE_SPREAD, 0),
            new BuilderAttribute(Attributes.WEAPON_FIRE_RATE, 10),
            new BuilderAttribute(Attributes.WEAPON_RECOIL, 0),
            new BuilderAttribute(Attributes.WEAPON_RELOAD_DURATION, 1)
    ).build())
            .component(Components.WEAPON_AMMO, new WeaponAmmoComponent(List.of(), 1, false))
            .component(Components.WEAPON_ATTACHMENTS, new WeaponAttachmentsComponent("gun", Map.of()))
    );

    public static void register() {
        RadiationApocalypse.LOGGER.info("Registering items.");
    }

    public static Item register(String name, Function<Item.Settings, Item> constructor, Item.Settings settings) {
        return Registry.register(Registries.ITEM, RadiationApocalypse.id(name), constructor.apply(settings));
    }

    private static AttributeModifiersComponent.Builder addAttributeModifiers(AttributeModifiersComponent.Builder builder, String prefix, BuilderAttribute... attributes) {
        for (BuilderAttribute builderAttribute : attributes) {
            String[] translationKey = builderAttribute.attribute().value().getTranslationKey().split("\\.");
            builder.add(Attributes.PROJECTILE_BLOOM, new EntityAttributeModifier(RadiationApocalypse.id(prefix + "." + translationKey[translationKey.length - 1]), builderAttribute.value(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND);
        }
        return builder;
    }

    private record BuilderAttribute(RegistryEntry<EntityAttribute> attribute, double value) {
    }
}
