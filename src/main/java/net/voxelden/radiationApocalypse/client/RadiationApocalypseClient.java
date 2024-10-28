package net.voxelden.radiationApocalypse.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.voxelden.radiationApocalypse.client.keybind.Keybinds;

public class RadiationApocalypseClient implements ClientModInitializer {
    public static boolean useCustomRenderer = false;

    @Override
    public void onInitializeClient() {
        Keybinds.register();

        ClientTickEvents.END_CLIENT_TICK.register(RadiationApocalypseClient::endTick);
    }

    private static void endTick(MinecraftClient client) {
        while (Keybinds.TOGGLE_CUSTOM_RENDERER.wasPressed()) useCustomRenderer = !useCustomRenderer;
    }
}
