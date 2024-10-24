package net.voxelden.radiationApocalypse.component;

import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.voxelden.radiationApocalypse.RadiationApocalypse;

import java.util.function.UnaryOperator;

public class Components {
    public static final ComponentType<WeaponAmmoComponent> WEAPON_AMMO = register("weapon_ammo", builder -> builder.codec(WeaponAmmoComponent.CODEC).packetCodec(WeaponAmmoComponent.PACKET_CODEC).cache());
    public static final ComponentType<AttributeModifiersComponent> WEAPON_AMMO_MODIFIERS = register("weapon_ammo_modifiers", builder -> builder.codec(AttributeModifiersComponent.CODEC).packetCodec(AttributeModifiersComponent.PACKET_CODEC).cache());
    public static final ComponentType<WeaponAttachmentsComponent> WEAPON_ATTACHMENTS = register("weapon_attachments", builder -> builder.codec(WeaponAttachmentsComponent.CODEC).packetCodec(WeaponAttachmentsComponent.PACKET_CODEC).cache());
    public static final ComponentType<AttributeModifiersComponent> WEAPON_ATTACHMENT_MODIFIERS = register("weapon_attachment_modifiers", builder -> builder.codec(AttributeModifiersComponent.CODEC).packetCodec(AttributeModifiersComponent.PACKET_CODEC).cache());

    public static void register() {
        RadiationApocalypse.LOGGER.info("Registering components.");
    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, RadiationApocalypse.id(id), builderOperator.apply(ComponentType.builder()).build());
    }
}
