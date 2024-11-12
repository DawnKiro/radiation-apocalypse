package net.voxelden.radiationApocalypse.item;

import net.voxelden.radiationApocalypse.component.Components;
import net.voxelden.radiationApocalypse.component.WeaponAttachmentsComponent;

public abstract class WeaponItem extends AttachableItem {
    public WeaponItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public String getModel() {
        WeaponAttachmentsComponent attachmentsComponent = getComponents().get(Components.WEAPON_ATTACHMENTS);
        return attachmentsComponent == null ? "null" : attachmentsComponent.model();
    }
}
