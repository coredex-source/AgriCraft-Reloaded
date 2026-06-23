package com.agricraft.agricraft.fabric.mixin;

import com.agricraft.agricraft.common.item.SeedBagItem;
import com.agricraft.agricraft.common.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@Unique
	private double agricraft$scrollCarry;

	@Inject(method = "onScroll(JDD)V", at = @At("HEAD"), cancellable = true)
	private void agricraft$onMouseScrollNamed(long windowPointer, double xOffset, double yOffset, CallbackInfo ci) {
		this.agricraft$onMouseScroll(yOffset, ci);
	}

	@Unique
	private void agricraft$onMouseScroll(double yOffset, CallbackInfo ci) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null || !player.isShiftKeyDown() || !player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.SEED_BAG.get())) {
			this.agricraft$scrollCarry = 0;
			return;
		}
		ci.cancel();
		this.agricraft$scrollCarry += yOffset;
		int delta = (int) this.agricraft$scrollCarry;
		if (delta == 0) {
			return;
		}
		this.agricraft$scrollCarry -= delta;
		SeedBagItem.changeSorter(player.getItemInHand(InteractionHand.MAIN_HAND), delta);
		net.minecraft.nbt.CompoundTag tag = player.getItemInHand(InteractionHand.MAIN_HAND).getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
		int s = tag.getIntOr("sorter", 0);
		String id = SeedBagItem.SORTERS.get(s).getId().toString().replace(":", ".");
		Minecraft.getInstance().gui.hud.setOverlayMessage(Component.translatable("agricraft.tooltip.bag.sorter")
				.append(Component.translatable("agricraft.tooltip.bag.sorter." + id)), false);
	}
}
