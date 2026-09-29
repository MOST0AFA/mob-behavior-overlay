package com.mobbehavioroverlay.gui;

import com.mobbehavioroverlay.config.OverlayConfig;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/** Main settings screen (default key: O). */
public class ConfigScreen extends Screen {
	private static final int W = 200;
	private static final int H = 20;
	private static final int GAP = 24;

	private final Screen parent;

	public ConfigScreen(Screen parent) {
		super(Component.translatable("mobbehavioroverlay.config.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		OverlayConfig cfg = OverlayConfig.get();
		int x = this.width / 2 - W / 2;
		int y = Math.max(28, this.height / 2 - 4 * GAP - 8);

		addRenderableWidget(new StringWidget(x, 12, W, 9, this.title, this.font).alignCenter());

		addToggle(x, y, "enabled", () -> cfg.enabled, v -> cfg.enabled = v);
		addRenderableWidget(new RangeSlider(x, y += GAP, cfg));
		addToggle(x, y += GAP, "line_of_sight", () -> cfg.requireLineOfSight, v -> cfg.requireLineOfSight = v);

		int third = (W - 8) / 3;
		addToggleSized(x, y += GAP, third, "hostile", () -> cfg.showHostile, v -> cfg.showHostile = v);
		addToggleSized(x + third + 4, y, third, "neutral", () -> cfg.showNeutral, v -> cfg.showNeutral = v);
		addToggleSized(x + 2 * (third + 4), y, third, "friendly", () -> cfg.showFriendly, v -> cfg.showFriendly = v);

		addRenderableWidget(Button.builder(Component.translatable("mobbehavioroverlay.config.choose_mobs"),
				b -> this.minecraft.gui.setScreen(new MobSelectScreen(this)))
				.bounds(x, y += GAP, W, H).build());

		addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> onClose())
				.bounds(x, y += GAP + 8, W, H).build());
	}

	private void addToggle(int x, int y, String key, Supplier<Boolean> get, Consumer<Boolean> set) {
		addToggleSized(x, y, W, key, get, set);
	}

	private void addToggleSized(int x, int y, int w, String key, Supplier<Boolean> get, Consumer<Boolean> set) {
		Component name = Component.translatable("mobbehavioroverlay.config." + key);
		addRenderableWidget(Button.builder(label(name, get.get()), b -> {
			boolean next = !get.get();
			set.accept(next);
			b.setMessage(label(name, next));
		}).bounds(x, y, w, H).build());
	}

	static Component label(Component name, boolean on) {
		return Component.empty().append(name).append(": ").append(on ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
	}

	@Override
	public void onClose() {
		OverlayConfig.save();
		this.minecraft.gui.setScreen(parent);
	}

	/** Slider for the outline distance in blocks. */
	private static class RangeSlider extends AbstractSliderButton {
		private final OverlayConfig cfg;

		RangeSlider(int x, int y, OverlayConfig cfg) {
			super(x, y, W, H, Component.empty(),
					(double) (cfg.range - OverlayConfig.MIN_RANGE) / (OverlayConfig.MAX_RANGE - OverlayConfig.MIN_RANGE));
			this.cfg = cfg;
			updateMessage();
		}

		@Override
		protected void updateMessage() {
			setMessage(Component.translatable("mobbehavioroverlay.config.range", cfg.range));
		}

		@Override
		protected void applyValue() {
			cfg.range = OverlayConfig.MIN_RANGE
					+ (int) Math.round(this.value * (OverlayConfig.MAX_RANGE - OverlayConfig.MIN_RANGE));
		}
	}
}
