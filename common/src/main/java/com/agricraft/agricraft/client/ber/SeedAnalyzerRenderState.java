package com.agricraft.agricraft.client.ber;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class SeedAnalyzerRenderState extends BlockEntityRenderState {
	public boolean hasSeed;
	public final ItemStackRenderState seedRenderState = new ItemStackRenderState();
	public float angle;
}
