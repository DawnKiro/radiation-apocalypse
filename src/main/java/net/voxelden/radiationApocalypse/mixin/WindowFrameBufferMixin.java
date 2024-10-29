package net.voxelden.radiationApocalypse.mixin;

import com.mojang.blaze3d.platform.GlConst;
import net.minecraft.client.gl.WindowFramebuffer;
import net.voxelden.radiationApocalypse.client.render.WorldRenderer;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WindowFramebuffer.class)
public class WindowFrameBufferMixin {
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_bindTexture(I)V", ordinal = 2))
    private void initNormal(int width, int height, CallbackInfo ci) {
        GL30.glDrawBuffers(new int[]{GlConst.GL_COLOR_ATTACHMENT0, WorldRenderer.GL_COLOR_ATTACHMENT1});
    }
}
