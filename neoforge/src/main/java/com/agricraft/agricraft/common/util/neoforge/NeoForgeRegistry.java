package com.agricraft.agricraft.common.util.neoforge;

import com.agricraft.agricraft.common.util.PlatformRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeRegistry<T> implements PlatformRegistry<T> {

	private static IEventBus modBus;

	public static void setModBus(IEventBus bus) {
		modBus = bus;
	}

	private final DeferredRegister<T> registry;

	public NeoForgeRegistry(Registry<T> registry, String modid) {
		this.registry = DeferredRegister.create(registry.key(), modid);
	}

	@Override
	public <I extends T> Entry<I> register(String id, Supplier<I> supplier) {
		DeferredHolder<T, I> object = this.registry.register(id, supplier);
		return new NeoForgeRegistryEntry<>(object);
	}

	@Override
	public void init() {
		this.registry.register(modBus);
	}

	public static class NeoForgeRegistryEntry<T> implements Entry<T> {

		private final DeferredHolder<? super T, T> object;

		public NeoForgeRegistryEntry(DeferredHolder<? super T, T> object) {
			this.object = object;
		}

		@Override
		public Identifier id() {
			return this.object.getId();
		}

		@Override
		public T get() {
			return this.object.get();
		}

	}

}
