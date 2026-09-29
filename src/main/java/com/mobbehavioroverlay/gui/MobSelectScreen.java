package com.mobbehavioroverlay.gui;

import com.mobbehavioroverlay.config.OverlayConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/** Paged list of every mob type with an on/off toggle each. */
public class MobSelectScreen extends Screen {
	/** Living mobs vanilla files under MobCategory.MISC. */
	private static final Set<String> EXTRA = Set.of(
			"minecraft:villager", "minecraft:wandering_trader", "minecraft:iron_golem",
			"minecraft:snow_golem", "minecraft:copper_golem");

	private static final int BTN_H = 20;
	private static final int ROW = 22;

	private final Screen parent;
	private final List<EntityType<?>> mobs = new ArrayList<>();
	private int page;

	public MobSelectScreen(Screen parent) {
		super(Component.translatable("mobbehavioroverlay.config.choose_mobs"));
		this.parent = parent;
		for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
			if (type.getCategory() != MobCategory.MISC || EXTRA.contains(BuiltInRegistries.ENTITY_TYPE.getKey(type).toString())) {
				mobs.add(type);
			}
		}
		mobs.sort(Comparator.comparing(t -> t.getDescription().getString()));
	}

	@Override
	protected void init() {
		OverlayConfig cfg = OverlayConfig.get();
		int cols = this.width >= 440 ? 3 : 2;
		int colW = Math.min(150, (this.width - 20) / cols - 4);
		int rows = Math.max(1, (this.height - 90) / ROW);
		int perPage = cols * rows;
		int pages = Math.max(1, (mobs.size() + perPage - 1) / perPage);
		page = Math.max(0, Math.min(page, pages - 1));

		int left = this.width / 2 - (cols * (colW + 4) - 4) / 2;
		addRenderableWidget(Button.builder(this.title, b -> { }).bounds(this.width / 2 - 100, 4, 200, BTN_H).build()).active = false;

		int start = page * perPage;
		for (int i = 0; i < perPage && start + i < mobs.size(); i++) {
			EntityType<?> type = mobs.get(start + i);
			int x = left + (i % cols) * (colW + 4);
			int y = 30 + (i / cols) * ROW;
			addRenderableWidget(Button.builder(label(type, cfg.isMobEnabled(type)), b -> {
				boolean next = !cfg.isMobEnabled(type);
				cfg.setMobEnabled(type, next);
				b.setMessage(label(type, next));
			}).bounds(x, y, colW, BTN_H).build());
		}

		int by = this.height - 52;
		int cx = this.width / 2;
		Button prev = addRenderableWidget(Button.builder(Component.literal("<"), b -> turn(-1)).bounds(cx - 100, by, 40, BTN_H).build());
		Button next = addRenderableWidget(Button.builder(Component.literal(">"), b -> turn(1)).bounds(cx + 60, by, 40, BTN_H).build());
		prev.active = page > 0;
		next.active = page < pages - 1;
		Button pageLabel = addRenderableWidget(Button.builder(Component.literal((page + 1) + " / " + pages), b -> { })
				.bounds(cx - 58, by, 116, BTN_H).build());
		pageLabel.active = false;

		int by2 = this.height - 28;
		addRenderableWidget(Button.builder(Component.translatable("mobbehavioroverlay.config.all_on"), b -> setAll(true))
				.bounds(cx - 154, by2, 100, BTN_H).build());
		addRenderableWidget(Button.builder(Component.translatable("mobbehavioroverlay.config.all_off"), b -> setAll(false))
				.bounds(cx + 54, by2, 100, BTN_H).build());
		addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> onClose())
				.bounds(cx - 50, by2, 100, BTN_H).build());
	}

	private void turn(int delta) {
		page += delta;
		rebuildWidgets();
	}

	private void setAll(boolean on) {
		OverlayConfig cfg = OverlayConfig.get();
		for (EntityType<?> type : mobs) {
			cfg.setMobEnabled(type, on);
		}
		rebuildWidgets();
	}

	private static Component label(EntityType<?> type, boolean on) {
		return ConfigScreen.label(type.getDescription(), on);
	}

	@Override
	public void onClose() {
		OverlayConfig.save();
		this.minecraft.gui.setScreen(parent);
	}
}
