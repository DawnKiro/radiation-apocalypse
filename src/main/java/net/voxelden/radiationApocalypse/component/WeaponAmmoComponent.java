package net.voxelden.radiationApocalypse.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.List;

public record WeaponAmmoComponent(List<ItemStack> ammo, int ammoPerShot, boolean automatic) {
    public static final Codec<WeaponAmmoComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(ItemStack.OPTIONAL_CODEC).fieldOf("ammo").forGetter(WeaponAmmoComponent::ammo), Codec.INT.fieldOf("ammo_per_shot").forGetter(WeaponAmmoComponent::ammoPerShot), Codec.BOOL.fieldOf("automatic").forGetter(WeaponAmmoComponent::automatic)).apply(instance, WeaponAmmoComponent::new));
    public static final PacketCodec<RegistryByteBuf, WeaponAmmoComponent> PACKET_CODEC = PacketCodec.tuple(ItemStack.OPTIONAL_LIST_PACKET_CODEC, WeaponAmmoComponent::ammo, PacketCodecs.INTEGER, WeaponAmmoComponent::ammoPerShot, PacketCodecs.BOOL, WeaponAmmoComponent::automatic, WeaponAmmoComponent::new);
}
