package com.agricraft.agricraft.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class MagnifyingGlassItem extends Item {

	public MagnifyingGlassItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag isAdvanced) {
		tooltipAdder.accept(Component.translatable("agricraft.tooltip.magnifying_glass").withStyle(ChatFormatting.DARK_GRAY));
	}

}
