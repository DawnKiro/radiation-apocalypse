package net.voxelden.radiationApocalypse.client.render.model.attachableItem;

import net.voxelden.radiationApocalypse.client.render.model.SimplePathNamedModel;
import net.voxelden.radiationApocalypse.item.AttachableItem;
import software.bernie.geckolib.model.GeoModel;

import java.util.List;

public class AttachmentModel extends SimplePathNamedModel<AttachableItem> {
    public static final List<GeoModel<AttachableItem>> MODELS = List.of(
            new AttachmentModel("barrel"),
            new AttachmentModel("scope")
    );

    private final String model;

    private AttachmentModel(String model) {
        super("attachment");
        this.model = model;
    }

    @Override
    protected String getModel(AttachableItem attachableItem) {
        return model;
    }
}
