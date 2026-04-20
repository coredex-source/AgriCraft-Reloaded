package com.agricraft.agricraft.compat.jei;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.AgriClientApi;
import com.agricraft.agricraft.api.plant.AgriPlant;
import com.agricraft.agricraft.common.util.LangUtils;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlantIngredient {

	public static final IIngredientType<AgriPlant> TYPE = () -> AgriPlant.class;

	public static final IIngredientHelper<AgriPlant> HELPER = new IIngredientHelper<>() {
		@Override
		public IIngredientType<AgriPlant> getIngredientType() {
			return TYPE;
		}

		@Override
		public String getDisplayName(AgriPlant plant) {
			return LangUtils.plantName(AgriApi.getPlantId(plant).map(Identifier::toString).orElse("agricraft:unknown")).getString();
		}

		@Override
		public Object getUid(AgriPlant plant, UidContext context) {
			return AgriApi.getPlantId(plant).map(Identifier::toString).orElse("agricraft:unknown");
		}

		@Override
		public Identifier getIdentifier(AgriPlant plant) {
			return AgriApi.getPlantId(plant).orElse(Identifier.parse("agricraft:unknown"));
		}

		@Override
		public AgriPlant copyIngredient(AgriPlant plant) {
			return plant;
		}

		@Override
		public String getErrorInfo(AgriPlant plant) {
			return AgriApi.getPlantId(plant).map(Identifier::toString).orElse("agricraft:unknown");
		}
	};

	public static final IIngredientRenderer<AgriPlant> RENDERER = new IIngredientRenderer<>() {
		@Override
		public void render(GuiGraphicsExtractor guiGraphics, AgriPlant plant) {
			Optional<Identifier> optional = AgriApi.getPlantId(plant);
			if (optional.isPresent()) {
				Identifier plantId = optional.get();
				// get the model for the last growth stage and use the particle texture (that is also the crop texture) to render in jei
				BlockStateModel model = AgriClientApi.getPlantModel(plantId.toString(), plant.getInitialGrowthStage().total() - 1);

				TextureAtlasSprite sprite = model.particleMaterial().sprite();
				guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, 16, 16);
			}
		}

		@Override
		public List<Component> getTooltip(AgriPlant ingredient, TooltipFlag tooltipFlag) {
			ArrayList<Component> list = new ArrayList<>();
			AgriApi.getPlantId(ingredient).map(Identifier::toString).ifPresent(id -> {
				list.add(LangUtils.plantName(id));
				Component desc = LangUtils.plantDescription(id);
				if (desc != null) {
					list.add(desc);
				}
			});
			return list;
		}
	};

}
