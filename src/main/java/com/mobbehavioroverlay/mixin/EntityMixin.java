package com.mobbehavioroverlay.mixin;

import com.mobbehavioroverlay.MobStance;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Reuses vanilla's thin glow outline (the same one team colors use) so the
 * overlay stays small, needs no custom rendering, and is tinted by mob stance.
 */
@Mixin(Entity.class)
public abstract class EntityMixin {
	private static final double RANGE_SQ = 24.0 * 24.0;

	@Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
	private void mbo$outline(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ() || mbo$stance() == null) {
			return;
		}
		cir.setReturnValue(true);
	}

	@Inject(method = "getTeamColor", at = @At("RETURN"), cancellable = true)
	private void mbo$color(CallbackInfoReturnable<Integer> cir) {
		MobStance stance = mbo$stance();
		if (stance != null) {
			cir.setReturnValue(stance.rgb);
		}
	}

	private MobStance mbo$stance() {
		Entity self = (Entity) (Object) this;
		if (!self.level().isClientSide()) {
			return null;
		}
		Player player = Minecraft.getInstance().player;
		if (player == null || self.distanceToSqr(player) > RANGE_SQ || !player.hasLineOfSight(self)) {
			return null;
		}
		return MobStance.of(self);
	}
}
