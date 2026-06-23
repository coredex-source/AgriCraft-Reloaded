package com.agricraft.agricraft.client.ber;

import com.agricraft.agricraft.api.AgriClientApi;
import com.agricraft.agricraft.api.crop.AgriGrowthStage;
import com.agricraft.agricraft.client.ClientUtil;
import com.agricraft.agricraft.common.block.CropBlock;
import com.agricraft.agricraft.common.block.entity.CropBlockEntity;
import com.agricraft.agricraft.common.util.PlatformClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Renderer for the agricraft crop block
 */
public class CropBlockEntityRenderer implements BlockEntityRenderer<CropBlockEntity, CropRenderState> {

	public CropBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public CropRenderState createRenderState() {
		return new CropRenderState();
	}

	@Override
	public void extractRenderState(CropBlockEntity blockEntity, CropRenderState state, float partialTick, Vec3 camera, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay);
		state.sticksModel = null;
		state.plantModel = null;
		state.weedModel = null;
		state.plantColor = -1;

		if (blockEntity.hasCropSticks()) {
			String modelId = ClientUtil.getModelForSticks(blockEntity.getBlockState().getValue(CropBlock.STICK_VARIANT));
			if (blockEntity.isCrossCropSticks()) {
				modelId = modelId.replace("crop", "cross_crop");
			}
			state.sticksModel = PlatformClient.get().getStandaloneModel(Identifier.parse(modelId));
		}
		if (blockEntity.hasPlant()) {
			AgriGrowthStage stage = blockEntity.getGrowthStage();
			String plantId = blockEntity.getPlantId();
			state.plantModel = AgriClientApi.getPlantModel(plantId, stage.index());
			var tintSource = Minecraft.getInstance().getBlockColors().getTintSource(blockEntity.getBlockState(), 0);
			if (tintSource != null) {
				state.plantColor = tintSource.color(blockEntity.getBlockState());
			}
		}
		if (blockEntity.hasWeeds()) {
			AgriGrowthStage weedStage = blockEntity.getWeedGrowthStage();
			String weedId = blockEntity.getWeedId();
			state.weedModel = AgriClientApi.getWeedModel(weedId, weedStage.index());
		}
	}

	@Override
	public void submit(CropRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
		RandomSource random = RandomSource.create(42L);
		if (state.sticksModel != null) {
			List<BlockStateModelPart> parts = new ArrayList<>();
			state.sticksModel.collectParts(random, parts);
			collector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), parts, new int[]{-1}, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}
		if (state.plantModel != null) {
			List<BlockStateModelPart> parts = new ArrayList<>();
			state.plantModel.collectParts(random, parts);
			if (state.plantColor == -1) {
				collector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), parts, new int[]{-1}, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			} else {
				collector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), parts, new int[]{0xFF000000 | state.plantColor}, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			}
		}
		if (state.weedModel != null) {
			List<BlockStateModelPart> parts = new ArrayList<>();
			state.weedModel.collectParts(random, parts);
			collector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), parts, new int[]{-1}, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}
	}

}
