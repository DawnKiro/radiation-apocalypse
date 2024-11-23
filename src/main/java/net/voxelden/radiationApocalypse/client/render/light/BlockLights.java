package net.voxelden.radiationApocalypse.client.render.light;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BlockLights {
    public static final Map<LightBlockPredicate, Supplier<List<Light>>> lights = new HashMap<>();

    private static BlockPredicate.Builder predicate() {
        return BlockPredicate.Builder.create();
    }

    public static List<Light> get(BlockPos blockPos, BlockState state) {
        for (Map.Entry<LightBlockPredicate, Supplier<List<Light>>> light : lights.entrySet()) {
            LightBlockPredicate predicate = light.getKey();
            if (testState(predicate.predicate(), state) && hashify(blockPos) % predicate.rarity == 0) return light.getValue().get();
        }
        return List.of();
    }

    private static int hashify(BlockPos pos) {
        int hash = pos.getX();
        hash = 31 * hash + pos.getY();
        hash = 31 * hash + pos.getZ();
        hash ^= (hash >>> 16);
        hash *= 0x85ebca6b;
        hash ^= (hash >>> 13);
        hash *= 0xc2b2ae35;
        hash ^= (hash >>> 16);
        return hash;
    }

    private static boolean testState(BlockPredicate predicate, BlockState state) {
        return (predicate.blocks().isEmpty() || state.isIn(predicate.blocks().get())) && (predicate.state().isEmpty() || predicate.state().get().test(state));
    }

    public static void register() {
        lights.put(LightBlockPredicate.of(predicate().blocks(Blocks.GLOWSTONE).build()), () -> List.of(new Light(32f, 255, 222, 200, Light.Type.POINT, new Vector3f(0f))));
        lights.put(LightBlockPredicate.of(predicate().blocks(Blocks.LAVA).build(), 32), () -> List.of(new Light(64f, 255, 128, 64, Light.Type.POINT, new Vector3f(0f))));
        lights.put(LightBlockPredicate.of(predicate().blocks(Blocks.SOUL_FIRE).build()), () -> List.of(new Light(24f, 16, 128, 128, Light.Type.POINT, new Vector3f(0f))));
        lights.put(LightBlockPredicate.of(predicate().blocks(Blocks.CAVE_VINES).state(StatePredicate.Builder.create().exactMatch(Properties.BERRIES, true)).build()), () -> List.of(new Light(12f, 160, 128, 64, Light.Type.POINT, new Vector3f(0f))));
        lights.put(LightBlockPredicate.of(predicate().blocks(Blocks.CAVE_VINES_PLANT).state(StatePredicate.Builder.create().exactMatch(Properties.BERRIES, true)).build()), () -> List.of(new Light(12f, 160, 128, 64, Light.Type.POINT, new Vector3f(0f))));
    }

    public record LightBlockPredicate(BlockPredicate predicate, int rarity) {
        public static LightBlockPredicate of(BlockPredicate predicate) {
            return of(predicate, 1);
        }

        public static LightBlockPredicate of(BlockPredicate predicate, int rarity) {
            if (rarity < 1) throw new IllegalArgumentException("Rarity can not be less than 1!");
            return new LightBlockPredicate(predicate, rarity);
        }
    }
}
