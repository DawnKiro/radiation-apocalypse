package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CompassAnglePredicateProvider.class)
public class CompassAnglePredicateProviderMixin {
    @Redirect(method = "getAngleTo(Lnet/minecraft/entity/Entity;JLnet/minecraft/util/math/BlockPos;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/item/CompassAnglePredicateProvider;getAngleTo(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;)D"))
    private double pointNorth(CompassAnglePredicateProvider instance, Entity entity, BlockPos pos) {
        return 0.75;
    }
}
