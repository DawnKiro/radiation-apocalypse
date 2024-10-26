package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.voxelden.radiationApocalypse.client.render.PlayerEntityAnimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements GeoEntity {
    @Unique
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(getThis());

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
}
