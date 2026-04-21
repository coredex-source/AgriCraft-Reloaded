package com.agricraft.agricraft.mixin;

import dev.eclipseui.gui.widget.OptionWidget;
import dev.eclipseui.util.Dim2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = OptionWidget.class, remap = false)
public abstract class OptionWidgetMixin {

	private static final int LABEL_WIDTH_PERCENT = 68;
	private static final int CONTROL_PADDING = 4;
	private static final int MIN_CONTROL_WIDTH = 96;

	@Inject(method = "getLabelDim", at = @At("HEAD"), cancellable = true, remap = false)
	private void agricraft$widenLabelArea(CallbackInfoReturnable<Dim2i> cir) {
		Dim2i dim = ((OptionWidget) (Object) this).getDim();
		int labelWidth = this.agricraft$computeLabelWidth();
		cir.setReturnValue(new Dim2i(dim.x(), dim.y(), labelWidth, dim.height()));
	}

	@Inject(method = "getControlDim", at = @At("HEAD"), cancellable = true, remap = false)
	private void agricraft$rightAlignControlArea(CallbackInfoReturnable<Dim2i> cir) {
		Dim2i dim = ((OptionWidget) (Object) this).getDim();
		int labelWidth = this.agricraft$computeLabelWidth();
		int controlWidth = Math.max(MIN_CONTROL_WIDTH, dim.width() - labelWidth - CONTROL_PADDING);
		int controlX = dim.getLimitX() - controlWidth;
		cir.setReturnValue(new Dim2i(controlX, dim.y(), controlWidth, dim.height()));
	}

	private int agricraft$computeLabelWidth() {
		Dim2i dim = ((OptionWidget) (Object) this).getDim();
		int computed = dim.width() * LABEL_WIDTH_PERCENT / 100;
		int maxAllowed = Math.max(0, dim.width() - MIN_CONTROL_WIDTH - CONTROL_PADDING);
		return Math.min(computed, maxAllowed);
	}
}
