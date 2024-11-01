package net.voxelden.radiationApocalypse.mixin;

import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.voxelden.radiationApocalypse.RadiationApocalypse;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(SpriteAtlasManager.class)
public class SpriteAtlasManagerMixin {
    @Mutable
    @Shadow
    @Final
    private Map<Identifier, SpriteAtlasManager.Atlas> atlases;

    @Inject(method = "<init>", at = @At("CTOR_HEAD"))
    private void initTextures(Map<Identifier, Identifier> loaders, TextureManager textureManager, CallbackInfo ci) {
        atlases = loaders.entrySet().stream().<Map.Entry<Identifier, SpriteAtlasManager.Atlas>>flatMap(entry -> {
            SpriteAtlasTexture spriteAtlasTexture = new SpriteAtlasTexture(entry.getKey());
            textureManager.registerTexture(entry.getKey(), spriteAtlasTexture);

            //Identifier dataAtlasID = WorldRenderer.getDataTexture(entry.getKey());
            //SpriteAtlasTexture spriteDataAtlasTexture = new SpriteAtlasTexture(dataAtlasID);
            //textureManager.registerTexture(dataAtlasID, spriteDataAtlasTexture);

            return Stream.of(new AbstractMap.SimpleEntry<>(entry.getKey(), AtlasAccessor.getNew(spriteAtlasTexture, entry.getValue())));//, new AbstractMap.SimpleEntry<>(dataAtlasID, AtlasAccessor.getNew(spriteDataAtlasTexture, entry.getValue())));
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        atlases.forEach((id, atlas) -> RadiationApocalypse.LOGGER.info("{};{};{}", id, atlas.atlas().getId(), atlas.atlasInfoLocation()));
    }

    @Mixin(SpriteAtlasManager.Atlas.class)
    public interface AtlasAccessor {
        @Invoker("<init>")
        static SpriteAtlasManager.Atlas getNew(SpriteAtlasTexture atlas, Identifier atlasInfoLocation) {
            throw new AssertionError();
        }
    }
}
