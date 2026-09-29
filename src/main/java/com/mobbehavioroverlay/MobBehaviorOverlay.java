package com.mobbehavioroverlay;

import com.mobbehavioroverlay.config.OverlayConfig;
import com.mobbehavioroverlay.gui.ConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobBehaviorOverlay implements ClientModInitializer {
	public static final String MOD_ID = "mobbehavioroverlay";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		OverlayConfig.load();

		KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));
		KeyMapping openConfig = KeyMappingHelper.registerKeyMapping(
				new KeyMapping("key.mobbehavioroverlay.open_config", InputConstants.Type.KEYSYM, InputConstants.KEY_O, category));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openConfig.consumeClick()) {
				client.gui.setScreen(new ConfigScreen(client.gui.screen()));
			}
		});
	}
}
