package com.agricraft.agricraft.api.config;

/**
 * AgriCraft main configuration values.
 */
public final class CoreConfig {

	private CoreConfig() {
	}

	public static boolean enablePacksByDefault = true;
	public static boolean cropSticksPreventTrampling = true;
	public static boolean plantOffCropSticks = true;
	public static boolean cropSticksCollide = true;
	public static boolean onlyFertileCropsSpread = false;
	public static boolean allowFertilizerMutation = true;
	public static boolean cloneMutations = false;
	public static boolean overrideVanillaFarming = true;
	public static boolean convertSeedsOnlyInAnalyzer = false;
	public static double growthMultiplier = 1.0;
	public static boolean onlyMatureSeedDrops = false;
	public static boolean disableWeeds = false;
	public static boolean matureWeedsKillPlants = true;
	public static boolean weedsSpreading = true;
	public static boolean weedsDestroyCropSticks = false;
	public static boolean disableFertilizerWeeds = true;
	public static boolean rakingDropsItems = true;
	public static float seedCompostValue = 0.3F;
	public static int seedBagCapacity = 64;
	public static int seedBagEnchantCost = 10;

}
