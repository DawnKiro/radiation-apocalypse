package net.voxelden.radiationApocalypse.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.voxelden.radiationApocalypse.item.KeyConsumingItem;

public class InputHandler {
    private static MinecraftClient client;

    public static void handle(ClientPlayerEntity player) {
        for (ItemStack handItem : player.getHandItems()) {
            if (handItem.getItem() instanceof KeyConsumingItem item && item.consumeKeys(player, handItem)) break;
        }
    }

    public static boolean consumeKey(KeyBinding key) {
        boolean pressed = key.isPressed();
        for (KeyBinding keybind : client.options.allKeys)
            if (keybind.equals(key)) while (keybind.wasPressed()) {
            }
        return pressed;
    }

    public static void setClient(MinecraftClient minecraftClient) {
        client = minecraftClient;
    }
}
