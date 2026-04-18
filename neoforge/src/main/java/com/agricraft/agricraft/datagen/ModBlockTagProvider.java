package com.agricraft.agricraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
	public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
		super(output, lookupProvider, modId);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		// Cross-Compatibility
		//     Plants (blocks)
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/aluminium")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:aluminum_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:aluminium_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/aluminum")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/aluminium")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/coal")))
				.add(Blocks.COAL_ORE)
				.add(Blocks.DEEPSLATE_COAL_ORE)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("minecraft:coal_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:coal_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/coal")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/copper")))
				.add(Blocks.COPPER_ORE)
				.add(Blocks.DEEPSLATE_COPPER_ORE)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("minecraft:copper_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:copper_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/copper")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/diamond")))
				.add(Blocks.DIAMOND_ORE)
				.add(Blocks.DEEPSLATE_DIAMOND_ORE)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("minecraft:diamond_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:diamond_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/diamond")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/emerald")))
				.add(Blocks.EMERALD_ORE)
				.add(Blocks.DEEPSLATE_EMERALD_ORE)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("minecraft:emerald_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:emerald_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/emerald")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/gold")))
				.add(Blocks.GOLD_ORE)
				.add(Blocks.DEEPSLATE_GOLD_ORE)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("minecraft:gold_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:gold_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/gold")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/iron")))
				.add(Blocks.IRON_ORE)
				.add(Blocks.DEEPSLATE_IRON_ORE)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("minecraft:iron_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:iron_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/iron")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/lapis")))
				.add(Blocks.LAPIS_ORE)
				.add(Blocks.DEEPSLATE_LAPIS_ORE)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("minecraft:lapis_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:lapis_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/lapis")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/lead")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:lead_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/lead")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/netherite_scrap")))
				.add(Blocks.ANCIENT_DEBRIS)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:netherite_scrap_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/netherite_scrap")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/nickel")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:nickel_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/nickel")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/osmium")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:osmium_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/osmium")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/platinum")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:platinum_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/platinum")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/quartz")))
				.add(Blocks.NETHER_QUARTZ_ORE)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:quartz_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/quartz")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/redstone")))
				.add(Blocks.REDSTONE_ORE)
				.add(Blocks.DEEPSLATE_REDSTONE_ORE)
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:redstone_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/redstone")));
		this.tag(TagKey.create(Registries.BLOCK, Identifier.parse("agricraft:ores/tin")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("c:tin_ores")))
				.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.parse("forge:ores/tin")));
	}
}

