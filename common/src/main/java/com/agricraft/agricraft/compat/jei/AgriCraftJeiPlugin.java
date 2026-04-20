package com.agricraft.agricraft.compat.jei;


import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.codecs.AgriMutation;
import com.agricraft.agricraft.api.genetic.AgriGenome;
import com.agricraft.agricraft.api.plant.AgriPlant;
import com.agricraft.agricraft.common.item.crafting.MagnifyingHelmetRecipe;
import com.agricraft.agricraft.common.registry.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@JeiPlugin
public class AgriCraftJeiPlugin implements IModPlugin {

	public static final Identifier ID = Identifier.fromNamespaceAndPath(AgriApi.MOD_ID, "compat_jei");

	@Override
	@NotNull
	public Identifier getPluginUid() {
		return ID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		// Register all The Seeds.
		registration.registerSubtypeInterpreter(ModItems.SEED.get(), (stack, context) -> {
			AgriGenome genome = AgriGenome.fromNBT(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag());
			if (genome != null) {
				return genome.getSpeciesGene().getTrait();
			}
			return "agricraft:unknown";
		});
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		// TODO: In JEI 18+, addCategoryExtension was removed. Need to use the new extension API.
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new CropProduceCategory());
		registration.addRecipeCategories(new CropClippingCategory());
		registration.addRecipeCategories(new CropMutationCategory());
		registration.addRecipeCategories(new CropRequirementCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		AgriApi.getPlantRegistry().ifPresent(registry -> registration.addRecipes(CropProduceCategory.TYPE, registry.stream().toList()));
		AgriApi.getPlantRegistry().ifPresent(registry -> registration.addRecipes(CropClippingCategory.TYPE, registry.stream().filter(plant -> {
			ArrayList<ItemStack> cliProducts = new ArrayList<>();
			plant.getAllPossibleClipProducts(cliProducts::add);
			return !cliProducts.isEmpty();
		}).toList()));
		AgriApi.getPlantRegistry().ifPresent(registry -> registration.addRecipes(CropRequirementCategory.TYPE, registry.stream().map(CropRequirementCategory.Recipe::new).toList()));
		AgriApi.getMutationRegistry().ifPresent(registry -> registration.addRecipes(CropMutationCategory.TYPE, registry.stream().filter(AgriMutation::isValid).toList()));
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		AgriApi.getPlantRegistry().ifPresent(registry -> registration.register(PlantIngredient.TYPE, registry.stream().toList(), PlantIngredient.HELPER, PlantIngredient.RENDERER, AgriPlant.CODEC));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(ModItems.CLIPPER.get().getDefaultInstance(), CropClippingCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.WOODEN_CROP_STICKS.get().getDefaultInstance(), CropProduceCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.WOODEN_CROP_STICKS.get().getDefaultInstance(), CropClippingCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.WOODEN_CROP_STICKS.get().getDefaultInstance(), CropMutationCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.WOODEN_CROP_STICKS.get().getDefaultInstance(), CropRequirementCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.IRON_CROP_STICKS.get().getDefaultInstance(), CropProduceCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.IRON_CROP_STICKS.get().getDefaultInstance(), CropClippingCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.IRON_CROP_STICKS.get().getDefaultInstance(), CropMutationCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.IRON_CROP_STICKS.get().getDefaultInstance(), CropRequirementCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.OBSIDIAN_CROP_STICKS.get().getDefaultInstance(), CropProduceCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.OBSIDIAN_CROP_STICKS.get().getDefaultInstance(), CropClippingCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.OBSIDIAN_CROP_STICKS.get().getDefaultInstance(), CropMutationCategory.TYPE);
		registration.addRecipeCatalyst(ModItems.OBSIDIAN_CROP_STICKS.get().getDefaultInstance(), CropRequirementCategory.TYPE);
	}

	public static IDrawable createDrawable(Identifier location, int uOffset, int vOffset, int width, int height, int textureWidth, int textureHeight) {
		return new IDrawable() {
			@Override
			public int getWidth() {
				return width;
			}

			@Override
			public int getHeight() {
				return height;
			}

			@Override
			public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, location, xOffset, yOffset, uOffset, vOffset, getWidth(), getHeight(), textureWidth, textureHeight);
			}
		};
	}

}