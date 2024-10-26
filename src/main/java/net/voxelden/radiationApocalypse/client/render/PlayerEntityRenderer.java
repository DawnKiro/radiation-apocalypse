package net.voxelden.radiationApocalypse.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PlayerEntityRenderer extends GeoEntityRenderer<AbstractClientPlayerEntity> {
    public static boolean isGui = true;
    public static Vec3d cameraPosition;
    public static Vec2f cameraRotation = Vec2f.ZERO;
    private final HeldItemRenderer heldItemRenderer;
    private static final float RAD2DEG = 57.29577951308232f;
    private static final float DEG2RAD = 0.017453292519943295f;

    public PlayerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel());
        heldItemRenderer = context.getHeldItemRenderer();
    }

    @Override
    public void preRender(MatrixStack poseStack, AbstractClientPlayerEntity entity, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
        if (MinecraftClient.getInstance().getCameraEntity() == entity) updateCamera(entity, model, partialTick);

        boolean clientPlayer = isClientPlayer(entity);

        model.getBone("client").ifPresent(geoBone -> geoBone.setHidden(!clientPlayer));
        model.getBone("world").ifPresent(geoBone -> geoBone.setHidden(clientPlayer));
    }

    private boolean isClientPlayer(AbstractClientPlayerEntity entity) {
        return entity == MinecraftClient.getInstance().getCameraEntity() && !isGui;
    }

    public void renderFinal(MatrixStack poseStack, AbstractClientPlayerEntity entity, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int color) {
        super.renderFinal(poseStack, entity, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, color);

        boolean clientPlayer = isClientPlayer(entity);
        boolean leftHanded = entity.getMainArm() == Arm.LEFT;

        if (clientPlayer) {
            float handYaw = entity.getYaw(partialTick) * DEG2RAD;
            model.getBone("right_hand_item_client").ifPresent(geoBone -> {
                poseStack.push();
                Vector3d bonePos = geoBone.getLocalPosition();
                poseStack.translate(bonePos.x, bonePos.y, bonePos.z);
                poseStack.multiply(new Quaternionf().rotateYXZ(geoBone.getRotY() - handYaw, geoBone.getRotX(), geoBone.getRotZ()));
                heldItemRenderer.renderItem(entity, entity.getMainHandStack(), ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, leftHanded, poseStack, bufferSource, packedLight);
                poseStack.pop();
            });
            model.getBone("left_hand_item_client").ifPresent(geoBone -> {
                poseStack.push();
                Vector3d bonePos = geoBone.getLocalPosition();
                poseStack.translate(bonePos.x, bonePos.y, bonePos.z);
                poseStack.multiply(new Quaternionf().rotateYXZ(geoBone.getRotY() - handYaw, geoBone.getRotX(), geoBone.getRotZ()));
                heldItemRenderer.renderItem(entity, entity.getOffHandStack(), ModelTransformationMode.THIRD_PERSON_LEFT_HAND, leftHanded, poseStack, bufferSource, packedLight);
                poseStack.pop();
            });
        } else {
            float bodyYaw = MathHelper.lerpAngleDegrees(partialTick, entity.prevBodyYaw, entity.bodyYaw) * DEG2RAD;
            model.getBone("right_hand_item").ifPresent(geoBone -> {
                poseStack.push();
                Vector3d bonePos = geoBone.getLocalPosition();
                poseStack.translate(bonePos.x, bonePos.y, bonePos.z);
                poseStack.multiply(new Quaternionf().rotateYXZ(geoBone.getRotY() - bodyYaw, geoBone.getRotX(), geoBone.getRotZ()));
                heldItemRenderer.renderItem(entity, entity.getMainHandStack(), ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, leftHanded, poseStack, bufferSource, packedLight);
                poseStack.pop();
            });
            model.getBone("left_hand_item").ifPresent(geoBone -> {
                poseStack.push();
                Vector3d bonePos = geoBone.getLocalPosition();
                poseStack.translate(bonePos.x, bonePos.y, bonePos.z);
                poseStack.multiply(new Quaternionf().rotateYXZ(geoBone.getRotY() - bodyYaw, geoBone.getRotX(), geoBone.getRotZ()));
                heldItemRenderer.renderItem(entity, entity.getOffHandStack(), ModelTransformationMode.THIRD_PERSON_LEFT_HAND, leftHanded, poseStack, bufferSource, packedLight);
                poseStack.pop();
            });
        }
    }

    public static void updateCamera(AbstractClientPlayerEntity entity, BakedGeoModel model, float tickDelta) {
        model.getBone("camera").ifPresentOrElse(geoBone -> {
            Vector3d camPos = geoBone.getLocalPosition();
            cameraPosition = new Vec3d(camPos.x, camPos.y, camPos.z);
            cameraRotation = new Vec2f(geoBone.getRotX(), geoBone.getRotY()).multiply(RAD2DEG);
        }, () -> {
            cameraPosition = entity.getLerpedPos(tickDelta).offset(Direction.UP, entity.getEyeHeight(entity.getPose()));
            cameraRotation = Vec2f.ZERO;
        });
    }
}
