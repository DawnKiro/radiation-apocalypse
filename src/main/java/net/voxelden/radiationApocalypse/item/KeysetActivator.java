package net.voxelden.radiationApocalypse.item;

import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.input.Keyset;

public interface KeysetActivator {
    default Identifier keyset() {
        return Keyset.NONE;
    }
}
