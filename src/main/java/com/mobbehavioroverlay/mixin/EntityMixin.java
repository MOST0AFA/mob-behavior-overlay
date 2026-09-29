package com.mobbehavioroverlay.mixin;

import com.mobbehavioroverlay.MobStance;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Tints vanilla's glow outline (the one team colors use) by mob stance. */
@Mixin(Entity.class)
public abstract class EntityMixin {
	@Inject(method = "getTeamColor()I", at = @At("HEAD"), cancellable = true)
	private void mbo$color(CallbackInfoReturnable<Integer> cir) {
		MobStance stance = MobStance.outlineFor((Entity) (Object) this);
		if (stance != null) {
			// Opaque ARGB, same as other 26.x mods that recolor outlines.
			cir.setReturnValue(0xFF000000 | stance.rgb);
		}
	}
}
