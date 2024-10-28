package net.voxelden.radiationApocalypse.client.render.attachableItem;

import net.voxelden.radiationApocalypse.client.render.SimplePathNamedModel;
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
