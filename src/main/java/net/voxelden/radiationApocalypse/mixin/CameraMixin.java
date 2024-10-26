package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.voxelden.radiationApocalypse.client.render.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Shadow
    protected abstract void setPos(Vec3d pos);

    @Shadow
    private float yaw;

    @Shadow
    private float pitch;

    @Inject(method = "update", at = @At("TAIL"))
    private void updateCamera(BlockView area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (entity instanceof AbstractClientPlayerEntity) {
            if (PlayerEntityRenderer.cameraPosition != null)
                setPos(PlayerEntityRenderer.cameraPosition.add(MathHelper.lerp(tickDelta, entity.prevX, entity.getX()), MathHelper.lerp(tickDelta, entity.prevY, entity.getY()), MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ())));
            setRotation(yaw + PlayerEntityRenderer.cameraRotation.y, pitch + PlayerEntityRenderer.cameraRotation.x);
        }
    }
}