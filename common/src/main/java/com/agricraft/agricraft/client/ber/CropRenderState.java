package com.agricraft.agricraft.client.ber;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class CropRenderState extends BlockEntityRenderState {
	public BlockStateModel sticksModel;
	public BlockStateModel plantModel;
	public BlockStateModel weedModel;
	public int plantColor = -1;
}
