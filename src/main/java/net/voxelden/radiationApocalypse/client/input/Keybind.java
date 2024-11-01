package net.voxelden.radiationApocalypse.client.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;

import java.util.function.BiConsumer;

public class Keybind {
    public static final Identifier RENDERER_TOGGLE = RadiationApocalypse.id("renderer_toggle");
    public static final Identifier GUN_FIRE = RadiationApocalypse.id("gun_fire");
    public static final Identifier GUN_RELOAD = RadiationApocalypse.id("gun_reload");
    public static final Identifier GUN_SCOPE = RadiationApocalypse.id("gun_scope");

    private final int key;
    public final BiConsumer<MinecraftClient, Integer> pressAction;
    public final BiConsumer<MinecraftClient, Integer> releaseAction;
    private boolean pressed;
    private long lastAction;

    public Keybind(int key, BiConsumer<MinecraftClient, Integer> pressAction, BiConsumer<MinecraftClient, Integer> releaseAction) {
        this.key = key;
        this.pressAction = pressAction;
        this.releaseAction = releaseAction;
        this.pressed = false;
    }

    public void tick(MinecraftClient client) {

    }

    public int key() {
        return key;
    }

    public boolean isPressed() {
        return pressed;
    }
}
