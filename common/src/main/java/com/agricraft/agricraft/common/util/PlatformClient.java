package com.agricraft.agricraft.common.util;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;

public abstract class PlatformClient {
	private static PlatformClient platform = null;

	public static void setup(PlatformClient platform) {
		if (PlatformClient.platform == null) {
			PlatformClient.platform = platform;
		}
	}

	public static PlatformClient get() {
		return PlatformClient.platform;
	}

	public abstract BlockStateModel getStandaloneModel(Identifier id);

}
