package com.agricraft.agricraft.common.item;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.config.CoreConfig;
import com.agricraft.agricraft.api.genetic.AgriGenomeProviderItem;
import com.agricraft.agricraft.api.plant.AgriPlant;
import com.agricraft.agricraft.api.crop.AgriCrop;
import com.agricraft.agricraft.api.genetic.AgriGenome;
import com.agricraft.agricraft.common.block.CropBlock;
import com.agricraft.agricraft.common.block.CropState;
import com.agricraft.agricraft.common.block.entity.SeedAnalyzerBlockEntity;
import com.agricraft.agricraft.common.registry.ModBlocks;
import com.agricraft.agricraft.common.registry.ModItems;
import com.agricraft.agricraft.common.util.LangUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AgriSeedItem extends BlockItem implements AgriGenomeProviderItem {

	public AgriSeedItem(Properties properties) {
		super(ModBlocks.CROP.get(), properties);
	}

	/**
	 * Create an ItemStack with the default genome for a plant
	 *
	 * @param plant the plant to create to genome from
	 * @return an ItemStack with the default genome of the plant
	 */
	public static ItemStack toStack(AgriPlant plant) {
		ItemStack stack = new ItemStack(ModItems.SEED.get(), 1);
		AgriGenome genome = new AgriGenome(plant);
		CompoundTag tag = new CompoundTag();
		genome.writeToNBT(tag);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
		return stack;
	}

	/**
	 * Create an ItemStack with the given genome
	 *
	 * @param genome the genome to create the ItemStack from
	 * @return an ItemStack with the given genome
	 */
	public static ItemStack toStack(AgriGenome genome) {
		ItemStack stack = new ItemStack(ModItems.SEED.get(), 1);
		CompoundTag tag = new CompoundTag();
		genome.writeToNBT(tag);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
		return stack;
	}

	/**
	 * Compute the plant species from an ItemStack
	 *
	 * @param stack the itemstack to compute the species from
	 * @return the plant species formatted as a resource location, or {@code agricraft:unknown} if not found
	 */
	public static String getSpecies(ItemStack stack) {
		if (stack.getItem() != ModItems.SEED.get()) {
			return "agricraft:unknown";
		}
		if (!stack.has(DataComponents.CUSTOM_DATA)) {
			return "agricraft:unknown";
		}
		CompoundTag tag = stack.get(DataComponents.CUSTOM_DATA).copyTag();
		AgriGenome genome = AgriGenome.fromNBT(tag);
		if (genome == null) {
			return "agricraft:unknown";
		}
		return genome.getSpeciesGene().getDominant().trait();
	}

	@Override
	public Component getName(ItemStack stack) {
		if (!stack.has(DataComponents.CUSTOM_DATA)) {
			return Component.translatable("seed.agricraft.agricraft.unknown");
		}
		AgriGenome genome = AgriGenome.fromNBT(stack.get(DataComponents.CUSTOM_DATA).copyTag());
		if (genome == null) {
			return Component.translatable("seed.agricraft.agricraft.unknown");
		}
		return LangUtils.seedName(genome.getSpeciesGene().getDominant().trait());
	}

	@Override
	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult result = super.place(context);
		Level level = context.getLevel();
		if (result.consumesAction() && !level.isClientSide) {
			AgriApi.getCrop(level, context.getClickedPos()).ifPresent(crop -> {
				if (context.getItemInHand().has(DataComponents.CUSTOM_DATA)) {
					CompoundTag tag = context.getItemInHand().get(DataComponents.CUSTOM_DATA).copyTag();
					crop.plantGenome(AgriGenome.fromNBT(tag));
				}
			});
		}
		return result;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (level.isClientSide) {
			return InteractionResult.PASS;
		}
		ItemStack heldItem = context.getItemInHand();
		// if crop sticks were clicked, attempt to plant the seed
		Optional<AgriCrop> optionalAgriCrop = AgriApi.getCrop(level, context.getClickedPos());
		if (optionalAgriCrop.isPresent()) {
			AgriCrop crop = optionalAgriCrop.get();
			if (crop.hasPlant() || crop.isCrossCropSticks()) {
				return InteractionResult.PASS;
			}
			plantSeed(context.getPlayer(), crop, heldItem);
			return InteractionResult.CONSUME;
		}
		// if a seed analyzer was clicked, insert the seed inside
		if (level.getBlockEntity(context.getClickedPos()) instanceof SeedAnalyzerBlockEntity seedAnalyzer) {
			if (seedAnalyzer.hasSeed()) {
				return InteractionResult.PASS;
			}
			ItemStack remaining = seedAnalyzer.insertSeed(heldItem);
			heldItem.setCount(remaining.getCount());
			return InteractionResult.CONSUME;
		}
		// if a soil was clicked, check the block above and handle accordingly
		return AgriApi.getSoil(level, context.getClickedPos()).map(soil -> AgriApi.getCrop(level, context.getClickedPos().above()).<InteractionResult>map(crop -> {
			// there is a crop with a plant or is a cross crop stick, do nothing
			if (crop.hasPlant() || crop.isCrossCropSticks()) {
				return InteractionResult.PASS;
			}
			// there is a crop without a plant, plant the seed in the crop
			plantSeed(context.getPlayer(), crop, heldItem);
			return InteractionResult.CONSUME;
		}).orElseGet(() -> {
			// no crop above soil, plant directly if config allows
			if (!CoreConfig.plantOffCropSticks) {
				return InteractionResult.PASS;
			}
			BlockPos cropPos = context.getClickedPos().above();
			if (!level.getBlockState(cropPos).canBeReplaced()) {
				return InteractionResult.PASS;
			}
			level.setBlock(cropPos, ModBlocks.CROP.get().defaultBlockState().setValue(CropBlock.CROP_STATE, CropState.PLANT), 3);
			AgriApi.getCrop(level, cropPos).ifPresent(crop -> plantSeed(context.getPlayer(), crop, heldItem));
			SoundType sound = SoundType.CROP;
			level.playSound(null, cropPos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
			return InteractionResult.CONSUME;
		})).orElse(super.useOn(context));
	}

	private void plantSeed(Player player, AgriCrop crop, ItemStack seed) {
		if (seed.has(DataComponents.CUSTOM_DATA)) {
			crop.plantGenome(AgriGenome.fromNBT(seed.get(DataComponents.CUSTOM_DATA).copyTag()), player);
		}
		if (player != null && !player.isCreative()) {
			seed.shrink(1);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		if (stack.has(DataComponents.CUSTOM_DATA)) {
			CompoundTag tag = stack.get(DataComponents.CUSTOM_DATA).copyTag();
			AgriGenome genome = AgriGenome.fromNBT(tag);
			if (genome != null) {
				genome.appendHoverText(tooltipComponents, isAdvanced);
			}
		}
	}

//	@Override
//	public ItemStack getDefaultInstance() {
//		return AgriApi.getPlant(ResourceLocation.parse("minecraft:wheat")).map(AgriSeedItem::toStack).orElse(super.getDefaultInstance());
//	}

}
