package net.voxelden.radiationApocalypse.client.render.light;

import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.voxelden.radiationApocalypse.client.render.SSBO;

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

    public static void update() {
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

        int size = 0;
        for (LightHolder<?> lightHolder : lightHolders) size += lightHolder.build();
        ByteBuffer buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).rewind();
        for (LightHolder<?> lightHolder : lightHolders) {
            buffer.put(lightHolder.buffer());
        }
        lightBuffer.update(buffer);
    }

    /**
     * Use this method when adding block lights from threads outside the main render thread.
     * @param blockPos The position of the light to be added.
     * @param blockState The BlockState of the block, will be used to deduce what lights to apply.
     */
    public static void queueBlockLight(BlockPos blockPos, BlockState blockState) {
        blockLightQueue.offer(new QueuedLight.Block(blockPos, BlockLights.get(blockPos, blockState)));
    }
}
