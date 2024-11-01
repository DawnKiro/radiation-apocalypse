package net.voxelden.radiationApocalypse.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPhase.Texture.class)
public class RenderPhaseTextureMixin {
    @Inject(method = "method_23563", at = @At("TAIL"))
    private static void addDataTexture(Identifier id, boolean blur, boolean mipmap, CallbackInfo ci, @Local TextureManager textureManager) {
        try {
            //Identifier dataId = WorldRenderer.getDataTexture(id);
            //textureManager.getTexture(dataId).setFilter(blur, mipmap);
            //RenderSystem.setShaderTexture(1, dataId);
        } catch (Exception ignored) {

        }
    }
}
