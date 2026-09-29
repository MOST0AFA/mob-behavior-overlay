package com.mobbehavioroverlay.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobbehavioroverlay.MobBehaviorOverlay;
import com.mobbehavioroverlay.MobStance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/** User settings, stored as JSON in the Fabric config directory. */
public class OverlayConfig {
	public static final int MIN_RANGE = 4;
	public static final int MAX_RANGE = 64;

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("mobbehavioroverlay.json");
	private static OverlayConfig instance = new OverlayConfig();

	public boolean enabled = true;
	/** Outline distance in blocks. */
	public int range = 24;
	/** Only outline mobs you can actually see (no x-ray through walls). */
	public boolean requireLineOfSight = true;
	public boolean showHostile = true;
	public boolean showNeutral = true;
	public boolean showFriendly = true;
	/** Registry ids (e.g. "minecraft:zombie") of mobs the user switched off. */
	public Set<String> disabledMobs = new HashSet<>();

	public static OverlayConfig get() {
		return instance;
	}

	public boolean showsStance(MobStance stance) {
		return switch (stance) {
			case HOSTILE -> showHostile;
			case NEUTRAL -> showNeutral;
			case FRIENDLY -> showFriendly;
		};
	}

	public boolean isMobEnabled(EntityType<?> type) {
		return disabledMobs.isEmpty() || !disabledMobs.contains(BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
	}

	public void setMobEnabled(EntityType<?> type, boolean on) {
		String id = BuiltInRegistries.ENTITY_TYPE.getKey(type).toString();
		if (on) {
			disabledMobs.remove(id);
		} else {
			disabledMobs.add(id);
		}
	}

	public static void load() {
		if (!Files.exists(FILE)) {
			save();
			return;
		}
		try (Reader reader = Files.newBufferedReader(FILE)) {
			OverlayConfig loaded = GSON.fromJson(reader, OverlayConfig.class);
			if (loaded != null) {
				if (loaded.disabledMobs == null) {
					loaded.disabledMobs = new HashSet<>();
				}
				loaded.range = Math.max(MIN_RANGE, Math.min(MAX_RANGE, loaded.range));
				instance = loaded;
			}
		} catch (IOException | RuntimeException e) {
			// Corrupt or unreadable file: keep defaults rather than crash the game.
			MobBehaviorOverlay.LOGGER.warn("Could not read {}, using defaults", FILE, e);
		}
	}

	public static void save() {
		try (Writer writer = Files.newBufferedWriter(FILE)) {
			GSON.toJson(instance, writer);
		} catch (IOException e) {
			MobBehaviorOverlay.LOGGER.warn("Could not write {}", FILE, e);
		}
	}
}
