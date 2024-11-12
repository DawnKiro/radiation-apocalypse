package net.voxelden.radiationApocalypse.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import net.voxelden.radiationApocalypse.client.render.WorldRenderer;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InputHandler {
    public static final List<Keybind.Basic> BASIC_KEYBINDS = List.of(registerBasic("renderer_toggle", GLFW.GLFW_KEY_Y, KeyBinding.MISC_CATEGORY, WorldRenderer::toggle));

    public static final Map<Identifier, Keyset> keysets = new HashMap<>();

    public static void setup() {
        keysets.put(Keyset.GUN, new Keyset(Map.of(Keybind.GUN_FIRE, new Keybind(GLFW.GLFW_MOUSE_BUTTON_LEFT, (client, modifiers) -> {
            log("gun_fire", true, modifiers);
        }, (client, modifiers) -> {
            log("gun_fire", false, modifiers);
        }), Keybind.GUN_RELOAD, new Keybind(GLFW.GLFW_KEY_R, (client, modifiers) -> {
            log("gun_reload", true, modifiers);
        }, (client, modifiers) -> {
            log("gun_reload", false, modifiers);
        }), Keybind.GUN_SCOPE, new Keybind(GLFW.GLFW_MOUSE_BUTTON_RIGHT, (client, modifiers) -> {
            log("gun_scope", true, modifiers);
        }, (client, modifiers) -> {
            log("gun_scope", false, modifiers);
        }))));
    }

    public static void tick(MinecraftClient client) {
        BASIC_KEYBINDS.forEach(basicKeybind -> {
            while (basicKeybind.keyBinding().wasPressed()) basicKeybind.action().accept(client);
        });
    }

    public static boolean keyboardInput(MinecraftClient client, int key, int action, int modifiers) {
        if (Keyset.active == null) return false;
        return Keyset.active.handle(client, key, action, modifiers);
    }

    private static void log(String action, boolean pressed, int modifiers) {
        RadiationApocalypse.LOGGER.info("{}:{}:{}", action, pressed, modifiers);
    }

    private static Keybind.Basic registerBasic(String name, int key, String category, Consumer<MinecraftClient> action) {
        return new Keybind.Basic(KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + RadiationApocalypse.MOD_ID + "." + name, InputUtil.Type.KEYSYM, key, category)), action);
    }
}
