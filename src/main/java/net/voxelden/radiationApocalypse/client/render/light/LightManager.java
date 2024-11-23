package net.voxelden.radiationApocalypse.client.render.light;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.voxelden.radiationApocalypse.client.render.SSBO;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LightManager {
    public static final LightHolder.Block blockLights = new LightHolder.Block();
    public static final LightHolder.Entity entityLights = new LightHolder.Entity();
    private static final ConcurrentLinkedQueue<QueuedLight.Block> blockLightQueue = new ConcurrentLinkedQueue<>();
    private static final List<LightHolder<?>> lightHolders = List.of(blockLights, entityLights);
    private static SSBO lightBuffer;

    public static void register() {
        BlockLights.register();
    }

    public static Text getDebugText() {
        return Text.literal(blockLights.getDebugText("B") + entityLights.getDebugText("E")).formatted(Formatting.AQUA);
    }

    public static void clear() {
        lightHolders.forEach(LightHolder::clear);
    }

    public static void setup() {
        lightBuffer = new SSBO(0);
    }

    public static void destroy() {
        if (lightBuffer != null) {
            lightBuffer.delete();
            lightBuffer = null;
        }
    }

    public static void update(MinecraftClient client, Camera camera) {
        while (!blockLightQueue.isEmpty()) {
            QueuedLight.Block queuedLight = blockLightQueue.poll();
            if (queuedLight.lights().isEmpty()) {
                blockLights.remove(queuedLight.t());
            } else {
                List<Light> blockLight = blockLights.get(queuedLight.t());
                blockLight.clear();
                blockLight.addAll(queuedLight.lights());
                blockLights.markDirty();
            }
        }

        float chunkRadius = (float) client.options.getViewDistance().getValue() * 16;
        blockLights.get().entrySet().removeIf(entry -> {
            Vector3f pos = blockLights.pos(entry.getKey());
            float radius = 0;
            for (Light light : entry.getValue()) {
                radius = Math.max(radius, light.radius());
            }
            radius += chunkRadius;
            return camera.getPos().squaredDistanceTo(pos.x(), pos.y(), pos.z()) > radius * radius;
        });

        int size = 0;
        for (LightHolder<?> lightHolder : lightHolders) size += lightHolder.build();
        ByteBuffer buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).rewind();
        lightHolders.forEach(lightHolder -> buffer.put(lightHolder.buffer()));
        lightBuffer.update(buffer);
    }

    /**
     * Use this method when adding block lights from threads outside the main render thread.
     *
     * @param blockPos The position of the light to be added.
     * @param lights   The lights of the block to apply.
     */
    public static void queueBlockLight(BlockPos blockPos, List<Light> lights) {
        blockLightQueue.offer(new QueuedLight.Block(blockPos, lights));
    }
}
