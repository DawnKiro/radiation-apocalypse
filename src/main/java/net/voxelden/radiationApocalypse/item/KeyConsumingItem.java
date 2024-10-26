package net.voxelden.radiationApocalypse.item;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class KeyConsumingItem extends Item {
    public KeyConsumingItem(Settings settings) {
        super(settings);
    }

    abstract public boolean consumeKeys(ClientPlayerEntity user, ItemStack stack);
}
