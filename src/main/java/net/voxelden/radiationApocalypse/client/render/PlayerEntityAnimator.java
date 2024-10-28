package net.voxelden.radiationApocalypse.client.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.voxelden.radiationApocalypse.animation.AnimationEvent;
import net.voxelden.radiationApocalypse.component.AnimationTypeComponent;
import net.voxelden.radiationApocalypse.component.Components;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static net.voxelden.radiationApocalypse.animation.AnimationEventHolder.getArmForHand;

public class PlayerEntityAnimator {
    private static final RawAnimation CONSTANT = RawAnimation.begin().thenLoop("constant");
    private static final Map<Function<PlayerEntity, Boolean>, String> MOVEMENT_MODIFIERS = Map.of(Entity::isCrawling, "crawl", Entity::isSneaky, "sneak");

    public static void registerControllers(PlayerEntity player, AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(player, "constant", 10, event -> event.setAndContinue(CONSTANT)));
        controllers.add(new AnimationController<>(player, "base", 10, PlayerEntityAnimator::movementAnimationController));
        controllers.add(new AnimationController<>(player, "right_arm", 10, event -> player.getRightArmEvent() == null ? PlayState.CONTINUE : PlayerEntityAnimator.armAnimationController(event, Arm.RIGHT)));
        controllers.add(new AnimationController<>(player, "left_arm", 10, event -> player.getLeftArmEvent() == null ? PlayState.CONTINUE : PlayerEntityAnimator.armAnimationController(event, Arm.LEFT)));
        controllers.add(new AnimationController<>(player, "generic", 10, event -> player.getGenericEvent() == null ? PlayState.CONTINUE : PlayerEntityAnimator.genericAnimationController(event)));
    }

    private static PlayState movementAnimationController(final AnimationState<PlayerEntity> event) {
        PlayerEntity player = event.getAnimatable();
        Hand hand = player.getMainHandStack().isEmpty() ? player.getOffHandStack().isEmpty() ? Hand.MAIN_HAND : Hand.OFF_HAND : Hand.MAIN_HAND;

        tryPlay(event.getController(), getMovementType(player) + getMovementModifier(player), animationParametersFromItem(player.getStackInHand(hand)).type(), getArmForHand(hand, player.getMainArm()).asString());

        return PlayState.CONTINUE;
    }

    private static PlayState armAnimationController(AnimationState<PlayerEntity> event, Arm arm) {
        PlayerEntity player = event.getAnimatable();
        AnimationEvent.Item animationEvent = arm == Arm.RIGHT ? player.getRightArmEvent() : player.getLeftArmEvent();
        AnimationParameters parameters = animationParametersFromItem(animationEvent.item());

        event.getController().forceAnimationReset();
        event.getController().setAnimationSpeed(parameters.speed());
        tryPlay(event.getController(), animationEvent.type(), parameters.type(), arm.asString(), getMovementType(player) + getMovementModifier(player));

        if (arm == Arm.RIGHT) player.setRightArmEvent(null);
        else player.setLeftArmEvent(null);

        return PlayState.CONTINUE;
    }

    private static PlayState genericAnimationController(AnimationState<PlayerEntity> event) {
        PlayerEntity player = event.getAnimatable();
        AnimationEvent.Data animationEvent = player.getGenericEvent();

        event.getController().forceAnimationReset();
        tryPlay(event.getController(), "generic", animationEvent.type(), animationEvent.data(), getMovementType(player) + getMovementModifier(player));

        player.setGenericEvent(null);

        return PlayState.CONTINUE;
    }

    private static String getMovementType(PlayerEntity player) {
        if (isWalking(player)) {
            if (player.isSprinting()) {
                return "sprint";
            } else {
                return "walk";
            }
        } else {
            return "idle";
        }
    }

    private static String getMovementModifier(PlayerEntity player) {
        for (Map.Entry<Function<PlayerEntity, Boolean>, String> entry : MOVEMENT_MODIFIERS.entrySet()) {
            if (entry.getKey().apply(player)) return "_" + entry.getValue();
        }
        return "";
    }

    private static boolean isWalking(PlayerEntity player) {
        return player.getVelocity().horizontalLengthSquared() < (player.isOnGround() ? 0.01 : 0.1);
    }

    private static AnimationParameters animationParametersFromItem(ItemStack item) {
        return AnimationParameters.fromComponent(item.getComponents().get(Components.ANIMATION_TYPE), item.isEmpty() ? "empty" : "default", 1);
    }

    private static void tryPlay(AnimationController<PlayerEntity> controller, String... animation) {
        if (!controller.tryTriggerAnimation(String.join(".", animation)) && animation.length > 0)
            tryPlay(controller, Arrays.copyOfRange(animation, 0, animation.length - 1));
    }

    private record AnimationParameters(String type, double speed) {
        public static AnimationParameters fromComponent(@Nullable AnimationTypeComponent component, String defaultName, double defaultSpeed) {
            return component == null ? new AnimationParameters(defaultName, defaultSpeed) : new AnimationParameters(component.type(), component.speed());
        }
    }
}
