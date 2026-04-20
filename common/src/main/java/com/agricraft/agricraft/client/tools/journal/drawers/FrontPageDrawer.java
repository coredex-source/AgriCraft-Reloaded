package com.agricraft.agricraft.client.tools.journal.drawers;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.tools.journal.JournalData;
import com.agricraft.agricraft.api.tools.journal.JournalPageDrawer;
import com.agricraft.agricraft.common.item.journal.FrontPage;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class FrontPageDrawer implements JournalPageDrawer<FrontPage> {

	private static final Identifier BACKGROUND_FRONT_RIGHT = Identifier.fromNamespaceAndPath(AgriApi.MOD_ID, "textures/gui/journal/front_page.png");

	@Override
	public void drawLeftSheet(GuiGraphicsExtractor guiGraphics, FrontPage page, int pageX, int pageY, JournalData journalData) {

	}

	@Override
	public void drawRightSheet(GuiGraphicsExtractor guiGraphics, FrontPage page, int pageX, int pageY, JournalData journalData) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_FRONT_RIGHT, pageX + 8, pageY + 25, 0, 0, 128, 192, 128, 192);
	}

}
