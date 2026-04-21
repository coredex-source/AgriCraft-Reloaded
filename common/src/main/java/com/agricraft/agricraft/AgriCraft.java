package com.agricraft.agricraft;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.common.registry.ModBlockEntityTypes;
import com.agricraft.agricraft.common.registry.ModBlocks;
import com.agricraft.agricraft.common.registry.ModCreativeTabs;
import com.agricraft.agricraft.common.registry.ModItems;
import com.agricraft.agricraft.common.registry.ModMenus;
import com.agricraft.agricraft.common.registry.ModRecipeSerializers;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class AgriCraft {

	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
		ModBlocks.BLOCKS.init();
		ModItems.ITEMS.init();
		ModBlockEntityTypes.BLOCK_ENTITY_TYPES.init();
		ModCreativeTabs.CREATIVE_MODE_TAB.init();
		ModMenus.MENUS.init();
		ModRecipeSerializers.RECIPE_SERIALIZERS.init();
		LOGGER.info("Intializing API for " + AgriApi.MOD_ID);
	}

}
