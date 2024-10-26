package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Inject(method = "renderArm", at = @At("HEAD"), cancellable = true)
    private void cancelFirstPersonArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderArmHoldingItem", at = @At("HEAD"), cancellable = true)
    private void cancelFirstPersonArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm, CallbackInfo ci) {
        ci.cancel();
    }
}
