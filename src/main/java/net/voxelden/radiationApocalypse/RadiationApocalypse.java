package net.voxelden.radiationApocalypse;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.component.Components;
import net.voxelden.radiationApocalypse.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RadiationApocalypse implements ModInitializer {
    public static final String MOD_ID = "radpoc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Burning through the ozone layer...");
        Attributes.register();
        Components.register();
        Items.register();
    }

    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }
}
