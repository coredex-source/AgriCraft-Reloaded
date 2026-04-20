package com.agricraft.agricraft.common.item.crafting;

import com.agricraft.agricraft.common.registry.ModItems;
import com.agricraft.agricraft.common.registry.ModRecipeSerializers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Custom recipe to add the magnifying property to a helmet, crafted from the helmet and a magnifying glass.
 * An item is considered as a helmet if it has an {@link Equippable} component for {@link EquipmentSlot#HEAD}.
 */
public class MagnifyingHelmetRecipe extends CustomRecipe {

	public MagnifyingHelmetRecipe() {
		super();
	}

	@Override
	public boolean matches(CraftingInput container, Level level) {
		boolean helmet = false;
		boolean glass = false;
		for (int i = 0; i < container.size(); i++) {
			ItemStack itemStack = container.getItem(i);
			if (isHeadHelmet(itemStack) && (!itemStack.has(DataComponents.CUSTOM_DATA) || !itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("magnifying", false))) {
				if (helmet) {
					return false;
				} else {
					helmet = true;
				}
			} else if (itemStack.is(ModItems.MAGNIFYING_GLASS.get())) {
				if (glass) {
					return false;
				} else {
					glass = true;
				}
			}
		}
		return helmet && glass;
	}

	@NotNull
	@Override
	public ItemStack assemble(CraftingInput container) {
		ItemStack helmet = null;
		ItemStack glass = null;
		for (int i = 0; i < container.size(); i++) {
			ItemStack itemStack = container.getItem(i);
			if (isHeadHelmet(itemStack)) {
				helmet = itemStack;
			} else if (itemStack.is(ModItems.MAGNIFYING_GLASS.get())) {
				glass = itemStack;
			}
		}
		if (helmet != null && glass != null) {
			ItemStack copy = helmet.copy();
			CompoundTag tag = copy.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
			tag.putBoolean("magnifying", true);
			copy.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
			return copy;
		}
		return ItemStack.EMPTY;
	}

	@NotNull
	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return ModRecipeSerializers.MAGNIFYING_HELMET.get();
	}

	private static boolean isHeadHelmet(ItemStack stack) {
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		return equippable != null && equippable.slot() == EquipmentSlot.HEAD;
	}

}
