package net.voxelden.radiationApocalypse.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class GameRenderer {
    public static void renderWorld(MinecraftClient client, net.minecraft.client.render.GameRenderer renderer, RenderTickCounter tickCounter, double fov) {
        if (client.world == null) return;

        float f = tickCounter.getTickDelta(true);

        client.getProfiler().push("camera");
        if (client.getCameraEntity() == null) {
            client.setCameraEntity(client.player);
        }
        renderer.updateCrosshairTarget(f);
        Camera camera = renderer.getCamera();
        Entity entity = client.getCameraEntity() == null ? client.player : client.getCameraEntity();
        camera.update(client.world, entity, false, false, client.world.getTickManager().shouldSkipTick(entity) ? 1f : f);

        client.getProfiler().swap("render");

        Matrix4f projectionMatrix = renderer.getBasicProjectionMatrix(fov);
        Matrix4f modelViewMatrix = new Matrix4f().rotation(camera.getRotation().conjugate(new Quaternionf()));

        renderer.loadProjectionMatrix(projectionMatrix);
        client.worldRenderer.setupFrustum(camera.getPos(), modelViewMatrix, projectionMatrix);
        client.worldRenderer.render(tickCounter, false, camera, renderer, client.gameRenderer.getLightmapTextureManager(), modelViewMatrix, projectionMatrix);

        client.getProfiler().pop();
    }
}
