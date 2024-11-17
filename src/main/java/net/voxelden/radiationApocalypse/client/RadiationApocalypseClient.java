package net.voxelden.radiationApocalypse.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.voxelden.radiationApocalypse.client.render.light.LightManager;
import net.voxelden.radiationApocalypse.input.InputHandler;

public class RadiationApocalypseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        InputHandler.setup();

        ClientTickEvents.END_CLIENT_TICK.register(RadiationApocalypseClient::endTick);

        LightManager.register();
    }

    private static void endTick(MinecraftClient client) {
        InputHandler.tick(client);
    }
}
