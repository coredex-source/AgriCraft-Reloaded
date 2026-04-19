package com.agricraft.agricraft.client;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.client.ber.CropBlockEntityRenderer;
import com.agricraft.agricraft.client.ber.SeedAnalyzerEntityRenderer;
import com.agricraft.agricraft.client.bewlr.AgriSeedBEWLR;
import com.agricraft.agricraft.client.gui.MagnifyingGlassOverlay;
import com.agricraft.agricraft.client.gui.SeedAnalyzerScreen;
import com.agricraft.agricraft.fabric.mixin.SpecialModelRenderersAccessor;
import com.agricraft.agricraft.common.registry.ModBlockEntityTypes;
import com.agricraft.agricraft.common.registry.ModBlocks;
import com.agricraft.agricraft.common.registry.ModMenus;
import com.agricraft.agricraft.common.util.LangUtils;
import com.agricraft.agricraft.common.util.PlatformClient;
import com.agricraft.agricraft.common.util.fabric.FabricPlatformClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.component.CustomData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AgriCraftFabricClient implements ClientModInitializer {

	static final Map<Identifier, ExtraModelKey<BlockStateModel>> MODEL_KEYS = new ConcurrentHashMap<>();

	public static ExtraModelKey<BlockStateModel> getModelKey(Identifier id) {
		return MODEL_KEYS.get(id);
	}

	private static ExtraModelKey<BlockStateModel> registerModel(ModelLoadingPlugin.Context pluginContext, Identifier id) {
		ExtraModelKey<BlockStateModel> key = ExtraModelKey.create(() -> id.toString());
		pluginContext.addModel(key, SimpleUnbakedExtraModel.blockStateModel(id));
		MODEL_KEYS.put(id, key);
		return key;
	}

	@Override
	public void onInitializeClient() {
		SpecialModelRenderersAccessor.agricraft$getIdMapper().put(Identifier.fromNamespaceAndPath(AgriApi.MOD_ID, "seed"), AgriSeedBEWLR.Unbaked.MAP_CODEC);
		PlatformClient.setup(new FabricPlatformClient());
		AgriCraftClient.init();
		ModelLoadingPlugin.register(pluginContext -> {
			for (Map.Entry<Identifier, Resource> entry : FileToIdConverter.json("models/seed").listMatchingResources(Minecraft.getInstance().getResourceManager()).entrySet()) {
				Identifier seed = Identifier.fromNamespaceAndPath(entry.getKey().getNamespace(), entry.getKey().getPath().replace("models/seed", "seed").replace(".json", ""));
				registerModel(pluginContext, seed);
			}
			for (Map.Entry<Identifier, Resource> entry : FileToIdConverter.json("models/crop").listMatchingResources(Minecraft.getInstance().getResourceManager()).entrySet()) {
				Identifier crop = Identifier.fromNamespaceAndPath(entry.getKey().getNamespace(), entry.getKey().getPath().replace("models/crop", "crop").replace(".json", ""));
				registerModel(pluginContext, crop);
			}
			for (Map.Entry<Identifier, Resource> entry : FileToIdConverter.json("models/weed").listMatchingResources(Minecraft.getInstance().getResourceManager()).entrySet()) {
				Identifier crop = Identifier.fromNamespaceAndPath(entry.getKey().getNamespace(), entry.getKey().getPath().replace("models/weed", "weed").replace(".json", ""));
				registerModel(pluginContext, crop);
			}
			// add the crop sticks models else they're not loaded
			registerModel(pluginContext, Identifier.parse("agricraft:block/wooden_crop_sticks"));
			registerModel(pluginContext, Identifier.parse("agricraft:block/iron_crop_sticks"));
			registerModel(pluginContext, Identifier.parse("agricraft:block/obsidian_crop_sticks"));
			registerModel(pluginContext, Identifier.parse("agricraft:block/wooden_cross_crop_sticks"));
			registerModel(pluginContext, Identifier.parse("agricraft:block/iron_cross_crop_sticks"));
			registerModel(pluginContext, Identifier.parse("agricraft:block/obsidian_cross_crop_sticks"));
		});

		BlockEntityRenderers.register(ModBlockEntityTypes.CROP.get(), CropBlockEntityRenderer::new);
		BlockEntityRenderers.register(ModBlockEntityTypes.SEED_ANALYZER.get(), SeedAnalyzerEntityRenderer::new);
		MenuScreens.register(ModMenus.SEED_ANALYZER_MENU.get(), SeedAnalyzerScreen::new);

		HudRenderCallback.EVENT.register((guiGraphics, deltaTracker) -> {
			MagnifyingGlassOverlay.renderOverlay(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false));
		});
		ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
			if (stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("magnifying", false)) {
				lines.add(1, Component.translatable("agricraft.tooltip.magnifying").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
			}
			AgriApi.getSoilRegistry().ifPresent(registry -> registry.forEach(soil -> {
						if (soil.isVariant(stack.getItem())) {
							lines.add(1, Component.translatable("agricraft.tooltip.magnifying.soil.soil").withStyle(ChatFormatting.DARK_GRAY));
							lines.add(2, Component.literal("  ").withStyle(ChatFormatting.DARK_GRAY)
									.append(Component.translatable("agricraft.tooltip.magnifying.soil.humidity"))
									.append(LangUtils.soilPropertyName("humidity", soil.humidity())));
							lines.add(3, Component.literal("  ").withStyle(ChatFormatting.DARK_GRAY)
									.append(Component.translatable("agricraft.tooltip.magnifying.soil.acidity"))
									.append(LangUtils.soilPropertyName("acidity", soil.acidity())));
							lines.add(4, Component.literal("  ").withStyle(ChatFormatting.DARK_GRAY)
									.append(Component.translatable("agricraft.tooltip.magnifying.soil.nutrients"))
									.append(LangUtils.soilPropertyName("nutrients", soil.nutrients())));
						}
					}
			));
		});
		BlockRenderLayerMap.putBlock(ModBlocks.SEED_ANALYZER.get(), ChunkSectionLayer.CUTOUT);
	}

}
