package net.voxelden.radiationApocalypse.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.voxelden.radiationApocalypse.Attributes;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import net.voxelden.radiationApocalypse.component.Components;
import net.voxelden.radiationApocalypse.component.WeaponAmmoComponent;
import net.voxelden.radiationApocalypse.component.WeaponAttachmentsComponent;

import java.util.List;
import java.util.Map;

public class GunItem extends Item {
    public GunItem(Settings settings) {
        super(addData(settings));
    }

    private static Settings addData(Settings settings) {
        settings.component(Components.WEAPON_AMMO, new WeaponAmmoComponent(List.of(), 1));
        settings.component(Components.WEAPON_ATTACHMENTS, new WeaponAttachmentsComponent(Map.of()));

        AttributeModifiersComponent.Builder attributes = AttributeModifiersComponent.builder();

        addAttributeModifier(attributes, Attributes.PROJECTILE_BLOOM, 1);
        addAttributeModifier(attributes, Attributes.PROJECTILE_DAMAGE, 2);

        return settings.attributeModifiers(attributes.build()).maxCount(1);
    }

    private static void addAttributeModifier(AttributeModifiersComponent.Builder builder, RegistryEntry<EntityAttribute> attribute, double value) {
        builder.add(Attributes.PROJECTILE_BLOOM, new EntityAttributeModifier(RadiationApocalypse.id("weapon.gun." + attribute.getIdAsString()), value, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.HAND);
    }
}
