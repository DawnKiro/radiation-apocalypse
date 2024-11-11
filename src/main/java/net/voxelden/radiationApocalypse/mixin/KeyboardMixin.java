package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.voxelden.radiationApocalypse.client.input.InputHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void redirectKeys(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (window == client.getWindow().getHandle() && InputHandler.keyboardInput(client, key, action, modifiers)) ci.cancel();
    }
}
