package net.voxelden.radiationApocalypse.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class GunItem extends WeaponItem implements KeysetActivator {
    public GunItem(Settings settings) {
        super(settings);
    }

    @Override
    protected void addTriggerableAnimations() {
        addTriggerableAnimation("fire");
        addTriggerableAnimation("reload");
    }

    public void fire(LivingEntity entity, ItemStack stack) {
        triggerAnimation(entity, stack, "fire");

    }

    public void reload(LivingEntity entity, ItemStack stack) {
        triggerAnimation(entity, stack, "reload");

    }
}
