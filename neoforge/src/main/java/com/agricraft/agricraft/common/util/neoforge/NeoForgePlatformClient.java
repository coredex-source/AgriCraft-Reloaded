package com.agricraft.agricraft.common.util.neoforge;

import com.agricraft.agricraft.client.neoforge.AgriCraftNeoForgeClient;
import com.agricraft.agricraft.common.util.PlatformClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

/**
 * NeoForge implementation of {@link PlatformClient}
 */
public class NeoForgePlatformClient extends PlatformClient {

	@Override
	public BlockStateModel getStandaloneModel(Identifier id) {
		StandaloneModelKey<BlockStateModel> key = AgriCraftNeoForgeClient.getModelKey(id);
		if (key == null) {
			return null;
		}
		return Minecraft.getInstance().getModelManager().getStandaloneModel(key);
	}

}
