package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.voxelden.radiationApocalypse.animation.AnimationEventHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract Arm getMainArm();

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Unique
    private LivingEntity getThis() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At("HEAD"))
    private void swingHand(Hand hand, boolean fromServerPlayer, CallbackInfo ci) {
        if (getThis() instanceof AnimationEventHolder eventHolder) {
            eventHolder.put("swing", hand, getMainArm(), getStackInHand(hand));
        }
    }

    @Inject(method = "onDamaged", at = @At("HEAD"))
    private void onDamaged(DamageSource damageSource, CallbackInfo ci) {
        if (getThis() instanceof AnimationEventHolder eventHolder) {
            eventHolder.put("swing", damageSource.getTypeRegistryEntry().getIdAsString());
        }
    }
}
