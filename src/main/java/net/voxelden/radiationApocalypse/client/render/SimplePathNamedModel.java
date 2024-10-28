package net.voxelden.radiationApocalypse.client.render;

import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public abstract class SimplePathNamedModel<T extends GeoAnimatable> extends GeoModel<T> {
    private final String path;

    public SimplePathNamedModel(String path) {
        this.path = path;
    }

    protected abstract String getModel(T t);

    @Override
    public Identifier getModelResource(T t) {
        return RadiationApocalypse.id("geo/" + path + "/" + getModel(t) + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(T t) {
        return RadiationApocalypse.id("textures/" + path + "/" + getModel(t) + ".png");
    }

    @Override
    public Identifier getAnimationResource(T t) {
        return RadiationApocalypse.id("animations/" + path + "/" + getModel(t) + ".animation.json");
    }
}
