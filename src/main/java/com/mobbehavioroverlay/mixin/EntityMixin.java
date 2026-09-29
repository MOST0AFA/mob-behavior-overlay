package com.mobbehavioroverlay.mixin;

import com.mobbehavioroverlay.MobStance;
import com.mobbehavioroverlay.config.OverlayConfig;
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
		OverlayConfig cfg = OverlayConfig.get();
		if (!cfg.enabled || !self.level().isClientSide()) {
			return null;
		}
		Player player = Minecraft.getInstance().player;
		if (player == null || self == player.getVehicle()
				|| self.distanceToSqr(player) > (double) cfg.range * cfg.range
				|| !cfg.isMobEnabled(self.getType())) {
			return null;
		}
		MobStance stance = MobStance.of(self);
		if (stance == null || !cfg.showsStance(stance)) {
			return null;
		}
		if (cfg.requireLineOfSight && !player.hasLineOfSight(self)) {
			return null;
		}
		return stance;
	}
}
