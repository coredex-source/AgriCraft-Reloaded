package com.agricraft.agricraft.compat.jei;

import com.agricraft.agricraft.common.item.crafting.MagnifyingHelmetRecipe;
import com.agricraft.agricraft.common.registry.ModItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

import java.util.List;

/**
 * Add the magnifying helmet custom crafting recipe to the crafting recipes in JEI
 */
public class MagnifyingHelmetExtension implements ICraftingCategoryExtension<MagnifyingHelmetRecipe> {

	private static final List<ItemStack> HELMETS = List.of(Items.LEATHER_HELMET.getDefaultInstance(), Items.CHAINMAIL_HELMET.getDefaultInstance(), Items.IRON_HELMET.getDefaultInstance(), Items.GOLDEN_HELMET.getDefaultInstance(), Items.DIAMOND_HELMET.getDefaultInstance(), Items.NETHERITE_HELMET.getDefaultInstance(), Items.TURTLE_HELMET.getDefaultInstance());

	@Override
	public List<SlotDisplay> getIngredients(RecipeHolder<MagnifyingHelmetRecipe> recipeHolder) {
		List<SlotDisplay> helmetsInput = HELMETS.stream()
				.map(stack -> new ItemStackTemplate(stack.getItem()))
				.map(SlotDisplay.ItemStackSlotDisplay::new)
				.map(display -> (SlotDisplay) display)
				.toList();
		return List.of(
				new SlotDisplay.Composite(helmetsInput),
				new SlotDisplay.ItemStackSlotDisplay(new ItemStackTemplate(ModItems.MAGNIFYING_GLASS.get()))
		);
	}

	@Override
	public void setRecipe(RecipeHolder<MagnifyingHelmetRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		List<ItemStack> helmetsOutput = HELMETS.stream().map(ItemStack::copy).toList();
		helmetsOutput.forEach(h -> {
			var tag = h.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
			tag.putBoolean("magnifying", true);
			h.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
		});
		int width = getWidth(recipeHolder);
		int height = getHeight(recipeHolder);
		craftingGridHelper.createAndSetInputs(builder, List.of(HELMETS, List.of(ModItems.MAGNIFYING_GLASS.get().getDefaultInstance())), width, height);
		craftingGridHelper.createAndSetOutputs(builder, helmetsOutput);
	}

	@Override
	public int getWidth(RecipeHolder<MagnifyingHelmetRecipe> recipeHolder) {
		return 2;
	}

	@Override
	public int getHeight(RecipeHolder<MagnifyingHelmetRecipe> recipeHolder) {
		return 1;
	}

}
