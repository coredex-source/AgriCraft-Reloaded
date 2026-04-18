package com.agricraft.agricraft.api.tools.journal;

import net.minecraft.resources.Identifier;

import java.util.List;

public interface JournalData {

	JournalPage getPage(int index);

	int size();

	List<Identifier> getDiscoveredSeeds();

}
