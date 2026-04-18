package com.agricraft.agricraft.common.util.neoforge;

import com.agricraft.agricraft.AgriCraft;
import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.plant.AgriPlant;
import com.agricraft.agricraft.common.block.entity.CropBlockEntity;
import com.agricraft.agricraft.common.neoforge.block.entity.NeoForgeCropBlockEntity;
import com.agricraft.agricraft.common.item.AgriSeedItem;
import com.agricraft.agricraft.common.item.neoforge.NeoForgeAgriSeedItem;
import com.agricraft.agricraft.common.registry.ModCreativeTabs;
import com.agricraft.agricraft.common.registry.ModItems;
import com.agricraft.agricraft.common.util.ExtraDataMenuProvider;
import com.agricraft.agricraft.common.util.Platform;
import com.agricraft.agricraft.common.util.PlatformRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * NeoForge implementation of {@link Platform}
 */
public class NeoForgePlatform extends Platform {

	@Override
	public <T> PlatformRegistry<T> createRegistry(Registry<T> registry, String modid) {
		return new NeoForgeRegistry<>(registry, modid);
	}

	@Override
	public AgriSeedItem createAgriSeedItem(Item.Properties properties) {
		return new NeoForgeAgriSeedItem(properties);
	}

	@Override
	public CropBlockEntity createCropBlockEntity(BlockPos pos, BlockState state) {
		return new NeoForgeCropBlockEntity(pos, state);
	}

	@Override
	public CreativeModeTab createMainCreativeTab() {
		return CreativeModeTab.builder()
				.icon(() -> new ItemStack(ModItems.DEBUGGER.get()))
				.title(Component.translatable("itemGroup.agricraft.main"))
				.displayItems(ModItems::addItemsToTabs)
				.build();
	}

	@Override
	public CreativeModeTab createSeedsCreativeTab() {
		return CreativeModeTab.builder()
				.title(Component.translatable("itemGroup.agricraft.seeds"))
				.icon(() -> new ItemStack(Items.WHEAT_SEEDS))
				.displayItems((itemDisplayParameters, output) -> AgriApi.getPlantRegistry()
						.ifPresent(registry -> {
							AgriCraft.LOGGER.info("add seeds in tab: " + registry.stream().count());
							for (Map.Entry<ResourceKey<AgriPlant>, AgriPlant> entry : registry.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
								output.accept(AgriSeedItem.toStack(entry.getValue()));
							}
						}))
				.withTabsBefore(ModCreativeTabs.MAIN_TAB.id())
				.build();
	}

	@Override
	public Optional<RegistryAccess> getRegistryAccess() {
		if (FMLLoader.getCurrent().getDist().isClient()) {
			if (Minecraft.getInstance().level != null) {
				return Optional.of(Minecraft.getInstance().level.registryAccess());
			}
		} else {
			if (ServerLifecycleHooks.getCurrentServer() != null) {
				return Optional.of(ServerLifecycleHooks.getCurrentServer().registryAccess());
			}
		}
		return Optional.empty();
	}

	@Override
	public List<Item> getItemsFromLocation(ExtraCodecs.TagOrElementLocation tag) {
		if (!tag.tag()) {
			return List.of(BuiltInRegistries.ITEM.getValue(tag.id()));
		} else {
			return StreamSupport.stream(BuiltInRegistries.ITEM.getTagOrEmpty(TagKey.create(Registries.ITEM, tag.id())).spliterator(), false)
					.map(Holder::value)
					.toList();
		}
	}

	@Override
	public List<Block> getBlocksFromLocation(ExtraCodecs.TagOrElementLocation tag) {
		if (!tag.tag()) {
			return List.of(BuiltInRegistries.BLOCK.getValue(tag.id()));
		} else {
			return StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(TagKey.create(Registries.BLOCK, tag.id())).spliterator(), false)
					.map(Holder::value)
					.toList();
		}
	}

	@Override
	public List<Fluid> getFluidsFromLocation(ExtraCodecs.TagOrElementLocation tag) {
		if (!tag.tag()) {
			return List.of(BuiltInRegistries.FLUID.getValue(tag.id()));
		} else {
			return StreamSupport.stream(BuiltInRegistries.FLUID.getTagOrEmpty(TagKey.create(Registries.FLUID, tag.id())).spliterator(), false)
					.map(Holder::value)
					.toList();
		}
	}

	@Override
	public <T extends AbstractContainerMenu> MenuType<T> createMenuType(Platform.MenuFactory<T> factory) {
		return IMenuTypeExtension.create(factory::create);
	}

	@Override
	public void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		player.openMenu(provider, buf -> provider.writeExtraData(player, buf));
	}

	@Override
	public ParticleType<?> getParticleType(Identifier particleId) {
		return BuiltInRegistries.PARTICLE_TYPE.getValue(particleId);
	}

}
