package com.agricraft.agricraft.client.neoforge;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.client.AgriCraftClient;
import com.agricraft.agricraft.client.bewlr.AgriSeedBEWLR;
import com.agricraft.agricraft.client.ber.CropBlockEntityRenderer;
import com.agricraft.agricraft.client.ber.SeedAnalyzerEntityRenderer;
import com.agricraft.agricraft.client.gui.MagnifyingGlassOverlay;
import com.agricraft.agricraft.client.gui.SeedAnalyzerScreen;
import com.agricraft.agricraft.common.config.neoforge.NeoForgeMenuConfig;
import com.agricraft.agricraft.common.registry.ModBlockEntityTypes;
import com.agricraft.agricraft.common.registry.ModMenus;
import com.agricraft.agricraft.common.util.PlatformClient;
import com.agricraft.agricraft.common.util.neoforge.NeoForgePlatformClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NeoForge client event handler in the mod event bus
 */
@EventBusSubscriber(modid = AgriApi.MOD_ID, value = Dist.CLIENT)
public class AgriCraftNeoForgeClient {

	static final Map<Identifier, StandaloneModelKey<BlockStateModel>> MODEL_KEYS = new ConcurrentHashMap<>();

	public static StandaloneModelKey<BlockStateModel> getModelKey(Identifier id) {
		return MODEL_KEYS.get(id);
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		PlatformClient.setup(new NeoForgePlatformClient());
		AgriCraftNeoForgeClient.init();
	}

	private static StandaloneModelKey<BlockStateModel> registerModel(ModelEvent.RegisterStandalone event, Identifier id) {
		StandaloneModelKey<BlockStateModel> key = new StandaloneModelKey<>(() -> id.toString());
		event.register(key, SimpleUnbakedStandaloneModel.blockStateModel(id));
		MODEL_KEYS.put(id, key);
		return key;
	}

	@SubscribeEvent
	public static void loadModels(ModelEvent.RegisterStandalone event) {
		for (Map.Entry<Identifier, Resource> entry : FileToIdConverter.json("models/seed").listMatchingResources(Minecraft.getInstance().getResourceManager()).entrySet()) {
			Identifier seed = Identifier.parse(entry.getKey().toString().replace("models/seed", "seed").replace(".json", ""));
			registerModel(event, seed);
		}
		for (Map.Entry<Identifier, Resource> entry : FileToIdConverter.json("models/crop").listMatchingResources(Minecraft.getInstance().getResourceManager()).entrySet()) {
			Identifier seed = Identifier.parse(entry.getKey().toString().replace("models/crop", "crop").replace(".json", ""));
			registerModel(event, seed);
		}
		for (Map.Entry<Identifier, Resource> entry : FileToIdConverter.json("models/weed").listMatchingResources(Minecraft.getInstance().getResourceManager()).entrySet()) {
			Identifier seed = Identifier.parse(entry.getKey().toString().replace("models/weed", "weed").replace(".json", ""));
			registerModel(event, seed);
		}
		registerModel(event, Identifier.parse("agricraft:block/wooden_crop_sticks"));
		registerModel(event, Identifier.parse("agricraft:block/iron_crop_sticks"));
		registerModel(event, Identifier.parse("agricraft:block/obsidian_crop_sticks"));
		registerModel(event, Identifier.parse("agricraft:block/wooden_cross_crop_sticks"));
		registerModel(event, Identifier.parse("agricraft:block/iron_cross_crop_sticks"));
		registerModel(event, Identifier.parse("agricraft:block/obsidian_cross_crop_sticks"));
	}

	@SubscribeEvent
	public static void registerSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
		event.register(Identifier.fromNamespaceAndPath(AgriApi.MOD_ID, "seed"), AgriSeedBEWLR.Unbaked.MAP_CODEC);
	}

	@SubscribeEvent
	public static void registerBer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CROP.get(), CropBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.SEED_ANALYZER.get(), SeedAnalyzerEntityRenderer::new);
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.HOTBAR, Identifier.fromNamespaceAndPath(AgriApi.MOD_ID, "magnifying_glass_info"), (guiGraphics, deltaTracker) -> MagnifyingGlassOverlay.renderOverlay(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false)));
	}

	@SubscribeEvent
	public static void registerMenuScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
		event.register(ModMenus.SEED_ANALYZER_MENU.get(), SeedAnalyzerScreen::new);
	}

	public static void init() {
		AgriCraftClient.init();
		ModList.get().getModContainerById(AgriApi.MOD_ID).ifPresent(NeoForgeMenuConfig::register);
	}

}
