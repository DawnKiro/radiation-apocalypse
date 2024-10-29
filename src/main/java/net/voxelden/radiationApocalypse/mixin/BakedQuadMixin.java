package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.render.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BakedQuad.class)
public class BakedQuadMixin {
    @Inject(method = "hasShade", at = @At("HEAD"), cancellable = true)
    private void noShade(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
