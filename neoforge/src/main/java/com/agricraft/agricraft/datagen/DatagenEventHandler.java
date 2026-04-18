package com.agricraft.agricraft.datagen;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.datagen.model.BlockModelBuilder;
import com.agricraft.agricraft.datagen.model.ItemModelBuilder;
import com.agricraft.agricraft.datagen.model.ModelBuilder;
import com.agricraft.agricraft.datagen.model.ModelProvider;
import net.minecraft.DetectedVersion;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@EventBusSubscriber(modid = AgriApi.MOD_ID)
public class DatagenEventHandler {

	private static final boolean biomesoplenty = true;
	private static final boolean immersiveengineering = true;
	private static final boolean pamhc2crops = true;
	private static final boolean mysticalagriculture = true;
	private static final boolean farmersdelight = true;

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		boolean include = event.includeDev();
		// Recipes are now handwritten in common/src/main/resources/data/agricraft/recipes/ using vanilla 1.21.1 format
		// generator.addProvider(event.includeServer(), (DataProvider.Factory<RecipeProvider>) output -> new ModRecipeProvider(output, event.getLookupProvider()));
		generator.addProvider(include, (DataProvider.Factory<BlockTagsProvider>) output -> new ModBlockTagProvider(output, event.getLookupProvider(), AgriApi.MOD_ID));
		generator.addProvider(include, (DataProvider.Factory<ItemTagsProvider>) output -> new ModItemTagProvider(output, event.getLookupProvider(), AgriApi.MOD_ID));
		generator.addProvider(
				include,
				(DataProvider.Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(
						output,
						event.getLookupProvider(),
						// The objects to generate
						new RegistrySetBuilder()
								.add(AgriApi.AGRIPLANTS, PlantsDatagen::registerPlants)
								.add(AgriApi.AGRISOILS, SoilsDatagen::registerSoils)
								.add(AgriApi.AGRIMUTATIONS, MutationsDatagen::registerMutations)
								.add(AgriApi.AGRIFERTILIZERS, FertilizersDatagen::registerFertilizers)
								.add(AgriApi.AGRIWEEDS, WeedsDatagen::registerWeeds),
						// Generate dynamic registry objects for this mod
						Set.of("minecraft", AgriApi.MOD_ID)
				)
		);
		addProvider("minecraft", "crop", ModelsDatagen::registerMinecraftPlant, BlockModelBuilder::new, generator, include);
		addProvider("minecraft", "seed", ModelsDatagen::registerMinecraftSeed, ItemModelBuilder::new, generator, include);
		addProvider("agricraft", "crop", ModelsDatagen::registerAgricraftPlant, BlockModelBuilder::new, generator, include);
		addProvider("agricraft", "seed", ModelsDatagen::registerAgricraftSeed, ItemModelBuilder::new, generator, include);
		addProvider("agricraft", "weed", ModelsDatagen::registerAgricraftWeed, BlockModelBuilder::new, generator, include);

		if (biomesoplenty) {
			addExtraDataPackProvider("biomesoplenty", new RegistrySetBuilder().add(AgriApi.AGRIPLANTS, PlantsDatagen::registerBiomesOPlenty).add(AgriApi.AGRIMUTATIONS, MutationsDatagen::registerBiomesOPlenty), ModelsDatagen::registerBiomesOPlentyPlant, ModelsDatagen::registerBiomesOPlentySeed, LangDatagen::biomesoplenty, event, include);
		}
		if (immersiveengineering) {
			addExtraDataPackProvider("immersiveengineering", new RegistrySetBuilder().add(AgriApi.AGRIPLANTS, PlantsDatagen::registerImmersiveEngineering).add(AgriApi.AGRIMUTATIONS, MutationsDatagen::registerImmersiveEngineering), ModelsDatagen::registerImmersiveEngineeringPlant, ModelsDatagen::registerImmersiveEngineeringSeed, LangDatagen::immersiveengineering, event, include);
		}
		if (pamhc2crops) {
			addExtraDataPackProvider("pamhc2crops", new RegistrySetBuilder().add(AgriApi.AGRIPLANTS, PlantsDatagen::registerPamsHarvestCraft2).add(AgriApi.AGRIMUTATIONS, MutationsDatagen::registerPamsHarvestCraft2), ModelsDatagen::registerPamsHarvestCraft2Plant, ModelsDatagen::registerPamsHarvestCraft2Seed, LangDatagen::pamhc2crops, event, include);
		}
		if (mysticalagriculture) {
			addExtraDataPackProvider("mysticalagriculture", new RegistrySetBuilder().add(AgriApi.AGRIPLANTS, PlantsDatagen::registerMysticalAgriculture).add(AgriApi.AGRISOILS, SoilsDatagen::registerMysticalAgriculture).add(AgriApi.AGRIFERTILIZERS, FertilizersDatagen::registerMysticalAgriculture), ModelsDatagen::registerMysticalAgriculturePlant, ModelsDatagen::registerMysticalAgricultureSeed, LangDatagen::mysticalagriculture, event, include);
		}
		if (farmersdelight) {
			addExtraDataPackProvider("farmersdelight", new RegistrySetBuilder().add(AgriApi.AGRIPLANTS, PlantsDatagen::registerFarmersDelight).add(AgriApi.AGRIMUTATIONS, MutationsDatagen::registerFarmersDelight).add(AgriApi.AGRISOILS, SoilsDatagen::registerFarmersDelight), ModelsDatagen::registerFarmersDelightPlant, ModelsDatagen::registerFarmersDelightSeed, LangDatagen::farmersdelight, event, include);
		}
	}

