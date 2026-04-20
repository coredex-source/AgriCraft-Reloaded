package com.agricraft.agricraft.client.tools.journal.drawers;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.AgriClientApi;
import com.agricraft.agricraft.api.tools.journal.JournalData;
import com.agricraft.agricraft.api.tools.journal.JournalPageDrawer;
import com.agricraft.agricraft.common.item.journal.MutationsPage;
import com.agricraft.agricraft.common.util.LangUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public class MutationPageDrawer implements JournalPageDrawer<MutationsPage> {

	@Override
	public void drawLeftSheet(GuiGraphicsExtractor guiGraphics, MutationsPage page, int pageX, int pageY, JournalData journalData) {
		int dy = 6;
		for (List<Identifier> plants : page.getMutationsLeft()) {
			this.drawMutation(guiGraphics, plants, pageX + 10, pageY + dy);
			dy += 20;
		}
	}

	@Override
	public void drawRightSheet(GuiGraphicsExtractor guiGraphics, MutationsPage page, int pageX, int pageY, JournalData journalData) {
		int dy = 6;
		for (List<Identifier> plants : page.getMutationsRight()) {
			this.drawMutation(guiGraphics, plants, pageX + 10, pageY + dy);
			dy += 20;
		}
	}

	public void drawMutation(GuiGraphicsExtractor guiGraphics, List<Identifier> plants, int pageX, int pageY) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI_COMPONENTS, pageX + 10, pageY + 24, 0, 76, 86, 18, 128, 128);
		TextureAtlasSprite parent1 = AgriClientApi.getPlantModel(plants.get(0), AgriApi.getPlant(plants.get(0)).map(plant -> plant.getInitialGrowthStage().total() - 1).orElse(0)).particleMaterial().sprite();
		TextureAtlasSprite parent2 = AgriClientApi.getPlantModel(plants.get(1), AgriApi.getPlant(plants.get(1)).map(plant -> plant.getInitialGrowthStage().total() - 1).orElse(0)).particleMaterial().sprite();
		TextureAtlasSprite child = AgriClientApi.getPlantModel(plants.get(2), AgriApi.getPlant(plants.get(2)).map(plant -> plant.getInitialGrowthStage().total() - 1).orElse(0)).particleMaterial().sprite();
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, parent1, pageX + 11, pageY + 25, 16, 16);
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, parent2, pageX + 45, pageY + 25, 16, 16);
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, child, pageX + 79, pageY + 25, 16, 16);
	}

	@Override
	public void drawLeftTooltip(GuiGraphicsExtractor guiGraphics, MutationsPage page, int pageX, int pageY, int mouseX, int mouseY) {
		int dy = 6;
		for (List<Identifier> plants : page.getMutationsLeft()) {
			Component component = getComponent(mouseX, mouseY, dy, plants);
			if (component != null) {
				guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, component, mouseX + pageX, mouseY + pageY);
			}
			dy += 20;
		}
	}

	@Override
	public void drawRightTooltip(GuiGraphicsExtractor guiGraphics, MutationsPage page, int pageX, int pageY, int mouseX, int mouseY) {
		int dy = 6;
		for (List<Identifier> plants : page.getMutationsRight()) {
			Component component = getComponent(mouseX, mouseY, dy, plants);
			if (component != null) {
				guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, component, mouseX + pageX, mouseY + pageY);
			}
			dy += 20;
		}
	}

	private Component getComponent(int mouseX, int mouseY, int y, List<Identifier> plants) {
		if (11 <= mouseX && mouseX <= 27 && y + 25 <= mouseY && mouseY <= y + 41) {
			return LangUtils.plantName(plants.get(0).toString());
		} else if (45 <= mouseX && mouseX <= 61 && y + 25 <= mouseY && mouseY <= y + 41) {
			return LangUtils.plantName(plants.get(1).toString());
		} else if (79 <= mouseX && mouseX <= 95 && y + 25 <= mouseY && mouseY <= y + 41) {
			return LangUtils.plantName(plants.get(2).toString());
		}
		return null;
	}

}
