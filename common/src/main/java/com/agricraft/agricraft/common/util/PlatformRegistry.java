package com.agricraft.agricraft.common.util;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public interface PlatformRegistry<T> {

	<I extends T> Entry<I> register(String id, Supplier<I> supplier);

	void init();

	interface Entry<T> extends Supplier<T> {

		Identifier id();

	}

}