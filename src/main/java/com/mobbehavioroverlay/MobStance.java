package com.mobbehavioroverlay;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
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
		if (mob instanceof Enemy) {
			return HOSTILE;
		}
		if (mob instanceof NeutralMob neutral) {
			// Neutral mobs turn red once angry.
			return neutral.isAngry() ? HOSTILE : NEUTRAL;
		}
		return FRIENDLY;
	}
}
