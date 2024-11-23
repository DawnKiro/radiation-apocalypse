package net.voxelden.radiationApocalypse.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.VertexSorter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.chunk.BlockBufferAllocatorStorage;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.chunk.SectionBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.voxelden.radiationApocalypse.client.render.WorldRenderer;
import net.voxelden.radiationApocalypse.client.render.light.BlockLights;
import net.voxelden.radiationApocalypse.client.render.light.Light;
import net.voxelden.radiationApocalypse.client.render.light.LightManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SectionBuilder.class)
public class SectionBuilderMixin {
    @Unique
    private static final Direction[] DIRECTIONS = Direction.values();

    @Inject(method = "build", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getFluidState()Lnet/minecraft/fluid/FluidState;"))
    private void injectRenderBlock(ChunkSectionPos sectionPos, ChunkRendererRegion renderRegion, VertexSorter vertexSorter, BlockBufferAllocatorStorage allocatorStorage, CallbackInfoReturnable<SectionBuilder.RenderData> cir, @Local(ordinal = 2) BlockPos blockPos, @Local BlockState blockState) {
        if (WorldRenderer.useCustomRenderer) {
            boolean lightOpen = false;
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (Direction direction : DIRECTIONS) {
                mutable.set(blockPos, direction);
                BlockState neighborState = renderRegion.getBlockState(mutable);
                if (!(neighborState.isSideSolidFullSquare(renderRegion, mutable, direction.getOpposite()) || blockState.equals(neighborState))) {
                    lightOpen = true;
                    break;
                }
            }

            BlockPos pos = blockPos.toImmutable();
            List<Light> lights = BlockLights.get(pos, blockState);
            if (lightOpen || lights.isEmpty()) {
                LightManager.queueBlockLight(pos, lights);
            }
        }
    }
}
