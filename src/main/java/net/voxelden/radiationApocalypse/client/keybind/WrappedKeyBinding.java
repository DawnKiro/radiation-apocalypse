package net.voxelden.radiationApocalypse.client.keybind;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.function.Consumer;

public record WrappedKeyBinding(KeyBinding keyBinding, Consumer<MinecraftClient> onPressed) {
}
