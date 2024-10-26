package net.voxelden.radiationApocalypse.client;

import net.fabricmc.api.ClientModInitializer;
import net.voxelden.radiationApocalypse.client.keybind.Keybinds;

public class RadiationApocalypseClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Keybinds.register();
    }
}
