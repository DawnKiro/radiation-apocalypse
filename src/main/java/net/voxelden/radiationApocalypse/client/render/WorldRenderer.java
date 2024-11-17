package net.voxelden.radiationApocalypse.client.render;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import net.voxelden.radiationApocalypse.client.render.light.Light;
import net.voxelden.radiationApocalypse.client.render.light.LightManager;
import net.voxelden.radiationApocalypse.mixin.PostEffectProcessorAccessor;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

public class WorldRenderer {
    public static boolean useCustomRenderer = false;

    public static float fogDensity = 0.2f;
    public static float fogNoiseInfluence = 0.1f;

    private static final Identifier DEFERRED_SHADER = Identifier.ofVanilla("shaders/post/deferred.json");
    private static PostEffectProcessor deferredProcessor;
    public static Framebuffer normalFrameBuffer;
    public static int normalAttachment = -1;
    public static Framebuffer dataFrameBuffer;
    public static int dataAttachment = -1;
    public static final int GL_COLOR_ATTACHMENT1 = GlConst.GL_COLOR_ATTACHMENT0 + 1;
    public static final int GL_COLOR_ATTACHMENT2 = GL_COLOR_ATTACHMENT1 + 1;

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

            LightManager.update();
            deferredProcessor.render(tickCounter.getLastFrameDuration());
        }
    }

    public static void loadProcessorLayers(MinecraftClient client) {
        try {
            resetProcessorLayers();

            deferredProcessor = new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), DEFERRED_SHADER);
            deferredProcessor.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());

            normalFrameBuffer = deferredProcessor.getSecondaryTarget("normal");
            normalAttachment = normalFrameBuffer.getColorAttachment();
            dataFrameBuffer = deferredProcessor.getSecondaryTarget("data");
            dataAttachment = dataFrameBuffer.getColorAttachment();

            GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, client.getFramebuffer().fbo);

            GlStateManager._bindTexture(WorldRenderer.normalAttachment);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MIN_FILTER, GlConst.GL_NEAREST);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MAG_FILTER, GlConst.GL_NEAREST);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_WRAP_S, GlConst.GL_CLAMP_TO_EDGE);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_WRAP_T, GlConst.GL_CLAMP_TO_EDGE);
            GlStateManager._glFramebufferTexture2D(GlConst.GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GlConst.GL_TEXTURE_2D, WorldRenderer.normalAttachment, 0);

            GlStateManager._bindTexture(WorldRenderer.dataAttachment);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MIN_FILTER, GlConst.GL_NEAREST);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MAG_FILTER, GlConst.GL_NEAREST);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_WRAP_S, GlConst.GL_CLAMP_TO_EDGE);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_WRAP_T, GlConst.GL_CLAMP_TO_EDGE);
            GlStateManager._glFramebufferTexture2D(GlConst.GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2, GlConst.GL_TEXTURE_2D, WorldRenderer.dataAttachment, 0);

            GL33.glDrawBuffers(new int[]{GlConst.GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2});
            GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);

            LightManager.setup();
            LightManager.entityLights.get(client.player).add(new Light(4f, 255, 0, 0, Light.Type.POINT));
            LightManager.blockLights.get(BlockPos.ORIGIN).add(new Light(16f, 255, 127, 255, Light.Type.POINT));
        } catch (Exception e) {
            if (client.player == null) {
                RadiationApocalypse.LOGGER.info("ERROR LOADING PROCESSOR LAYERS: ", e);
            } else {
                client.player.sendMessage(Text.literal("ERROR LOADING PROCESSOR LAYERS: ").append(Text.literal(e.getMessage()).setStyle(Style.EMPTY.withColor(Formatting.RED))));
            }
            resetProcessorLayers();
            useCustomRenderer = false;
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
            normalAttachment = -1;
        }

        if (dataFrameBuffer != null) {
            dataFrameBuffer.delete();
            dataFrameBuffer = null;
            dataAttachment = -1;
        }

        LightManager.clear();
        LightManager.destroy();
    }

    public static void resizeProcessorLayers(int width, int height) {
        if (deferredProcessor != null) {
            deferredProcessor.setupDimensions(width, height);
        }
    }

    public static void toggle(MinecraftClient client) {
        useCustomRenderer = !useCustomRenderer;
        if (useCustomRenderer) {
            loadProcessorLayers(client);
        } else {
            resetProcessorLayers();
        }
    }

    public static Identifier getDataTexture(Identifier id) {
        String basePath = id.getPath();
        int index = basePath.lastIndexOf(".png");
        if (index == -1) {
            return id;
        } else {
            return Identifier.of(id.getNamespace(), basePath.substring(0, index) + "_.png");
        }
    }
}
