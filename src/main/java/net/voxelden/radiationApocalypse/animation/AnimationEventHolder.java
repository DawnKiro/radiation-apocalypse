package net.voxelden.radiationApocalypse.animation;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

public interface AnimationEventHolder {
    IllegalStateException ILLEGAL_SET = new IllegalStateException("Can't set events in holder!");
    IllegalStateException ILLEGAL_GET = new IllegalStateException("Can't get events in holder!");

    default void put(String type, Hand hand, Arm mainArm, ItemStack item) {
        AnimationEvent.Item event = new AnimationEvent.Item(type, item);
        if (getArmForHand(hand, mainArm) == Arm.RIGHT) setRightArmEvent(event);
        else setLeftArmEvent(event);
    }

    default void put(String type, String data) {
        setGenericEvent(new AnimationEvent.Data(type, data));
    }

    default void setRightArmEvent(AnimationEvent.Item event) {
        throw ILLEGAL_SET;
    }

    default AnimationEvent.Item getRightArmEvent() {
        throw ILLEGAL_GET;
    }

    default void setLeftArmEvent(AnimationEvent.Item event) {
        throw ILLEGAL_SET;
    }

    default AnimationEvent.Item getLeftArmEvent() {
        throw ILLEGAL_GET;
    }

    default void setGenericEvent(AnimationEvent.Data event) {
        throw ILLEGAL_SET;
    }

    default AnimationEvent.Data getGenericEvent() {
        throw ILLEGAL_GET;
    }

    static Arm getArmForHand(Hand hand, Arm mainArm) {
        return hand == Hand.MAIN_HAND ? mainArm : mainArm.getOpposite();
    }
}
