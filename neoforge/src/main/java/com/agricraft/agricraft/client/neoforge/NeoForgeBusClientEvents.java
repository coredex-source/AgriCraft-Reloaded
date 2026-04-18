package com.agricraft.agricraft.client.neoforge;

import com.agricraft.agricraft.api.AgriApi;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

/**
 * NeoForge client event handler in the neoforge event bus
 */
@EventBusSubscriber(modid = AgriApi.MOD_ID, value = Dist.CLIENT)
public class NeoForgeBusClientEvents {

	@SubscribeEvent
	public static void onTooltipRender(RenderTooltipEvent.GatherComponents event) {
		if (event.getItemStack().has(DataComponents.CUSTOM_DATA) && event.getItemStack().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("magnifying", false)) {
			event.getTooltipElements().add(1, Either.left(Component.translatable("agricraft.tooltip.magnifying").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC)));
		}
	}

}
