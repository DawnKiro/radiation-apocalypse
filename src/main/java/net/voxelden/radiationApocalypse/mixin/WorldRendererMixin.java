package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    protected abstract void resetTransparencyPostProcessor();

    @Shadow
    @Final
    private MinecraftClient client;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;isThirdPerson()Z"))
    private boolean renderSelf(Camera camera) {
        return true;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/debug/DebugRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V"))
    private void deferredPass(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        net.voxelden.radiationApocalypse.client.render.WorldRenderer.render(tickCounter, camera, matrix4f, matrix4f2);
    }

    @Inject(method = "loadTransparencyPostProcessor", at = @At("HEAD"), cancellable = true)
    private void loadProcessorLayers(CallbackInfo ci) {
        if (net.voxelden.radiationApocalypse.client.render.WorldRenderer.useCustomRenderer) {
            resetTransparencyPostProcessor();
            net.voxelden.radiationApocalypse.client.render.WorldRenderer.loadProcessorLayers(client);
            ci.cancel();
        }
    }

    @Inject(method = "onResized", at = @At("TAIL"))
    private void resizeProcessorLayers(int width, int height, CallbackInfo ci) {
        if (net.voxelden.radiationApocalypse.client.render.WorldRenderer.useCustomRenderer) {
            net.voxelden.radiationApocalypse.client.render.WorldRenderer.resizeProcessorLayers(width, height);
        }
    }
}
