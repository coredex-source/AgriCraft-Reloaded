package com.agricraft.agricraft.common.item;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.tools.journal.JournalData;
import com.agricraft.agricraft.api.tools.journal.JournalPage;
import com.agricraft.agricraft.client.ClientUtil;
import com.agricraft.agricraft.common.block.entity.SeedAnalyzerBlockEntity;
import com.agricraft.agricraft.common.item.journal.EmptyPage;
import com.agricraft.agricraft.common.item.journal.FrontPage;
import com.agricraft.agricraft.common.item.journal.GeneticsPage;
import com.agricraft.agricraft.common.item.journal.GrowthReqsPage;
import com.agricraft.agricraft.common.item.journal.IntroductionPage;
import com.agricraft.agricraft.common.item.journal.MutationsPage;
import com.agricraft.agricraft.common.item.journal.PlantPage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class JournalItem extends Item {

	public JournalItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);
		if (player.isDiscrete()) {
			return InteractionResult.PASS;
		}
		if (level.isClientSide()) {
			ClientUtil.openJournalScreen(player, usedHand);
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (level.isClientSide()) {
			return InteractionResult.PASS;
		}
		ItemStack heldItem = context.getItemInHand();
		// if a seed analyzer was clicked, insert the journal inside
		if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof SeedAnalyzerBlockEntity seedAnalyzer) {
			if (seedAnalyzer.hasJournal()) {
				return InteractionResult.PASS;
			}
			ItemStack remaining = seedAnalyzer.insertJournal(heldItem);
			heldItem.setCount(remaining.getCount());
			return InteractionResult.CONSUME;
		}
		return super.useOn(context);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
//		researchPlant(stack, Identifier.parse("minecraft:wheat"));
		return stack;
	}

	public static void researchPlant(ItemStack journal, Identifier plantId) {
		CompoundTag tag = journal.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		StringTag idTag = StringTag.valueOf(plantId.toString());
		if (tag.contains("plants")) {
			ListTag plants = tag.getListOrEmpty("plants");
			if (!plants.contains(idTag)) {
				plants.add(idTag);
			}
		} else {
			ListTag plants = new ListTag();
			plants.add(idTag);
			tag.put("plants", plants);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag isAdvanced) {
		tooltipAdder.accept(Component.translatable("agricraft.tooltip.journal", getResearchedPlants(stack)).withStyle(ChatFormatting.GRAY));
	}

	public static JournalData getJournalData(ItemStack journal) {
		return new Data(journal);
	}

	public static int getResearchedPlants(ItemStack journal) {
		CompoundTag tag = journal.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if (tag == null || !tag.contains("plants")) {
			return 0;
		}
		return tag.getListOrEmpty("plants").size();
	}

	public static class Data implements JournalData {

		private final List<Identifier> plants;
		private final List<JournalPage> pages;

		public Data(ItemStack journalStack) {
			this.plants = new ArrayList<>();
			this.pages = new ArrayList<>();
			CompoundTag tag = journalStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
			if (tag != null && tag.contains("plants")) {
				ListTag list = tag.getListOrEmpty("plants");
				for (Tag plantTag : list) {
					Identifier plantId = Identifier.parse(plantTag.asString().orElse(""));
					if (AgriApi.getPlant(plantId).isPresent()) {
						plants.add(plantId);
					}
				}
			}
			this.plants.sort(Comparator.comparing(Identifier::toString));
			this.initializePages();
		}

		public void initializePages() {
			this.pages.clear();
			this.pages.add(new FrontPage());
			this.pages.add(new IntroductionPage());
			this.pages.add(new GeneticsPage());
			this.pages.add(new GrowthReqsPage());
			for (Identifier plant : this.plants) {
				PlantPage plantPage = new PlantPage(plant, plants);
				this.pages.add(plantPage);
				List<List<Identifier>> mutations = plantPage.getMutationsOffPage();
				int size = mutations.size();
				if (size > 0) {
					int remaining = size;
					int from = 0;
					int to = Math.min(remaining, MutationsPage.LIMIT);
					while (remaining > 0) {
						pages.add(new MutationsPage(mutations.subList(from, to)));
						remaining -= (to - from);
						from = to;
						to = from + Math.min(remaining, MutationsPage.LIMIT);
					}
				}
			}
		}

		@Override
		public JournalPage getPage(int index) {
			if (0 <= index && index < this.pages.size()) {
				return this.pages.get(index);
			}
			return new EmptyPage();
		}

		@Override
		public int size() {
			return this.pages.size();
		}

		@Override
		public List<Identifier> getDiscoveredSeeds() {
			return this.plants;
		}

	}

}
