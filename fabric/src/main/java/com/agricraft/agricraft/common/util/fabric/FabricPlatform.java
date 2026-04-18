package com.agricraft.agricraft.common.util.fabric;

import com.agricraft.agricraft.AgriCraft;
import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.plant.AgriPlant;
import com.agricraft.agricraft.common.block.entity.CropBlockEntity;
import com.agricraft.agricraft.common.item.AgriSeedItem;
import com.agricraft.agricraft.common.registry.ModItems;
import com.agricraft.agricraft.common.util.ExtraDataMenuProvider;
import com.agricraft.agricraft.common.util.Platform;
import com.agricraft.agricraft.common.util.PlatformRegistry;
import com.agricraft.agricraft.fabric.AgriCraftFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
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
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Fabric implementation of {@link Platform}
 */
public class FabricPlatform extends Platform {

	@Override
	public <T> PlatformRegistry<T> createRegistry(Registry<T> registry, String modid) {
		return new FabricRegistry<>(registry, modid);
	}

	@Override
	public AgriSeedItem createAgriSeedItem(Item.Properties properties) {
		return new AgriSeedItem(properties);
	}

	@Override
	public CropBlockEntity createCropBlockEntity(BlockPos pos, BlockState state) {
		return new CropBlockEntity(pos, state);
	}

	@Override
	public CreativeModeTab createMainCreativeTab() {
		return FabricItemGroup.builder()
				.icon(() -> new ItemStack(ModItems.DEBUGGER.get()))
				.title(Component.translatable("itemGroup.agricraft.main"))
				.displayItems(ModItems::addItemsToTabs)
				.build();
	}

	@Override
	public CreativeModeTab createSeedsCreativeTab() {
		return FabricItemGroup.builder()
				.title(Component.translatable("itemGroup.agricraft.seeds"))
				.icon(() -> new ItemStack(Items.WHEAT_SEEDS))
				.displayItems((itemDisplayParameters, output) -> AgriApi.getPlantRegistry()
						.ifPresent(registry -> {
							AgriCraft.LOGGER.info("add seeds in tab: " + registry.stream().count());
							for (Map.Entry<ResourceKey<AgriPlant>, AgriPlant> entry : registry.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().identifier())).toList()) {
								output.accept(AgriSeedItem.toStack(entry.getValue()));
							}
						}))
				.build();
	}

	@Override
	public Optional<RegistryAccess> getRegistryAccess() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			if (Minecraft.getInstance().level != null) {
				return Optional.of(Minecraft.getInstance().level.registryAccess());
			}
		} else {
			if (AgriCraftFabric.cachedServer != null) {
				return Optional.of(AgriCraftFabric.cachedServer.registryAccess());
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
		return new ExtendedScreenHandlerType<>((syncId, inventory, pos) -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
			buf.writeBlockPos(pos);
			return factory.create(syncId, inventory, buf);
		}, BlockPos.STREAM_CODEC);
	}

	@Override
	public void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		player.openMenu(new ExtendedScreenHandlerFactory<BlockPos>() {
			@Override
			public BlockPos getScreenOpeningData(ServerPlayer player) {
				FriendlyByteBuf buf = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
				provider.writeExtraData(player, buf);
				return buf.readBlockPos();
			}

			@Override
			public Component getDisplayName() {
				return provider.getDisplayName();
			}

			@Nullable
			@Override
			public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
				return provider.createMenu(i, inventory, player);
			}
		});
	}

	@Override
	public ParticleType<?> getParticleType(Identifier particleId) {
		return BuiltInRegistries.PARTICLE_TYPE.getValue(particleId);
	}

}
