package com.agricraft.agricraft.common.registry;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.common.block.entity.CropBlockEntity;
import com.agricraft.agricraft.common.block.entity.SeedAnalyzerBlockEntity;
import com.agricraft.agricraft.common.util.Platform;
import com.agricraft.agricraft.common.util.PlatformRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class ModBlockEntityTypes {
	public static final PlatformRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = Platform.get().createRegistry(BuiltInRegistries.BLOCK_ENTITY_TYPE, AgriApi.MOD_ID);

	public static final PlatformRegistry.Entry<BlockEntityType<CropBlockEntity>> CROP = BLOCK_ENTITY_TYPES.register("crop", () -> new BlockEntityType<>((blockPos, blockState) -> Platform.get().createCropBlockEntity(blockPos, blockState), Set.of(ModBlocks.CROP.get())));
	public static final PlatformRegistry.Entry<BlockEntityType<SeedAnalyzerBlockEntity>> SEED_ANALYZER = BLOCK_ENTITY_TYPES.register("seed_analyzer", () -> new BlockEntityType<>(SeedAnalyzerBlockEntity::new, Set.of(ModBlocks.SEED_ANALYZER.get())));

}
