package net.voxelden.radiationApocalypse.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

public record Keyset(Map<Identifier, Keybind> keybinds) {
    public static Keyset active = null;

    public static final Identifier NONE = RadiationApocalypse.id("none");
    public static final Identifier GUN = RadiationApocalypse.id("gun");

    public static void activate(Identifier keyset) {
        active = InputHandler.keysets.getOrDefault(keyset, null);
    }

    public boolean handle(MinecraftClient client, int key, int action, int modifiers) {
        for (Map.Entry<Identifier, Keybind> entry : keybinds.entrySet()) {
            Keybind keybind = entry.getValue();
            if (key == keybind.key()) {
                if (action == GLFW.GLFW_PRESS) keybind.pressAction().accept(client, modifiers);
                else if (action == GLFW.GLFW_RELEASE) keybind.releaseAction().accept(client, modifiers);
                return true;
            }
        }
        return false;
    }
}
