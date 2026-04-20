package com.agricraft.agricraft.common.util.fabric;

import com.agricraft.agricraft.client.AgriCraftFabricClient;
import com.agricraft.agricraft.common.util.PlatformClient;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.resources.Identifier;

/**
 * Fabric implementation of {@link PlatformClient}
 */
public class FabricPlatformClient extends PlatformClient {

	@Override
	public BlockStateModel getStandaloneModel(Identifier id) {
		ExtraModelKey<BlockStateModel> key = AgriCraftFabricClient.getModelKey(id);
		if (key == null) {
			return null;
		}
		return ((FabricModelManager) Minecraft.getInstance().getModelManager()).getModel(key);
	}

}
