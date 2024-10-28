package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.voxelden.radiationApocalypse.animation.AnimationEvent;
import net.voxelden.radiationApocalypse.animation.AnimationEventHolder;
import net.voxelden.radiationApocalypse.client.render.player.PlayerEntityAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements GeoEntity, AnimationEventHolder {
    @Unique
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(getThis());
    @Unique
    private AnimationEvent.Item rightArmEvent;
    @Unique
    private AnimationEvent.Item leftArmEvent;
    @Unique
    private AnimationEvent.Data genericEvent;

    @Unique
    private PlayerEntity getThis() {
        return (PlayerEntity) (Object) this;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        PlayerEntityAnimator.registerControllers(getThis(), controllers);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void setRightArmEvent(AnimationEvent.Item event) {
        rightArmEvent = event;
    }

    @Override
    public AnimationEvent.Item getRightArmEvent() {
        return rightArmEvent;
    }

    @Override
    public void setLeftArmEvent(AnimationEvent.Item event) {
        leftArmEvent = event;
    }

    @Override
    public AnimationEvent.Item getLeftArmEvent() {
        return leftArmEvent;
    }

    @Override
    public void setGenericEvent(AnimationEvent.Data event) {
        genericEvent = event;
    }

    @Override
    public AnimationEvent.Data getGenericEvent() {
        return genericEvent;
    }
}
