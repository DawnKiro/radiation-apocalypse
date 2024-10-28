package net.voxelden.radiationApocalypse.client.render.model.attachableItem;

import net.voxelden.radiationApocalypse.item.AttachableItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class AttachmentRenderLayer extends GeoRenderLayer<AttachableItem> {
    private final GeoModel<AttachableItem> model;

    public AttachmentRenderLayer(GeoRenderer<AttachableItem> entityRendererIn, GeoModel<AttachableItem> model) {
        super(entityRendererIn);
        this.model = model;
    }

    @Override
    public GeoModel<AttachableItem> getGeoModel() {
        return model;
    }
}
