package net.voxelden.radiationApocalypse.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record AnimationTypeComponent(String type, double speed) {
    public static final Codec<AnimationTypeComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("type").forGetter(AnimationTypeComponent::type), Codec.DOUBLE.optionalFieldOf("speed", 1d).forGetter(AnimationTypeComponent::speed)).apply(instance, AnimationTypeComponent::new));
    public static final PacketCodec<RegistryByteBuf, AnimationTypeComponent> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.STRING, AnimationTypeComponent::type, PacketCodecs.DOUBLE, AnimationTypeComponent::speed, AnimationTypeComponent::new);
}
