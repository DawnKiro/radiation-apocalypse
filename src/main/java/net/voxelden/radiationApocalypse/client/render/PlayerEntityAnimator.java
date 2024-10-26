package net.voxelden.radiationApocalypse.client.render;

import net.minecraft.entity.player.PlayerEntity;
import software.bernie.geckolib.animation.*;

public class PlayerEntityAnimator {
    public static void registerControllers(PlayerEntity player, AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(player, "base", 5, event -> event.setAndContinue(PlayerAnimation.BASE)));
        controllers.add(new AnimationController<>(player, "movement", 5, PlayerEntityAnimator::movementAnimationController));
    }

    protected static PlayState movementAnimationController(final AnimationState<PlayerEntity> event) {
        if (event.isMoving()) {}
        else
            return event.setAndContinue(PlayerAnimation.IDLE);

        return PlayState.STOP;
    }

    public static class PlayerAnimation {
        public static final RawAnimation BASE = RawAnimation.begin().thenLoop("base");
        public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    }
}
