package net.voxelden.radiationApocalypse.client.render.model.attachableItem;

import net.voxelden.radiationApocalypse.client.render.model.SimplePathNamedModel;
import net.voxelden.radiationApocalypse.item.AttachableItem;

public class AttachableItemModel extends SimplePathNamedModel<AttachableItem> {
    public AttachableItemModel() {
        super("attachable");
    }

    @Override
    protected String getModel(AttachableItem item) {
        return item.getModel();
    }
}
