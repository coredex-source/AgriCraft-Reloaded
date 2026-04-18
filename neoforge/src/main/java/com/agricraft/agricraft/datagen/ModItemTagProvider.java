package com.agricraft.agricraft.datagen;

import com.agricraft.agricraft.common.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

	public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
		super(output, lookupProvider, modId);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		// Fabric
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("c:seeds")))
				.add(ModItems.SEED.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("c:coal_nuggets")))
				.add(ModItems.COAL_PEBBLE.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("c:copper_nuggets")))
				.add(ModItems.COPPER_NUGGET.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("c:diamond_nuggets")))
				.add(ModItems.DIAMOND_SHARD.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("c:emerald_nuggets")))
				.add(ModItems.EMERALD_SHARD.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("c:quartz_nuggets")))
				.add(ModItems.QUARTZ_SHARD.get());

		// Forge
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("forge:seeds")))
				.add(ModItems.SEED.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/coal")))
				.add(ModItems.COAL_PEBBLE.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/copper")))
				.add(ModItems.COPPER_NUGGET.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/diamond")))
				.add(ModItems.DIAMOND_SHARD.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/emerald")))
				.add(ModItems.EMERALD_SHARD.get());
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/quartz")))
				.add(ModItems.QUARTZ_SHARD.get());

		// Cross-Compatibility
		//     Recipes
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:rods/wooden")))
				.add(Items.STICK)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:wooden_rods")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:rods/wooden")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/iron")))
				.add(Items.IRON_NUGGET)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:iron_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/iron")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:obsidian")))
				.add(Items.OBSIDIAN)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:obsidian")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:obsidian")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:seeds")))
				.add(Items.BEETROOT_SEEDS)
				.add(Items.MELON_SEEDS)
				.add(Items.PUMPKIN_SEEDS)
				.add(Items.WHEAT_SEEDS)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:seeds")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:seeds")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:ingots/iron")))
				.add(Items.IRON_INGOT)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:iron_ingots")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:ingots/iron")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:glass_panes/colorless")))
				.add(Items.GLASS_PANE)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:clear_glass_panes")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:glass_panes/colorless")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:fences/wooden")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("minecraft:wooden_fences"))) // Not sure why this tag isn't available during datagen?
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:wooden_fences")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:fences/wooden")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:string")))
				.add(Items.STRING)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:string")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:strings")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:string")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:leather")))
				.add(Items.LEATHER)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:leather")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:leathers")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:leather")));

		//     Plants (produce)
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:dusts/glowstone")))
				.add(Items.GLOWSTONE_DUST)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:glowstone_dusts")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:dusts/glowstone")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:dusts/redstone")))
				.add(Items.REDSTONE)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:redstone_dusts")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:dusts/redstone")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:gems/lapis")))
				.add(Items.LAPIS_LAZULI)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:lapis")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:lapis_gems")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:gems/lapis")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/aluminium")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:aluminum_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:aluminium_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/aluminum")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/aluminium")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/coal")))
				.add(ModItems.COAL_PEBBLE.get())
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:coal_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/coal")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/copper")))
				.add(ModItems.COPPER_NUGGET.get())
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:copper_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/copper")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/diamond")))
				.add(ModItems.DIAMOND_SHARD.get())
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:diamond_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/diamond")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/emerald")))
				.add(ModItems.EMERALD_SHARD.get())
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:emerald_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/emerald")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/gold")))
				.add(Items.GOLD_NUGGET)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:gold_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/gold")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/iron")))
				.add(Items.IRON_NUGGET)
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:iron_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/iron")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/lead")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:lead_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/lead")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/nickel")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:nickel_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/nickel")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/osmium")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:osmium_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/osmium")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/platinum")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:platinum_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/platinum")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/quartz")))
				.add(ModItems.QUARTZ_SHARD.get())
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:quartz_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/quartz")));
		this.tag(TagKey.create(Registries.ITEM, Identifier.parse("agricraft:nuggets/tin")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("c:tin_nuggets")))
				.addOptionalTag(TagKey.create(Registries.ITEM, Identifier.parse("forge:nuggets/tin")));
	}
}

