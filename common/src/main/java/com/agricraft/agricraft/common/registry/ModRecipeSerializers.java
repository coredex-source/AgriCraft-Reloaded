package com.agricraft.agricraft.common.registry;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.common.item.crafting.MagnifyingHelmetRecipe;
import com.agricraft.agricraft.common.util.Platform;
import com.agricraft.agricraft.common.util.PlatformRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializers {

	public static final PlatformRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = Platform.get().createRegistry(BuiltInRegistries.RECIPE_SERIALIZER, AgriApi.MOD_ID);

	public static final PlatformRegistry.Entry<RecipeSerializer<MagnifyingHelmetRecipe>> MAGNIFYING_HELMET = RECIPE_SERIALIZERS.register("magnifyinghelmet", () -> new RecipeSerializer<>(MapCodec.unit(new MagnifyingHelmetRecipe()), StreamCodec.unit(new MagnifyingHelmetRecipe())));

}
