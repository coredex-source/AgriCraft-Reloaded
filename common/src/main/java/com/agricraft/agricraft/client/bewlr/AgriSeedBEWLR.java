package com.agricraft.agricraft.client.bewlr;

import com.agricraft.agricraft.api.AgriClientApi;
import com.agricraft.agricraft.common.item.AgriSeedItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Renderer for the agricraft seed item.
 */
public class AgriSeedBEWLR implements SpecialModelRenderer<String> {

	public static final AgriSeedBEWLR INSTANCE = new AgriSeedBEWLR();
	private static final Vector3fc MIN = new Vector3f(0.0F, 0.0F, 0.0F);
	private static final Vector3fc MAX = new Vector3f(1.0F, 1.0F, 1.0F);

	private AgriSeedBEWLR() {
	}

	@Override
	public String extractArgument(ItemStack stack) {
		return AgriSeedItem.getSpecies(stack);
	}

	@Override
	public void submit(String species, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int packedOverlay, boolean hasFoil, int seed) {
		BlockStateModel seedModel = AgriClientApi.getSeedModel(species);
		collector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), seedModel, 1.0F, 1.0F, 1.0F, packedLight, packedOverlay, 0);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> extents) {
		extents.accept(MIN);
		extents.accept(MAX);
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

		@Override
		public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
			return INSTANCE;
		}

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}
	}
}
