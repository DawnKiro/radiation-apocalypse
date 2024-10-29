package net.voxelden.radiationApocalypse.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import net.voxelden.radiationApocalypse.mixin.PostEffectProcessorAccessor;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WorldRenderer {
    public static boolean useCustomRenderer = false;

    public static float fogDensity = 0.2f;
    public static float fogNoiseInfluence = 0.1f;

    private static final Identifier DEFERRED_SHADER = Identifier.ofVanilla("shaders/post/deferred.json");
    private static PostEffectProcessor deferredProcessor;
    private static Framebuffer normalFrameBuffer;

    public static void render(RenderTickCounter tickCounter, Camera camera, Matrix4f modelViewMatrix, Matrix4f projectionMatrix) {
        if (deferredProcessor != null) {
            Matrix4f inverseWorldMat = new Matrix4f(projectionMatrix);
            projectionMatrix.mul(modelViewMatrix, inverseWorldMat).invert();
            Vector3f cameraPos = new Vector3f((float) camera.getPos().x, (float) camera.getPos().y, (float) camera.getPos().z);
            ((PostEffectProcessorAccessor) deferredProcessor).getPasses().forEach(pass -> {
                pass.getProgram().getUniformByNameOrDummy("InvWorldMat").set(inverseWorldMat);
                pass.getProgram().getUniformByNameOrDummy("CameraPos").set(cameraPos);
                pass.getProgram().getUniformByNameOrDummy("CameraBlockPos").set(camera.getBlockPos().getX(), camera.getBlockPos().getY(), camera.getBlockPos().getZ());
                pass.getProgram().getUniformByNameOrDummy("FogDensity").set(fogDensity);
                pass.getProgram().getUniformByNameOrDummy("FogNoiseInfluence").set(fogNoiseInfluence);
            });

            deferredProcessor.render(tickCounter.getLastFrameDuration());
        }
    }

    public static void loadProcessorLayers(MinecraftClient client) {
        try {
            resetProcessorLayers();

            deferredProcessor = new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), DEFERRED_SHADER);
            deferredProcessor.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());

            normalFrameBuffer = deferredProcessor.getSecondaryTarget("normal");

        } catch (Exception e) {
            RadiationApocalypse.LOGGER.error("ERROR LOADING PROCESSOR LAYERS: ", e);
            WorldRenderer.disable();
        }
    }

    public static void resetProcessorLayers() {
        if (deferredProcessor != null) {
            deferredProcessor.close();
            deferredProcessor = null;
        }

        if (normalFrameBuffer != null) {
            normalFrameBuffer.delete();
            normalFrameBuffer = null;
        }
    }

    public static void disable() {
        resetProcessorLayers();

    }

    public static void toggle(MinecraftClient client) {
        useCustomRenderer = !useCustomRenderer;
        if (useCustomRenderer) {
            loadProcessorLayers(client);
        } else {
            disable();
        }
    }
}
