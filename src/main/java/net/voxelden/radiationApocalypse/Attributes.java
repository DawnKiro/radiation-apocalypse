package net.voxelden.radiationApocalypse;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class Attributes {
    // GUN ATTRIBUTES: projectile_speed, projectile_range, projectile_damage, projectile_damage_falloff, projectile_gravity, fire_rate, reload_duration, projectile_inaccuracy, projectile_bloom, recoil
    public static final RegistryEntry<EntityAttribute> PROJECTILE_BLOOM = registerClampedEntityAttribute("projectile_bloom", 0.0, 0.0, 64.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_DAMAGE = registerClampedEntityAttribute("projectile_damage", 1.0, 0.0, 4096.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_DAMAGE_FALLOFF = registerClampedEntityAttribute("projectile_damage_falloff", 0.0, 0.0, 4096.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_DRAG = registerClampedEntityAttribute("projectile_drag", 1.0, 0.0, 64.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_GRAVITY = registerClampedEntityAttribute("projectile_gravity", 0.0, -64.0, 64.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_RANGE = registerClampedEntityAttribute("projectile_range", 64.0, 0.0, 4096.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_SPEED = registerClampedEntityAttribute("projectile_speed", 1.0, 0.0, 64.0, false);
    public static final RegistryEntry<EntityAttribute> PROJECTILE_SPREAD = registerClampedEntityAttribute("projectile_spread", 0.0, 0.0, 64.0, false);
    public static final RegistryEntry<EntityAttribute> WEAPON_FIRE_RATE = registerClampedEntityAttribute("weapon_fire_rate", 1.0, 0.0, 40.0, true);
    public static final RegistryEntry<EntityAttribute> WEAPON_RECOIL = registerClampedEntityAttribute("weapon_recoil", 1.0, 0.0, 1.0, true);
    public static final RegistryEntry<EntityAttribute> WEAPON_RELOAD_DURATION = registerClampedEntityAttribute("weapon_reload_duration", 1.0, 0.0, 64.0, true);

    public static void register() {
        RadiationApocalypse.LOGGER.info("Registering attributes.");
    }

    private static RegistryEntry<EntityAttribute> registerClampedEntityAttribute(String name, double defaultValue, double minValue, double maxValue, boolean tracked) {
        return Registry.registerReference(Registries.ATTRIBUTE, RadiationApocalypse.id(name), new ClampedEntityAttribute("attribute.name." + name, Math.clamp(defaultValue, minValue, maxValue), minValue, maxValue).setTracked(tracked));
    }
}
