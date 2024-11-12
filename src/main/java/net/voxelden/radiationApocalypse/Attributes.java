package net.voxelden.radiationApocalypse;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class Attributes {
    public static final RegistryEntry<EntityAttribute> PROJECTILE_BLOOM = registerClampedEntityAttribute("projectile_bloom", 0.0, -180.0, 180.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_DAMAGE = registerClampedEntityAttribute("projectile_damage", 0.0, 0.0, 4096.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_DAMAGE_FALLOFF = registerClampedEntityAttribute("projectile_damage_falloff", 0.0, 0.0, 4096.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_DRAG = registerClampedEntityAttribute("projectile_drag", 0.0, 0.0, 1.0, true);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_GRAVITY = registerClampedEntityAttribute("projectile_gravity", 0.0, -64.0, 64.0, true);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_RANGE = registerClampedEntityAttribute("projectile_range", 0.0, 0.0, 4096.0, true);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_SPEED = registerClampedEntityAttribute("projectile_speed", 0.0, 0.0, 64.0, true);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_SPREAD = registerClampedEntityAttribute("projectile_spread", 0.0, 0.0, 180.0, false);
    public static final RegistryEntry<EntityAttribute> WEAPON_FIRE_RATE = registerClampedEntityAttribute("weapon_fire_rate", 0.0, 0.0, 40.0, true);
    public static final RegistryEntry<EntityAttribute> WEAPON_RECOIL = registerClampedEntityAttribute("weapon_recoil", 0.0, -1.0, 1.0, true);
    public static final RegistryEntry<EntityAttribute> WEAPON_RELOAD_DURATION = registerClampedEntityAttribute("weapon_reload_duration", 0.0, 0.0, 64.0, true);

    public static void register() {
        RadiationApocalypse.LOGGER.info("Registering attributes.");
    }

    private static RegistryEntry<EntityAttribute> registerClampedEntityAttribute(String name, double defaultValue, double minValue, double maxValue, boolean tracked) {
        return Registry.registerReference(Registries.ATTRIBUTE, RadiationApocalypse.id(name), new ClampedEntityAttribute("attribute.name." + name, Math.clamp(defaultValue, minValue, maxValue), minValue, maxValue).setTracked(tracked));
    }
}
