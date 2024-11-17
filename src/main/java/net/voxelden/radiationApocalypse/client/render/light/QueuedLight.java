package net.voxelden.radiationApocalypse.client.render.light;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public abstract class QueuedLight<T> {
    private final T t;
    private final List<Light> lights;

    public QueuedLight(T t, List<Light> lights) {
        this.t = t;
        this.lights = lights;
    }

    public T t() {
        return t;
    }

    public List<Light> lights() {
        return lights;
    }

    public static class Block extends QueuedLight<BlockPos> {
        public Block(BlockPos blockPos, List<Light> lights) {
            super(blockPos, lights);
        }
    }
}
