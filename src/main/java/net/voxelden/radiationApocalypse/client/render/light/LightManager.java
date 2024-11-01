package net.voxelden.radiationApocalypse.client.render.light;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import net.voxelden.radiationApocalypse.client.render.SSBO;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class LightManager {
    private static final HashMap<Light, LightPos> lights = new HashMap<>();
    private static SSBO shaderBuffer;
    private static boolean needsUpdate = false;

    public static void add(Light light, LightPos pos) {
        lights.put(light, pos);
        needsUpdate = true;
    }

    public static void add(Light light, BlockPos block) {
        add(light, new LightPos.Block(block));
    }

    public static void add(Light light, Entity entity) {
        add(light, new LightPos.Entity(entity));
    }

    public static void add(Light light, UUID uuid) {
        add(light, new LightPos.Absolute(uuid));
    }

    private static void remove(Predicate<LightPos> predicate) {
        needsUpdate = true;
        lights.values().removeIf(predicate);
    }

    public static void remove(Light light) {
        needsUpdate = true;
        lights.remove(light);
    }

    public static void remove(LightPos pos) {
        remove(pos::equals);
    }

    public static void remove(BlockPos block) {
        remove(value -> value instanceof LightPos.Block attachedBlock && block.equals(attachedBlock.getBlock()));
    }

    public static void remove(Entity entity) {
        remove(value -> value instanceof LightPos.Entity attachedEntity && entity.equals(attachedEntity.getEntity()));
    }

    public static void remove(UUID uuid) {
        remove(value -> value instanceof LightPos.Absolute absolute && uuid.equals(absolute.getUUID()));
    }

    public static List<Light> getLight(Predicate<Map.Entry<Light, LightPos>> predicate) {
        return lights.entrySet().stream().filter(predicate).map(Map.Entry::getKey).toList();
    }

    public static LightPos getPos(Light light) {
        return lights.get(light);
    }

    public static List<Light> getLight(LightPos pos) {
        return getLight(entry -> pos.equals(entry.getValue()));
    }

    public static List<Light> getLight(BlockPos block) {
        return getLight(entry -> entry.getValue() instanceof LightPos.Block attachedBlock && block.equals(attachedBlock.getBlock()));
    }

    public static List<Light> getLight(Entity entity) {
        return getLight(entry -> entry.getValue() instanceof LightPos.Entity attachedEntity && entity.equals(attachedEntity.getEntity()));
    }

    public static List<Light> getLight(UUID uuid) {
        return getLight(entry -> entry.getValue() instanceof LightPos.Absolute absolute && uuid.equals(absolute.getUUID()));
    }

    public static void clear() {
        needsUpdate = true;
        lights.clear();
    }

    public static void update() {
        if (needsUpdate) {
            ByteBuffer buffer = ByteBuffer.allocate(lights.size() * (4 * Float.BYTES + 4 * Integer.BYTES) + 4 * Integer.BYTES);
            buffer.putInt(lights.size());
            buffer.putInt(lights.size());
            buffer.putInt(lights.size());
            buffer.putInt(lights.size());

            lights.forEach((light, pos) -> {
                buffer.putFloat(pos.pos().x + light.pos().x);
                buffer.putFloat(pos.pos().y + light.pos().y);
                buffer.putFloat(pos.pos().z + light.pos().x);
                buffer.putFloat(light.radius());
                buffer.putInt(light.color());
                buffer.putInt(light.type().ordinal());
                buffer.putInt(pos.pitch() + light.pitch());
                buffer.putInt(pos.yaw() + light.yaw());
            });

            RadiationApocalypse.LOGGER.info("LIGHTS = {}, BUFFER = {}", lights.size(), buffer.limit());
            //buffer.flip();
            shaderBuffer.update(buffer);
            needsUpdate = false;
        }
    }

    public static void setup() {
        shaderBuffer = new SSBO();
        shaderBuffer.bind(0);
    }

    public static void destroy() {
        if (shaderBuffer != null) {
            shaderBuffer.delete();
            shaderBuffer = null;
        }
    }
}
