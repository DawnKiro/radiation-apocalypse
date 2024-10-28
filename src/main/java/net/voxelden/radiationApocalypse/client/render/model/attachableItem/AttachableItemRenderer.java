package net.voxelden.radiationApocalypse.client.render.model.attachableItem;

import net.voxelden.radiationApocalypse.item.AttachableItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AttachableItemRenderer extends GeoItemRenderer<AttachableItem> {
    public AttachableItemRenderer() {
        super(new AttachableItemModel());

        AttachmentModel.MODELS.forEach(this::addRenderLayer);
    }

    private void addRenderLayer(GeoModel<AttachableItem> model) {
        addRenderLayer(new AttachmentRenderLayer(this, model));
    }
}
