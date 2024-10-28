package net.voxelden.radiationApocalypse.animation;

import net.minecraft.item.ItemStack;

public class AnimationEvent {
    private final String type;

    public AnimationEvent(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    public static class Data extends AnimationEvent {
        private final String data;

        public Data(String type, String data) {
            super(type);
            this.data = data;
        }

        public String data() {
            return data;
        }
    }

    public static class Item extends AnimationEvent {
        private final ItemStack item;

        public Item(String type, ItemStack item) {
            super(type);
            this.item = item;
        }

        public ItemStack item() {
            return item;
        }
    }
}
