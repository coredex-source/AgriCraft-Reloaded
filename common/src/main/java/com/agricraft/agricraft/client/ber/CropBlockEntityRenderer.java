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
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

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
			state.plantColor = Minecraft.getInstance().getBlockColors().getColor(blockEntity.getBlockState(), blockEntity.getLevel(), blockEntity.getBlockPos(), 0);
		}
		if (blockEntity.hasWeeds()) {
			AgriGrowthStage weedStage = blockEntity.getWeedGrowthStage();
			String weedId = blockEntity.getWeedId();
			state.weedModel = AgriClientApi.getWeedModel(weedId, weedStage.index());
		}
	}

	@Override
	public void submit(CropRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
		if (state.sticksModel != null) {
			collector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), state.sticksModel, 1, 1, 1, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}
		if (state.plantModel != null) {
			if (state.plantColor == -1) {
				collector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), state.plantModel, 1, 1, 1, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			} else {
				float r = (state.plantColor >> 16 & 0xFF) / 255.0F;
				float g = (state.plantColor >> 8 & 0xFF) / 255.0F;
				float b = (state.plantColor & 0xFF) / 255.0F;
				collector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), state.plantModel, r, g, b, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			}
		}
		if (state.weedModel != null) {
			collector.submitBlockModel(poseStack, Sheets.cutoutBlockSheet(), state.weedModel, 1, 1, 1, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}
	}

}
