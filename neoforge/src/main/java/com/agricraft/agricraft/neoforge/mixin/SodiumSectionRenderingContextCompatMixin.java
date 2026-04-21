package com.agricraft.agricraft.neoforge.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.neoforge.level.NeoForgeLevelRenderHooks", remap = false)
public abstract class SodiumSectionRenderingContextCompatMixin {

	@Redirect(
			method = "runChunkMeshAppenders",
			at = @At(
					value = "NEW",
					target = "(Ljava/util/function/Function;Lnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/client/renderer/block/ModelBlockRenderer;Lnet/minecraft/core/BlockPos;)Lnet/neoforged/neoforge/client/event/AddSectionGeometryEvent$SectionRenderingContext;"
			),
			require = 0,
			remap = false
	)
	private static AddSectionGeometryEvent.SectionRenderingContext agricraft$compat$redirectLegacyContextCtor(
			Function<ChunkSectionLayer, VertexConsumer> typeToConsumer,
			BlockAndTintGetter region,
			ModelBlockRenderer blockRenderer,
			BlockPos ignoredOrigin
	) {
		// NeoForge 26.1.2.x removed the BlockPos parameter from SectionRenderingContext.
		return new AddSectionGeometryEvent.SectionRenderingContext(typeToConsumer, region, blockRenderer);
	}
}
