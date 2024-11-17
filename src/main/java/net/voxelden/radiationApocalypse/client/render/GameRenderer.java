package net.voxelden.radiationApocalypse.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import net.voxelden.radiationApocalypse.client.render.light.LightManager;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;

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

    public static void renderDebugText(TextRenderer textRenderer, DrawContext drawContext) {
        List<Text> debugText = List.of(
                Text.literal("CUSTOM RENDERER IS ACTIVE").formatted(Formatting.LIGHT_PURPLE),
                LightManager.getDebugText()
        );

        int offset = 2;
        RenderSystem.enableBlend();
        for (Text text : debugText) {
            int height = textRenderer.getWrappedLinesHeight(text, 128) + 4;
            drawContext.fill(2, offset, 133, height, ColorHelper.Argb.withAlpha(127, 0));
            drawContext.drawTextWrapped(textRenderer, text, 4, offset + 2, 128, -1);
            offset += height + 2;
        }
        RenderSystem.disableBlend();
    }
}
