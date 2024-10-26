package net.voxelden.radiationApocalypse.client.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final KeyBinding GUN_FIRE = register("gun_fire", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT);
    public static final KeyBinding GUN_RELOAD = register("gun_reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R);
    public static final KeyBinding GUN_SCOPE = register("gun_scope", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT);

    public static void register() {
    }

    private static KeyBinding register(String name, InputUtil.Type type, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key.radpoc." + name, type, key, "category.radpoc.keybinds"));
    }
}