package com.mobbehavioroverlay;

import com.mobbehavioroverlay.config.OverlayConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

/** Classifies a mob and maps it to an outline color (RGB). */
public enum MobStance {
	HOSTILE(0xFF3B3B),
	NEUTRAL(0xFFD83B),
	FRIENDLY(0x3BFF5A);

	public final int rgb;

	MobStance(int rgb) {
		this.rgb = rgb;
	}

	/** @return the stance, or null if this entity should get no outline. */
	public static MobStance of(Entity entity) {
		if (!(entity instanceof Mob mob) || entity instanceof Player) {
			return null;
		}
		// Tamed pets are always friendly.
		if (mob instanceof TamableAnimal pet && pet.isTame()) {
			return FRIENDLY;
		}
		if (mob instanceof AbstractHorse horse && horse.isTamed()) {
			return FRIENDLY;
		}
		// Check NeutralMob before Enemy: endermen and zombified piglins are both,
		// and must be yellow until provoked.
		if (mob instanceof NeutralMob neutral) {
			// Anger time isn't always synced to the client, so also honor the
			// synced "aggressive" flag (set while the mob is attacking).
			return neutral.isAngry() || mob.isAggressive() ? HOSTILE : NEUTRAL;
		}
		if (mob instanceof Enemy) {
			return HOSTILE;
		}
		return FRIENDLY;
	}

	/**
	 * The stance to outline this entity with right now, applying all user settings
	 * (enabled, range, line of sight, category and per-mob filters), or null for no outline.
	 */
	public static MobStance outlineFor(Entity self) {
		OverlayConfig cfg = OverlayConfig.get();
		if (!cfg.enabled || !(self.level() instanceof ClientLevel)) {
			return null;
		}
		Player player = Minecraft.getInstance().player;
		if (player == null || self == player.getVehicle()
				|| self.distanceToSqr(player) > (double) cfg.range * cfg.range
				|| !cfg.isMobEnabled(self.getType())) {
			return null;
		}
		MobStance stance = of(self);
		if (stance == null || !cfg.showsStance(stance)) {
			return null;
		}
		if (cfg.requireLineOfSight && !player.hasLineOfSight(self)) {
			return null;
		}
		return stance;
	}
}
