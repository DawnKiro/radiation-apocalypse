package net.voxelden.radiationApocalypse.item;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.voxelden.radiationApocalypse.Attributes;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import net.voxelden.radiationApocalypse.client.InputHandler;
import net.voxelden.radiationApocalypse.client.keybind.Keybinds;
import net.voxelden.radiationApocalypse.component.Components;
import net.voxelden.radiationApocalypse.component.WeaponAmmoComponent;
import net.voxelden.radiationApocalypse.component.WeaponAttachmentsComponent;

import java.util.List;
import java.util.Map;

public class GunItem extends KeyConsumingItem {
    public GunItem(Settings settings) {
        super(addData(settings));
    }

    @Override
    public boolean consumeKeys(ClientPlayerEntity user, ItemStack stack) {
        if (InputHandler.consumeKey(Keybinds.GUN_FIRE)) {
            user.sendMessage(Text.literal("fire gun"), false);
        }
        return true;
    }

    private static Settings addData(Settings settings) {
        settings.component(Components.WEAPON_AMMO, new WeaponAmmoComponent(List.of(), 1, false));
        settings.component(Components.WEAPON_ATTACHMENTS, new WeaponAttachmentsComponent(Map.of()));

        AttributeModifiersComponent.Builder attributes = AttributeModifiersComponent.builder();

        addAttributeModifier(attributes, Attributes.PROJECTILE_BLOOM, 1);
        addAttributeModifier(attributes, Attributes.PROJECTILE_DAMAGE, 2);
        addAttributeModifier(attributes, Attributes.PROJECTILE_DAMAGE_FALLOFF, 1);
        addAttributeModifier(attributes, Attributes.PROJECTILE_DRAG, 0);
        addAttributeModifier(attributes, Attributes.PROJECTILE_GRAVITY, 0);
        addAttributeModifier(attributes, Attributes.PROJECTILE_RANGE, 64);
        addAttributeModifier(attributes, Attributes.PROJECTILE_SPEED, 64);
        addAttributeModifier(attributes, Attributes.PROJECTILE_SPREAD, 0);
        addAttributeModifier(attributes, Attributes.WEAPON_FIRE_RATE, 10);
        addAttributeModifier(attributes, Attributes.WEAPON_RECOIL, 0);
        addAttributeModifier(attributes, Attributes.WEAPON_RELOAD_DURATION, 1);

        return settings.attributeModifiers(attributes.build()).maxCount(1);
    }

    private static void addAttributeModifier(AttributeModifiersComponent.Builder builder, RegistryEntry<EntityAttribute> attribute, double value) {
        String[] translationKey = attribute.value().getTranslationKey().split("\\.");
        builder.add(Attributes.PROJECTILE_BLOOM, new EntityAttributeModifier(RadiationApocalypse.id("weapon.gun." + translationKey[translationKey.length - 1]), value, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.HAND);
    }
}
