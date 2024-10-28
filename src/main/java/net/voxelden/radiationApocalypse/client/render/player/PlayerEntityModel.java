package net.voxelden.radiationApocalypse.client.render.player;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import software.bernie.geckolib.model.GeoModel;

public class PlayerEntityModel extends GeoModel<AbstractClientPlayerEntity> {
    private static final Identifier MODEL = RadiationApocalypse.id("geo/player.geo.json");
    private static final Identifier MODEL_SLIM = RadiationApocalypse.id("geo/player_slim.geo.json");
    private static final Identifier ANIMATION = RadiationApocalypse.id("animations/player.animation.json");

    @Override
    public Identifier getModelResource(AbstractClientPlayerEntity player) {
        return player.getSkinTextures().model() == SkinTextures.Model.SLIM ? MODEL_SLIM : MODEL;
    }

    @Override
    public Identifier getTextureResource(AbstractClientPlayerEntity player) {
        return player.getSkinTextures().texture();
    }

    @Override
    public Identifier getAnimationResource(AbstractClientPlayerEntity player) {
        return ANIMATION;
    }
}
