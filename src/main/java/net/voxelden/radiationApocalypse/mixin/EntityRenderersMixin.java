package net.voxelden.radiationApocalypse.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.player.PlayerEntity;
import net.voxelden.radiationApocalypse.client.render.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EntityRenderers.class)
public class EntityRenderersMixin {
    @Unique
    private static final Map<SkinTextures.Model, EntityRendererFactory<AbstractClientPlayerEntity>> CUSTOM_PLAYER_RENDERER_FACTORIES = Map.of(SkinTextures.Model.WIDE, PlayerEntityRenderer::new, SkinTextures.Model.SLIM, PlayerEntityRenderer::new);

    @Inject(method = "reloadPlayerRenderers", at = @At("HEAD"), cancellable = true)
    private static void useCustomPlayerModels(EntityRendererFactory.Context ctx, CallbackInfoReturnable<Map<SkinTextures.Model, EntityRenderer<? extends PlayerEntity>>> cir) {
        ImmutableMap.Builder<SkinTextures.Model, EntityRenderer<? extends PlayerEntity>> builder = ImmutableMap.builder();
        CUSTOM_PLAYER_RENDERER_FACTORIES.forEach((model, factory) -> {
            try {
                builder.put(model, factory.create(ctx));
            } catch (Exception exception) {
                throw new IllegalArgumentException("Failed to create player model for " + model, exception);
            }
        });
        cir.setReturnValue(builder.build());
    }
}
