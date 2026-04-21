package com.agricraft.agricraft.common.config.neoforge;

import com.agricraft.agricraft.AgriCraft;
import com.agricraft.agricraft.api.config.AgriCraftConfig;
import com.agricraft.agricraft.api.config.CompatConfig;
import com.agricraft.agricraft.api.config.CoreConfig;
import com.agricraft.agricraft.api.config.StatsConfig;
import dev.eclipseui.EclipseUI;
import dev.eclipseui.api.Theme;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class NeoForgeMenuConfig {

	/**
	 * Register our configuration menu to the modlist menu
	 */
	public static void register(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, (minecraft, parent) -> createConfigScreen(parent));
	}

	private static Screen createConfigScreen(Screen parent) {
		AgriCraft.CONFIGURATOR.loadConfig(AgriCraftConfig.class);

		return EclipseUI.configScreen()
				.title(Component.translatable("config.agricraft.title"))
				.parent(parent)
				.theme(Theme.MODERN)
				.onSave(() -> AgriCraft.CONFIGURATOR.saveConfig(AgriCraftConfig.class))
				.category(cat -> cat
						.name(Component.translatable("config.agricraft.core"))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.register_packs_by_default")).binding(() -> CoreConfig.enablePacksByDefault, v -> CoreConfig.enablePacksByDefault = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.crop_sticks_prevent_trampling")).binding(() -> CoreConfig.cropSticksPreventTrampling, v -> CoreConfig.cropSticksPreventTrampling = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.plant_off_crop_sticks")).binding(() -> CoreConfig.plantOffCropSticks, v -> CoreConfig.plantOffCropSticks = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.crop_sticks_collide")).binding(() -> CoreConfig.cropSticksCollide, v -> CoreConfig.cropSticksCollide = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.only_fertile_crops_spread")).binding(() -> CoreConfig.onlyFertileCropsSpread, v -> CoreConfig.onlyFertileCropsSpread = v).defaultValue(false))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.allow_fertilizer_mutation")).binding(() -> CoreConfig.allowFertilizerMutation, v -> CoreConfig.allowFertilizerMutation = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.clone_mutations")).binding(() -> CoreConfig.cloneMutations, v -> CoreConfig.cloneMutations = v).defaultValue(false))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.override_vanilla_farming")).binding(() -> CoreConfig.overrideVanillaFarming, v -> CoreConfig.overrideVanillaFarming = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.converts_seed_only_in_analyzer")).binding(() -> CoreConfig.convertSeedsOnlyInAnalyzer, v -> CoreConfig.convertSeedsOnlyInAnalyzer = v).defaultValue(false))
						.slider(s -> s.name(Component.translatable("config.agricraft.core.growth_multiplier")).range(0.0, 3.0, 0.05).bindingDouble(() -> CoreConfig.growthMultiplier, v -> CoreConfig.growthMultiplier = v).defaultValue(1.0))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.only_mature_seed_drops")).binding(() -> CoreConfig.onlyMatureSeedDrops, v -> CoreConfig.onlyMatureSeedDrops = v).defaultValue(false))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.disable_weeds")).binding(() -> CoreConfig.disableWeeds, v -> CoreConfig.disableWeeds = v).defaultValue(false))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.mature_weeds_kill_plants")).binding(() -> CoreConfig.matureWeedsKillPlants, v -> CoreConfig.matureWeedsKillPlants = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.weeds_spreading")).binding(() -> CoreConfig.weedsSpreading, v -> CoreConfig.weedsSpreading = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.weeds_destroy_crop_sticks")).binding(() -> CoreConfig.weedsDestroyCropSticks, v -> CoreConfig.weedsDestroyCropSticks = v).defaultValue(false))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.disable_fertilizer_weeds")).binding(() -> CoreConfig.disableFertilizerWeeds, v -> CoreConfig.disableFertilizerWeeds = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.core.raking_drops_items")).binding(() -> CoreConfig.rakingDropsItems, v -> CoreConfig.rakingDropsItems = v).defaultValue(true))
						.slider(s -> s.name(Component.translatable("config.agricraft.core.seed_compost_value")).range(0.0, 1.0, 0.05).bindingDouble(() -> (double) CoreConfig.seedCompostValue, v -> CoreConfig.seedCompostValue = v.floatValue()).defaultValue(0.3).percentageFormat())
						.slider(s -> s.name(Component.translatable("config.agricraft.core.seed_bag_capactity")).range(8, 256, 1).bindingInt(() -> CoreConfig.seedBagCapacity, v -> CoreConfig.seedBagCapacity = v).defaultValue(64))
						.slider(s -> s.name(Component.translatable("config.agricraft.core.seed_bag_enchant_cost")).range(0, 30, 1).bindingInt(() -> CoreConfig.seedBagEnchantCost, v -> CoreConfig.seedBagEnchantCost = v).defaultValue(10))
				)
				.category(cat -> cat
						.name(Component.translatable("config.agricraft.stats"))
						.label(Component.literal("Gain"))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.gain_min")).range(1, 10, 1).bindingInt(() -> StatsConfig.gainMin, v -> StatsConfig.gainMin = v).defaultValue(1))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.gain_max")).range(1, 10, 1).bindingInt(() -> StatsConfig.gainMax, v -> StatsConfig.gainMax = v).defaultValue(10))
						.toggle(t -> t.name(Component.translatable("config.agricraft.stats.gain_hidden")).binding(() -> StatsConfig.gainHidden, v -> StatsConfig.gainHidden = v).defaultValue(false))
						.separator()
						.label(Component.literal("Growth"))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.growth_min")).range(1, 10, 1).bindingInt(() -> StatsConfig.growthMin, v -> StatsConfig.growthMin = v).defaultValue(1))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.growth_max")).range(1, 10, 1).bindingInt(() -> StatsConfig.growthMax, v -> StatsConfig.growthMax = v).defaultValue(10))
						.toggle(t -> t.name(Component.translatable("config.agricraft.stats.growth_hidden")).binding(() -> StatsConfig.growthHidden, v -> StatsConfig.growthHidden = v).defaultValue(false))
						.separator()
						.label(Component.literal("Strength"))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.strength_min")).range(1, 10, 1).bindingInt(() -> StatsConfig.strengthMin, v -> StatsConfig.strengthMin = v).defaultValue(1))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.strength_max")).range(1, 10, 1).bindingInt(() -> StatsConfig.strengthMax, v -> StatsConfig.strengthMax = v).defaultValue(10))
						.toggle(t -> t.name(Component.translatable("config.agricraft.stats.strength_hidden")).binding(() -> StatsConfig.strengthHidden, v -> StatsConfig.strengthHidden = v).defaultValue(false))
						.separator()
						.label(Component.literal("Resistance"))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.resistance_min")).range(1, 10, 1).bindingInt(() -> StatsConfig.resistanceMin, v -> StatsConfig.resistanceMin = v).defaultValue(1))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.resistance_max")).range(1, 10, 1).bindingInt(() -> StatsConfig.resistanceMax, v -> StatsConfig.resistanceMax = v).defaultValue(10))
						.toggle(t -> t.name(Component.translatable("config.agricraft.stats.resistance_hidden")).binding(() -> StatsConfig.resistanceHidden, v -> StatsConfig.resistanceHidden = v).defaultValue(false))
						.separator()
						.label(Component.literal("Fertility"))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.fertility_min")).range(1, 10, 1).bindingInt(() -> StatsConfig.fertilityMin, v -> StatsConfig.fertilityMin = v).defaultValue(1))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.fertility_max")).range(1, 10, 1).bindingInt(() -> StatsConfig.fertilityMax, v -> StatsConfig.fertilityMax = v).defaultValue(10))
						.toggle(t -> t.name(Component.translatable("config.agricraft.stats.fertility_hidden")).binding(() -> StatsConfig.fertilityHidden, v -> StatsConfig.fertilityHidden = v).defaultValue(false))
						.separator()
						.label(Component.literal("Mutativity"))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.mutativity_min")).range(1, 10, 1).bindingInt(() -> StatsConfig.mutativityMin, v -> StatsConfig.mutativityMin = v).defaultValue(1))
						.slider(s -> s.name(Component.translatable("config.agricraft.stats.mutativity_max")).range(1, 10, 1).bindingInt(() -> StatsConfig.mutativityMax, v -> StatsConfig.mutativityMax = v).defaultValue(10))
						.toggle(t -> t.name(Component.translatable("config.agricraft.stats.mutativity_hidden")).binding(() -> StatsConfig.mutativityHidden, v -> StatsConfig.mutativityHidden = v).defaultValue(false))
				)
				.category(cat -> cat
						.name(Component.translatable("config.agricraft.compat"))
						.toggle(t -> t.name(Component.translatable("config.agricraft.compat.mysticalagriculture")).binding(() -> CompatConfig.enableMysticalAgriculture, v -> CompatConfig.enableMysticalAgriculture = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.compat.pneumaticcraft")).binding(() -> CompatConfig.enablePneumaticCraft, v -> CompatConfig.enablePneumaticCraft = v).defaultValue(true))
						.toggle(t -> t.name(Component.translatable("config.agricraft.compat.industrialforegoing")).binding(() -> CompatConfig.enableIndustrialForegoing, v -> CompatConfig.enableIndustrialForegoing = v).defaultValue(true))
				)
				.build();
	}

}
