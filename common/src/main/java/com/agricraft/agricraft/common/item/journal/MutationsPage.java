package com.agricraft.agricraft.common.item.journal;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.tools.journal.JournalPage;
import net.minecraft.resources.Identifier;

import java.util.List;

public class MutationsPage implements JournalPage {

	public static final Identifier ID = Identifier.fromNamespaceAndPath(AgriApi.MOD_ID, "mutation_page");
	public static final int LIMIT = 18;

	private final List<List<Identifier>> mutationsLeft;
	private final List<List<Identifier>> mutationsRight;

	public MutationsPage(List<List<Identifier>> mutations) {
		int count = mutations.size();
		if (count <= LIMIT / 2) {
			this.mutationsLeft = mutations;
			this.mutationsRight = List.of();
		} else {
			this.mutationsLeft = mutations.subList(0, LIMIT / 2 - 1);
			this.mutationsRight = mutations.subList(LIMIT / 2, count - 1);
		}
	}

	@Override
	public Identifier getDrawerId() {
		return ID;
	}

	public List<List<Identifier>> getMutationsLeft() {
		return this.mutationsLeft;
	}

	public List<List<Identifier>> getMutationsRight() {
		return this.mutationsRight;
	}

}
