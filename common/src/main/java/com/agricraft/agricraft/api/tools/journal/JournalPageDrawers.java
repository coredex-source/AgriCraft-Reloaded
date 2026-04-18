package com.agricraft.agricraft.api.tools.journal;

import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class JournalPageDrawers {

	private static final Map<Identifier, JournalPageDrawer<?>> DRAWERS = new HashMap<>();

	public static void registerPageDrawer(Identifier id, JournalPageDrawer<?> drawer) {
		DRAWERS.put(id, drawer);
	}

	@SuppressWarnings("unchecked")
	public static <T extends JournalPage> JournalPageDrawer<T> getPageDrawer(T page) {
		return (JournalPageDrawer<T>) DRAWERS.getOrDefault(page.getDrawerId(), EmptyPageDrawer.INSTANCE);
	}

}
