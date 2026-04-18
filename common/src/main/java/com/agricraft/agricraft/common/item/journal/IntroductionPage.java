package com.agricraft.agricraft.common.item.journal;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.tools.journal.JournalPage;
import net.minecraft.resources.Identifier;

public class IntroductionPage implements JournalPage {

	public static final Identifier ID = Identifier.fromNamespaceAndPath(AgriApi.MOD_ID, "introduction_page");

	@Override
	public Identifier getDrawerId() {
		return ID;
	}

}
