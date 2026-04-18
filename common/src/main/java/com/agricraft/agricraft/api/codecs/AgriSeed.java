package com.agricraft.agricraft.api.codecs;

import com.agricraft.agricraft.common.util.Platform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Item;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record AgriSeed(ExtraCodecs.TagOrElementLocation item, boolean overridePlanting, CompoundTag nbt,
                       double grassDropChance, double seedDropChance, double seedDropBonus) {
	// TODO: @Ketheroth move drop chances in AgriPlant to make it usable for the default agricraft seed

	public static final Codec<AgriSeed> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.TAG_OR_ELEMENT_ID.fieldOf("item").forGetter(seed -> seed.item),
			Codec.BOOL.fieldOf("override_planting").forGetter(seed -> seed.overridePlanting),
			CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(seed -> seed.nbt.isEmpty() ? Optional.empty() : Optional.of(seed.nbt)),
			Codec.DOUBLE.fieldOf("grass_drop_chance").forGetter(plant -> plant.grassDropChance),
			Codec.DOUBLE.fieldOf("seed_drop_bonus").forGetter(plant -> plant.seedDropBonus),
			Codec.DOUBLE.fieldOf("seed_drop_chance").forGetter(plant -> plant.seedDropChance)
	).apply(instance, AgriSeed::new));

	public AgriSeed(ExtraCodecs.TagOrElementLocation item, boolean overridePlanting, Optional<CompoundTag> nbt,
	                double grassDropChance, double seedDropChance, double seedDropBonus) {
		// codec use
		this(item, overridePlanting, nbt.orElse(new CompoundTag()), grassDropChance, seedDropChance, seedDropBonus);
	}

	public static Builder builder() {
		return new Builder();
	}

	public boolean isVariant(ItemStack itemStack) {
		List<Item> items = Platform.get().getItemsFromLocation(this.item());
		if (items.contains(itemStack.getItem())) {
			if (this.nbt.isEmpty()) {
				return true;
			}
			CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
			for (String key : this.nbt.keySet()) {
				if (!tag.contains(key) || !tag.get(key).equals(this.nbt.get(key))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static class Builder {

		ExtraCodecs.TagOrElementLocation item;
		boolean overridePlanting = true;
		CompoundTag nbt = new CompoundTag();
		double grassDropChance = 0;
		double seedDropChance = 1.0;
		double seedDropBonus = 0.0;

		public AgriSeed build() {
			return new AgriSeed(item, overridePlanting, nbt, grassDropChance, seedDropChance, seedDropBonus);
		}

		public Builder item(String location) {
			this.item = new ExtraCodecs.TagOrElementLocation(Identifier.parse(location), false);
			return this;
		}

		public Builder item(String namespace, String path) {
			this.item = new ExtraCodecs.TagOrElementLocation(Identifier.fromNamespaceAndPath(namespace, path), false);
			return this;
		}

		public Builder tag(String location) {
			this.item = new ExtraCodecs.TagOrElementLocation(Identifier.parse(location), true);
			return this;
		}

		public Builder tag(String namespace, String path) {
			this.item = new ExtraCodecs.TagOrElementLocation(Identifier.fromNamespaceAndPath(namespace, path), true);
			return this;
		}

		public Builder nbt(CompoundTag nbt) {
			this.nbt = nbt;
			return this;
		}

		public Builder chances(double grass, double seed, double seedBonus) {
			this.grassDropChance = grass;
			this.seedDropChance = seed;
			this.seedDropBonus = seedBonus;
			return this;
		}

		public Builder overridePlanting(boolean overridePlanting) {
			this.overridePlanting = overridePlanting;
			return this;
		}

	}

}
