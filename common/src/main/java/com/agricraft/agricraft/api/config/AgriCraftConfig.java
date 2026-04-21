package com.agricraft.agricraft.api.config;

import com.agricraft.agricraft.AgriCraft;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class AgriCraftConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static final String CONFIG_FILE = "agricraft.jsonc";
	private static final String LEGACY_CONFIG_FILE = "agricraft.json";

	private AgriCraftConfig() {
	}

	public static void load(Path configDirectory) {
		Path configPath = resolveConfigForRead(configDirectory);
		if (configPath == null) {
			save(configDirectory);
			return;
		}

		try (Reader reader = Files.newBufferedReader(configPath)) {
			JsonReader jsonReader = new JsonReader(reader);
			jsonReader.setLenient(true);
			JsonObject root = GSON.fromJson(jsonReader, JsonObject.class);
			if (root == null) {
				AgriCraft.LOGGER.warn("Invalid AgriCraft config format in {}", configPath);
				return;
			}
			apply(root);
		} catch (Exception e) {
			AgriCraft.LOGGER.warn("Failed to load AgriCraft config from {}", configPath, e);
			return;
		}

		if (LEGACY_CONFIG_FILE.equals(configPath.getFileName().toString())) {
			save(configDirectory);
		}
	}

	public static void save(Path configDirectory) {
		if (configDirectory == null) {
			AgriCraft.LOGGER.warn("Skipping AgriCraft config save: config directory is null");
			return;
		}
		try {
			Files.createDirectories(configDirectory);
		} catch (IOException e) {
			AgriCraft.LOGGER.warn("Failed to create config directory {}", configDirectory, e);
			return;
		}

		Path configPath = configDirectory.resolve(CONFIG_FILE);
		JsonObject root = new JsonObject();
		root.addProperty("rconfig:version", 0);
		root.add("core", writeCore());
		root.add("stats", writeStats());
		root.add("compat", writeCompat());

		try (Writer writer = Files.newBufferedWriter(configPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
			GSON.toJson(root, writer);
		} catch (Exception e) {
			AgriCraft.LOGGER.warn("Failed to save AgriCraft config to {}", configPath, e);
		}
	}

	private static Path resolveConfigForRead(Path configDirectory) {
		if (configDirectory == null) {
			AgriCraft.LOGGER.warn("Skipping AgriCraft config load: config directory is null");
			return null;
		}

		Path configPath = configDirectory.resolve(CONFIG_FILE);
		if (Files.exists(configPath)) {
			return configPath;
		}

		Path legacyPath = configDirectory.resolve(LEGACY_CONFIG_FILE);
		if (Files.exists(legacyPath)) {
			return legacyPath;
		}

		return null;
	}

	private static void apply(JsonObject root) {
		JsonObject core = object(root, "core");
		CoreConfig.enablePacksByDefault = bool(core, "register_packs_by_default", CoreConfig.enablePacksByDefault);
		CoreConfig.cropSticksPreventTrampling = bool(core, "crop_sticks_prevent_trampling", CoreConfig.cropSticksPreventTrampling);
		CoreConfig.plantOffCropSticks = bool(core, "plant_off_crop_sticks", CoreConfig.plantOffCropSticks);
		CoreConfig.cropSticksCollide = bool(core, "crop_sticks_collide", CoreConfig.cropSticksCollide);
		CoreConfig.onlyFertileCropsSpread = bool(core, "only_fertile_crops_spread", CoreConfig.onlyFertileCropsSpread);
		CoreConfig.allowFertilizerMutation = bool(core, "allow_fertilizer_mutation", CoreConfig.allowFertilizerMutation);
		CoreConfig.cloneMutations = bool(core, "clone_mutations", CoreConfig.cloneMutations);
		CoreConfig.overrideVanillaFarming = bool(core, "override_vanilla_farming", CoreConfig.overrideVanillaFarming);
		CoreConfig.convertSeedsOnlyInAnalyzer = bool(core, "converts_seed_only_in_analyzer", CoreConfig.convertSeedsOnlyInAnalyzer);
		CoreConfig.growthMultiplier = decimal(core, "growth_multiplier", CoreConfig.growthMultiplier, 0.0, 3.0);
		CoreConfig.onlyMatureSeedDrops = bool(core, "only_mature_seed_drops", CoreConfig.onlyMatureSeedDrops);
		CoreConfig.disableWeeds = bool(core, "disable_weeds", CoreConfig.disableWeeds);
		CoreConfig.matureWeedsKillPlants = bool(core, "mature_weeds_kill_plants", CoreConfig.matureWeedsKillPlants);
		CoreConfig.weedsSpreading = bool(core, "weeds_spreading", CoreConfig.weedsSpreading);
		CoreConfig.weedsDestroyCropSticks = bool(core, "weeds_destroy_crop_sticks", CoreConfig.weedsDestroyCropSticks);
		CoreConfig.disableFertilizerWeeds = bool(core, "disable_fertilizer_weeds", CoreConfig.disableFertilizerWeeds);
		CoreConfig.rakingDropsItems = bool(core, "raking_drops_items", CoreConfig.rakingDropsItems);
		CoreConfig.seedCompostValue = floating(core, "seed_compost_value", CoreConfig.seedCompostValue, 0.0F, 1.0F);
		CoreConfig.seedBagCapacity = integer(core, "seed_bag_capactity", CoreConfig.seedBagCapacity, 8, 256);
		CoreConfig.seedBagEnchantCost = integer(core, "seed_bag_enchant_cost", CoreConfig.seedBagEnchantCost, 0, 30);

		JsonObject stats = object(root, "stats");
		StatsConfig.gainMin = integer(stats, "gain_min", StatsConfig.gainMin, 1, 10);
		StatsConfig.gainMax = integer(stats, "gain_max", StatsConfig.gainMax, 1, 10);
		StatsConfig.gainHidden = bool(stats, "gain_hidden", StatsConfig.gainHidden);
		StatsConfig.growthMin = integer(stats, "growth_min", StatsConfig.growthMin, 1, 10);
		StatsConfig.growthMax = integer(stats, "growth_max", StatsConfig.growthMax, 1, 10);
		StatsConfig.growthHidden = bool(stats, "growth_hidden", StatsConfig.growthHidden);
		StatsConfig.strengthMin = integer(stats, "strength_min", StatsConfig.strengthMin, 1, 10);
		StatsConfig.strengthMax = integer(stats, "strength_max", StatsConfig.strengthMax, 1, 10);
		StatsConfig.strengthHidden = bool(stats, "strength_hidden", StatsConfig.strengthHidden);
		StatsConfig.resistanceMin = integer(stats, "resistance_min", StatsConfig.resistanceMin, 1, 10);
		StatsConfig.resistanceMax = integer(stats, "resistance_max", StatsConfig.resistanceMax, 1, 10);
		StatsConfig.resistanceHidden = bool(stats, "resistance_hidden", StatsConfig.resistanceHidden);
		StatsConfig.fertilityMin = integer(stats, "fertility_min", StatsConfig.fertilityMin, 1, 10);
		StatsConfig.fertilityMax = integer(stats, "fertility_max", StatsConfig.fertilityMax, 1, 10);
		StatsConfig.fertilityHidden = bool(stats, "fertility_hidden", StatsConfig.fertilityHidden);
		StatsConfig.mutativityMin = integer(stats, "mutativity_min", StatsConfig.mutativityMin, 1, 10);
		StatsConfig.mutativityMax = integer(stats, "mutativity_max", StatsConfig.mutativityMax, 1, 10);
		StatsConfig.mutativityHidden = bool(stats, "mutativity_hidden", StatsConfig.mutativityHidden);

		JsonObject compat = object(root, "compat");
		CompatConfig.enableMysticalAgriculture = bool(compat, "mysticalagriculture", CompatConfig.enableMysticalAgriculture);
		CompatConfig.enablePneumaticCraft = bool(compat, "pneumaticcraft", CompatConfig.enablePneumaticCraft);
		CompatConfig.enableIndustrialForegoing = bool(compat, "industrialforegoing", CompatConfig.enableIndustrialForegoing);
	}

	private static JsonObject writeCore() {
		JsonObject core = new JsonObject();
		core.addProperty("register_packs_by_default", CoreConfig.enablePacksByDefault);
		core.addProperty("crop_sticks_prevent_trampling", CoreConfig.cropSticksPreventTrampling);
		core.addProperty("plant_off_crop_sticks", CoreConfig.plantOffCropSticks);
		core.addProperty("crop_sticks_collide", CoreConfig.cropSticksCollide);
		core.addProperty("only_fertile_crops_spread", CoreConfig.onlyFertileCropsSpread);
		core.addProperty("allow_fertilizer_mutation", CoreConfig.allowFertilizerMutation);
		core.addProperty("clone_mutations", CoreConfig.cloneMutations);
		core.addProperty("override_vanilla_farming", CoreConfig.overrideVanillaFarming);
		core.addProperty("converts_seed_only_in_analyzer", CoreConfig.convertSeedsOnlyInAnalyzer);
		core.addProperty("growth_multiplier", CoreConfig.growthMultiplier);
		core.addProperty("only_mature_seed_drops", CoreConfig.onlyMatureSeedDrops);
		core.addProperty("disable_weeds", CoreConfig.disableWeeds);
		core.addProperty("mature_weeds_kill_plants", CoreConfig.matureWeedsKillPlants);
		core.addProperty("weeds_spreading", CoreConfig.weedsSpreading);
		core.addProperty("weeds_destroy_crop_sticks", CoreConfig.weedsDestroyCropSticks);
		core.addProperty("disable_fertilizer_weeds", CoreConfig.disableFertilizerWeeds);
		core.addProperty("raking_drops_items", CoreConfig.rakingDropsItems);
		core.addProperty("seed_compost_value", CoreConfig.seedCompostValue);
		core.addProperty("seed_bag_capactity", CoreConfig.seedBagCapacity);
		core.addProperty("seed_bag_enchant_cost", CoreConfig.seedBagEnchantCost);
		return core;
	}

	private static JsonObject writeStats() {
		JsonObject stats = new JsonObject();
		stats.addProperty("gain_min", StatsConfig.gainMin);
		stats.addProperty("gain_max", StatsConfig.gainMax);
		stats.addProperty("gain_hidden", StatsConfig.gainHidden);
		stats.addProperty("growth_min", StatsConfig.growthMin);
		stats.addProperty("growth_max", StatsConfig.growthMax);
		stats.addProperty("growth_hidden", StatsConfig.growthHidden);
		stats.addProperty("strength_min", StatsConfig.strengthMin);
		stats.addProperty("strength_max", StatsConfig.strengthMax);
		stats.addProperty("strength_hidden", StatsConfig.strengthHidden);
		stats.addProperty("resistance_min", StatsConfig.resistanceMin);
		stats.addProperty("resistance_max", StatsConfig.resistanceMax);
		stats.addProperty("resistance_hidden", StatsConfig.resistanceHidden);
		stats.addProperty("fertility_min", StatsConfig.fertilityMin);
		stats.addProperty("fertility_max", StatsConfig.fertilityMax);
		stats.addProperty("fertility_hidden", StatsConfig.fertilityHidden);
		stats.addProperty("mutativity_min", StatsConfig.mutativityMin);
		stats.addProperty("mutativity_max", StatsConfig.mutativityMax);
		stats.addProperty("mutativity_hidden", StatsConfig.mutativityHidden);
		return stats;
	}

	private static JsonObject writeCompat() {
		JsonObject compat = new JsonObject();
		compat.addProperty("mysticalagriculture", CompatConfig.enableMysticalAgriculture);
		compat.addProperty("pneumaticcraft", CompatConfig.enablePneumaticCraft);
		compat.addProperty("industrialforegoing", CompatConfig.enableIndustrialForegoing);
		return compat;
	}

	private static JsonObject object(JsonObject root, String key) {
		JsonElement element = root.get(key);
		if (element != null && element.isJsonObject()) {
			return element.getAsJsonObject();
		}
		return new JsonObject();
	}

	private static boolean bool(JsonObject object, String key, boolean fallback) {
		JsonElement element = object.get(key);
		if (element == null || !element.isJsonPrimitive()) {
			return fallback;
		}
		try {
			return element.getAsBoolean();
		} catch (Exception ignored) {
			return fallback;
		}
	}

	private static int integer(JsonObject object, String key, int fallback, int min, int max) {
		JsonElement element = object.get(key);
		if (element == null || !element.isJsonPrimitive()) {
			return fallback;
		}
		try {
			return clamp(element.getAsInt(), min, max);
		} catch (Exception ignored) {
			return fallback;
		}
	}

	private static double decimal(JsonObject object, String key, double fallback, double min, double max) {
		JsonElement element = object.get(key);
		if (element == null || !element.isJsonPrimitive()) {
			return fallback;
		}
		try {
			return clamp(element.getAsDouble(), min, max);
		} catch (Exception ignored) {
			return fallback;
		}
	}

	private static float floating(JsonObject object, String key, float fallback, float min, float max) {
		JsonElement element = object.get(key);
		if (element == null || !element.isJsonPrimitive()) {
			return fallback;
		}
		try {
			return clamp(element.getAsFloat(), min, max);
		} catch (Exception ignored) {
			return fallback;
		}
	}

	private static int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

	private static double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	private static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}

}
