package net.voxelden.radiationApocalypse.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.voxelden.radiationApocalypse.client.keybind.Keybinds;
import net.voxelden.radiationApocalypse.client.render.WorldRenderer;

public class RadiationApocalypseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Keybinds.register();

        ClientTickEvents.END_CLIENT_TICK.register(RadiationApocalypseClient::endTick);
    }

    private static void endTick(MinecraftClient client) {
        while (Keybinds.TOGGLE_CUSTOM_RENDERER.wasPressed()) WorldRenderer.toggle(client);
        while (Keybinds.RELOAD_CUSTOM_RENDERER.wasPressed()) WorldRenderer.loadProcessorLayers(client);
    }
}