	private static <T extends ModelBuilder<T>> void addProvider(String modid, String folder, Consumer<ModelProvider<T>> consumer, Function<Identifier, T> builderFactory, DataGenerator generator, boolean include) {
		generator.addProvider(include, new ModelProvider<T>(generator.getPackOutput(), modid, folder, builderFactory) {
			@Override
			protected void registerModels() {
				consumer.accept(this);
			}

			@Override
			public String getName() {
				return "Models for: %s:%s".formatted(this.modid, this.folder);
			}
		});
	}

	private static void addExtraDataPackProvider(String modid, RegistrySetBuilder registrySetBuilder, Consumer<ModelProvider<BlockModelBuilder>> blockModels,
	                                             Consumer<ModelProvider<ItemModelBuilder>> seedModels, Consumer<LanguageProvider> translations, GatherDataEvent event, boolean include) {
		DataGenerator generator = event.getGenerator();
		PackOutput dataOutput = generator.getPackOutput("datapacks/" + modid);
		generator.addProvider(include, (DataProvider.Factory<PackMetadataGenerator>) output -> new PackMetadataGenerator(dataOutput) {
			@Override
			public String getName() {
				return "DataPack Metadata " + modid;
			}
		}.add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(Component.translatable("agricraft.datapacks." + modid), DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA).minorRange())));
		generator.addProvider(include,
				(DataProvider.Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(dataOutput, event.getLookupProvider(), registrySetBuilder, Set.of(modid)) {
					@Override
					public String getName() {
						return "Registries " + modid;
					}
				}
		);
		PackOutput resourceOutput = generator.getPackOutput("resourcepacks/" + modid);
		generator.addProvider(include, (DataProvider.Factory<PackMetadataGenerator>) output -> new PackMetadataGenerator(resourceOutput) {
			@Override
			public String getName() {
				return "ResourcePack Metadata " + modid;
			}
		}.add(PackMetadataSection.CLIENT_TYPE, new PackMetadataSection(Component.translatable("agricraft.resourcepacks." + modid), DetectedVersion.BUILT_IN.packVersion(PackType.CLIENT_RESOURCES).minorRange())));
		generator.addProvider(include, new ModelProvider<BlockModelBuilder>(resourceOutput, modid, "crop", BlockModelBuilder::new) {
			@Override
			protected void registerModels() {
				blockModels.accept(this);
			}

			@Override
			public String getName() {
				return "Crop Models for " + modid;
			}
		});
		generator.addProvider(include, new ModelProvider<ItemModelBuilder>(resourceOutput, modid, "seed", ItemModelBuilder::new) {
			@Override
			protected void registerModels() {
				seedModels.accept(this);
			}

			@Override
			public String getName() {
				return "Seed Models for " + modid;
			}
		});
		generator.addProvider(include, new LanguageProvider(resourceOutput, modid, "en_us") {
			@Override
			public String getName() {
				return "Languages: en_us for " + modid;
			}

			@Override
			protected void addTranslations() {
				translations.accept(this);
			}
		});
	}

}
