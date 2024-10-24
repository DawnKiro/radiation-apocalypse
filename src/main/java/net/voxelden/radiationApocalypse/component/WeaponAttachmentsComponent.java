package net.voxelden.radiationApocalypse.component;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public record WeaponAttachmentsComponent(Map<String, List<ItemStack>> properties) implements TooltipAppender {
    public static final Codec<WeaponAttachmentsComponent> CODEC = Codec.unboundedMap(Codec.STRING, Codec.list(ItemStack.OPTIONAL_CODEC)).xmap(WeaponAttachmentsComponent::new, WeaponAttachmentsComponent::properties);
    public static final PacketCodec<RegistryByteBuf, WeaponAttachmentsComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.map(HashMap::new, PacketCodecs.STRING, ItemStack.OPTIONAL_LIST_PACKET_CODEC),
            WeaponAttachmentsComponent::properties,
            WeaponAttachmentsComponent::new
    );

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {

    }
}
