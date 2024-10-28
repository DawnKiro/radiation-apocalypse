package net.voxelden.radiationApocalypse.item;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public interface KeyConsumingItem {
    boolean consumeKeys(ClientPlayerEntity user, ItemStack stack);
}
