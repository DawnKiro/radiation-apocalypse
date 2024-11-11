package net.voxelden.radiationApocalypse.client.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record Keybind(int key, BiConsumer<MinecraftClient, Integer> pressAction,
                      BiConsumer<MinecraftClient, Integer> releaseAction) {
    public static final Identifier GUN_FIRE = RadiationApocalypse.id("gun_fire");
    public static final Identifier GUN_RELOAD = RadiationApocalypse.id("gun_reload");
    public static final Identifier GUN_SCOPE = RadiationApocalypse.id("gun_scope");

    public record Basic(KeyBinding keyBinding, Consumer<MinecraftClient> action) {
    }
}
