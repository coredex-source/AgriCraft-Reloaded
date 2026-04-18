package com.agricraft.agricraft.common.item.journal;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.tools.journal.JournalPage;
import net.minecraft.resources.Identifier;

public class FrontPage implements JournalPage {

	public static final Identifier ID = Identifier.fromNamespaceAndPath(AgriApi.MOD_ID, "front_page");

	@Override
	public Identifier getDrawerId() {
		return ID;
	}

}
