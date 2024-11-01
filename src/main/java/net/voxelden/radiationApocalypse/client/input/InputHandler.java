package net.voxelden.radiationApocalypse.client.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import net.voxelden.radiationApocalypse.client.render.WorldRenderer;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class InputHandler {
    public static final Map<Identifier, Keybind> keybinds = new HashMap<>();

    public static void setup() {
        keybinds.put(Keybind.RENDERER_TOGGLE, new Keybind(GLFW.GLFW_KEY_Y, (client, modifiers) -> {
        }, (client, modifiers) -> {
            WorldRenderer.toggle(client);

            //WorldRenderer.loadProcessorLayers(client);
        }));
        keybinds.put(Keybind.GUN_FIRE, new Keybind(GLFW.GLFW_MOUSE_BUTTON_LEFT, (client, modifiers) -> {
        }, (client, modifiers) -> {
        }));
        keybinds.put(Keybind.GUN_RELOAD, new Keybind(GLFW.GLFW_KEY_R, (client, modifiers) -> {
        }, (client, modifiers) -> {
        }));
        keybinds.put(Keybind.GUN_SCOPE, new Keybind(GLFW.GLFW_MOUSE_BUTTON_RIGHT, (client, modifiers) -> {
        }, (client, modifiers) -> {
        }));
    }

    public static void tick(MinecraftClient client) {
        keybinds.forEach((identifier, keybind) -> keybind.tick(client));
    }

    public static void keyboardInput(MinecraftClient client, int key, int action, int modifiers) {
        keybinds.forEach((identifier, keybind) -> {
            if (key == keybind.key()) {
                if (action == 0) keybind.pressAction.accept(client, modifiers);
                else if (action == 1) keybind.releaseAction.accept(client, modifiers);
            }
        });

        RadiationApocalypse.LOGGER.info("{}:{}:{}", key, action, modifiers);
    }
}
