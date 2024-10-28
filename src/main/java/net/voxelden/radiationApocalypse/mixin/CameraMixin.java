package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.voxelden.radiationApocalypse.client.render.player.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    protected abstract void setPos(Vec3d pos);

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 0), cancellable = true)
    private void updateCamera(BlockView area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayerEntity player && PlayerEntityRenderer.cameraPosition != null) {
            setPos(PlayerEntityRenderer.cameraPosition.add(MathHelper.lerp(tickDelta, player.prevX, player.getX()), MathHelper.lerp(tickDelta, player.prevY, player.getY()), MathHelper.lerp(tickDelta, player.prevZ, player.getZ())));
            setRotation(player.getYaw(tickDelta) + PlayerEntityRenderer.cameraRotation.y, player.getPitch(tickDelta) + PlayerEntityRenderer.cameraRotation.x);
            ci.cancel();
        }
    }
}
