package net.voxelden.radiationApocalypse.item;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.voxelden.radiationApocalypse.client.render.model.attachableItem.AttachableItemRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public abstract class AttachableItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final AnimationController<AttachableItem> animationController = new AnimationController<>(this, "item", 10, AttachableItem::animationController);
    private final RawAnimation animation = RawAnimation.begin();

    public AttachableItem(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private AttachableItemRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (renderer == null)
                    renderer = new AttachableItemRenderer();

                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(animationController);
        addTriggerableAnimations();
    }

    protected void addTriggerableAnimation(String name) {
        animationController.triggerableAnim(name, animation);
    }

    protected void triggerAnimation(LivingEntity entity, ItemStack stack, String name) {
        if (entity.getWorld() instanceof ServerWorld world) {
            triggerAnim(entity, GeoItem.getOrAssignId(stack, world), animationController.getName(), name);
        }
    }

    private static PlayState animationController(AnimationState<AttachableItem> event) {
        return null;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public abstract String getModel();

    protected abstract void addTriggerableAnimations();
}
