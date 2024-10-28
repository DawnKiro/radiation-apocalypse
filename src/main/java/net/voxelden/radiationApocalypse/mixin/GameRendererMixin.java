package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.voxelden.radiationApocalypse.client.render.player.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final
    MinecraftClient client;

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
}
