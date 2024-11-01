package net.voxelden.radiationApocalypse.client.render.light;

import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

import java.util.UUID;

public abstract class LightPos {
    abstract Vector3f pos();

    public int pitch() {
        return 0;
    }

    public int yaw() {
        return 0;
    }

    public static class Block extends LightPos {
        private final BlockPos block;

        public Block(BlockPos block) {
            this.block = block;
        }

        public BlockPos getBlock() {
            return block;
        }

        @Override
        public Vector3f pos() {
            return block.toCenterPos().toVector3f();
        }
    }

    public static class Entity extends LightPos {
        private final net.minecraft.entity.Entity entity;

        public Entity(net.minecraft.entity.Entity entity) {
            this.entity = entity;
        }

        public net.minecraft.entity.Entity getEntity() {
            return entity;
        }

        @Override
        public Vector3f pos() {
            return entity.getEyePos().toVector3f();
        }

        @Override
        public int pitch() {
            return (int) (entity.getPitch() * 1024f);
        }

        @Override
        public int yaw() {
            return (int) (entity.getYaw() * 1024f);
        }
    }

    public static class Absolute extends LightPos {
        private final UUID uuid;

        public Absolute(UUID uuid) {
            this.uuid = uuid;
        }

        public UUID getUUID() {
            return uuid;
        }

        @Override
        public Vector3f pos() {
            return new Vector3f();
        }
    }
}
