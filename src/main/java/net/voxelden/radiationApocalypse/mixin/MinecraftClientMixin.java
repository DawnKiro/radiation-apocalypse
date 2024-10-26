package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.voxelden.radiationApocalypse.client.InputHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Unique
    private MinecraftClient getThis() {
        return (MinecraftClient) (Object) this;
    }

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void handleCustomInput(CallbackInfo ci) {
        InputHandler.setClient(getThis());
        if (player != null) InputHandler.handle(player);
    }
}
