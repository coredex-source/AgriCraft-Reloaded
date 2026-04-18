package com.agricraft.agricraft.client.ber;

import com.agricraft.agricraft.common.block.entity.SeedAnalyzerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Renderer for the agricraft seed analyzer block
 */
public class SeedAnalyzerEntityRenderer implements BlockEntityRenderer<SeedAnalyzerBlockEntity, SeedAnalyzerRenderState> {

	public SeedAnalyzerEntityRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public SeedAnalyzerRenderState createRenderState() {
		return new SeedAnalyzerRenderState();
	}

	@Override
	public void extractRenderState(SeedAnalyzerBlockEntity analyzer, SeedAnalyzerRenderState state, float partialTick, Vec3 camera, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderState.extractBase(analyzer, state, crumblingOverlay);
		state.hasSeed = analyzer.hasSeed();
		if (state.hasSeed) {
			ItemStack seed = analyzer.getSeed();
			state.angle = (float) ((720F * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) * Math.PI / 180F);
			Minecraft.getInstance().getItemModelResolver().appendItemLayers(state.seedRenderState, seed, ItemDisplayContext.GROUND, analyzer.getLevel(), null, 0);
		}
	}

	@Override
	public void submit(SeedAnalyzerRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
		if (state.hasSeed) {
			poseStack.pushPose();
			poseStack.translate(0.5, 0.5, 0.5);
			poseStack.mulPose(new Quaternionf(new AxisAngle4f(state.angle, new Vector3f(0, 1, 0))));
			state.seedRenderState.submit(poseStack, collector, state.lightCoords, 0, 0);
			poseStack.popPose();
		}
	}

}
