package com.agricraft.agricraft.mixin;

import com.agricraft.agricraft.api.config.CoreConfig;
import com.agricraft.agricraft.common.block.CropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class FarmBlockMixin {

	@Inject(method = "turnToDirt", at = @At("HEAD"), cancellable = true)
	private static void agricraft$preventTrampling(Entity entity, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
		if (CoreConfig.cropSticksPreventTrampling) {
			BlockState above = level.getBlockState(pos.above());
			if (above.getBlock() instanceof CropBlock && above.getValue(CropBlock.CROP_STATE).hasSticks()) {
				ci.cancel();
			}
		}
	}

}
