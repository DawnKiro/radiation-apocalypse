package net.voxelden.radiationApocalypse.client.render.light;

import net.minecraft.util.math.BlockPos;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LightHolder<T> {
    private final Map<T, List<Light>> lights = new HashMap<>();
    private int count = 0;
    private ByteBuffer buffer;
    private boolean dirty = true;

    public int build() {
        if (!dirty()) return buffer.limit();
        markDirty(false);

        count = 0;
        for (List<Light> tLights : lights.values()) count += tLights.size();

        buffer = ByteBuffer.allocateDirect(count * (4 * Float.BYTES + 4 * Integer.BYTES)).order(ByteOrder.nativeOrder());

        lights.forEach((t, tLights) -> tLights.forEach(light -> {
            Vector3f pos = pos(t).add(light.pos());
            Vector2f rotation = rotation(t).add(light.rotation());
            buffer.putFloat(pos.x());
            buffer.putFloat(pos.y());
            buffer.putFloat(pos.z());
            buffer.putFloat(light.radius());
            buffer.putInt(light.color());
            buffer.putInt(light.type().ordinal());
            buffer.putInt((int) (rotation.x() * 1024f));
            buffer.putInt((int) (rotation.y() * 1024f));
        }));

        return buffer.limit();
    }

    public String getDebugText(String prefix) {
        return prefix + ": " + count() + " (" + buffer().limit() + ") ";
    }

    private void markDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void markDirty() {
        markDirty(true);
    }

    public boolean dirty() {
        return dirty;
    }

    public void clear() {
        markDirty();
        lights.clear();
    }

    public List<Light> get(T t) {
        lights.putIfAbsent(t, new ArrayList<>());
        return lights.get(t);
    }

    public void remove(T t) {
        markDirty();
        lights.remove(t);
    }

    public ByteBuffer buffer() {
        return buffer.rewind();
    }

    public int count() {
        return count;
    }

    abstract Vector3f pos(T of);

    abstract Vector2f rotation(T of);

    public static class Block extends LightHolder<BlockPos> {
        @Override
        public Vector3f pos(BlockPos of) {
            return of.toCenterPos().toVector3f();
        }

        @Override
        Vector2f rotation(BlockPos of) {
            return new Vector2f();
        }
    }

    public static class Entity extends LightHolder<net.minecraft.entity.Entity> {
        @Override
        public Vector3f pos(net.minecraft.entity.Entity of) {
            return of.getPos().toVector3f();
        }

        @Override
        Vector2f rotation(net.minecraft.entity.Entity of) {
            return new Vector2f(of.getPitch(), of.getYaw());
        }

        @Override
        public boolean dirty() {
            return true;
        }
    }
}
