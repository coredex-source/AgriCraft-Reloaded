package com.agricraft.agricraft.client;

import com.agricraft.agricraft.client.gui.JournalScreen;
import com.agricraft.agricraft.common.block.CropStickVariant;
import com.agricraft.agricraft.common.item.JournalItem;
import com.agricraft.agricraft.common.util.PlatformClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClientUtil {

	public static String getModelForSticks(CropStickVariant variant) {
		return switch (variant) {
			case WOODEN -> "agricraft:block/wooden_crop_sticks";
			case IRON -> "agricraft:block/iron_crop_sticks";
			case OBSIDIAN -> "agricraft:block/obsidian_crop_sticks";
		};
	}

	public static void spawnParticlesForPlant(String plantModelId, LevelAccessor level, BlockState state, BlockPos pos, VoxelShape voxelShape) {
		BlockStateModel model = PlatformClient.get().getStandaloneModel(Identifier.parse(plantModelId));
		spawnParticlesForShape(model, level, state, pos, voxelShape);
	}

	public static void spawnParticlesForSticks(CropStickVariant variant, LevelAccessor level, BlockState state, BlockPos pos, VoxelShape voxelShape) {
		String modelId = getModelForSticks(variant);
		BlockStateModel model = PlatformClient.get().getStandaloneModel(Identifier.parse(modelId));
		spawnParticlesForShape(model, level, state, pos, voxelShape);
	}

	public static void spawnParticlesForShape(BlockStateModel model, LevelAccessor level, BlockState state, BlockPos pos, VoxelShape voxelShape) {
		if (model == null) {
			return;
		}
		TextureAtlasSprite particleIcon = model.particleMaterial().sprite();
		if (particleIcon == null) {
			return;
		}

		voxelShape.forAllBoxes((startX, startY, startZ, endX, endY, endZ) -> {
			double xBoxes = Math.min(1.0, endX - startX);
			double yBoxes = Math.min(1.0, endY - startY);
			double zBoxes = Math.min(1.0, endZ - startZ);
			int maxX = Math.max(2, Mth.ceil(xBoxes / 0.25));
			int maxY = Math.max(2, Mth.ceil(yBoxes / 0.25));
			int maxZ = Math.max(2, Mth.ceil(zBoxes / 0.25));
			for (int p = 0; p < maxX; ++p) {
				for (int q = 0; q < maxY; ++q) {
					for (int r = 0; r < maxZ; ++r) {
						double dx = (p + 0.5D) / maxX;
						double dy = (q + 0.5D) / maxY;
						double dz = (r + 0.5D) / maxZ;
						double ox = dx * xBoxes + startX;
						double oy = dy * yBoxes + startY;
						double oz = dz * zBoxes + startZ;
						TerrainParticle particle = new TerrainParticle((ClientLevel) level, pos.getX() + ox,
								pos.getY() + oy, pos.getZ() + oz, dx - 0.5, dy - 0.5, dz - 0.5, state, pos);
						Minecraft.getInstance().particleEngine.add(particle);
					}
				}
			}
		});
	}

	public static void openJournalScreen(Player player, InteractionHand hand) {
		Minecraft.getInstance().gui.setScreen(new JournalScreen(JournalItem.getJournalData(player.getItemInHand(hand))));
	}

}
