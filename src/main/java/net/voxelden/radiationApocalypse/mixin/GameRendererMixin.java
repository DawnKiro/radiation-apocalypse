package net.voxelden.radiationApocalypse.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.voxelden.radiationApocalypse.client.render.WorldRenderer;
import net.voxelden.radiationApocalypse.client.render.model.player.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    MinecraftClient client;

    @Shadow protected abstract double getFov(Camera camera, float tickDelta, boolean changingFov);

    @Shadow @Final private Camera camera;

    @Inject(method = "render", at = @At("HEAD"))
    private void renderWorld(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        PlayerEntityRenderer.isGui = false;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", ordinal = 0))
    private void renderGui(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        PlayerEntityRenderer.isGui = true;
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1), cancellable = true)
    private void skipVanillaHandDraw(RenderTickCounter tickCounter, CallbackInfo ci) {
        client.getProfiler().pop();
        ci.cancel();
    }

    @Inject(method = "renderWorld", at = @At("HEAD"), cancellable = true)
    private void renderWorld(RenderTickCounter tickCounter, CallbackInfo ci) {
        PlayerEntityRenderer.isGui = false;
        if (WorldRenderer.useCustomRenderer) {
            net.voxelden.radiationApocalypse.client.render.GameRenderer.renderWorld(client, (GameRenderer) (Object) this, tickCounter, getFov(camera, tickCounter.getTickDelta(true), true));
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V"))
    private void renderDebugInfo(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, @Local DrawContext drawContext) {
        if (WorldRenderer.useCustomRenderer) {
            Text debugText = Text.literal("CUSTOM RENDERER IS ACTIVE");
            RenderSystem.enableBlend();
            drawContext.fill(2, 2, 5 + client.textRenderer.getWidth(debugText), 14, ColorHelper.Argb.withAlpha(127, 0));
            RenderSystem.disableBlend();
            drawContext.drawText(client.textRenderer, debugText, 4, 4, 13649145, false);
        }
    }
}
