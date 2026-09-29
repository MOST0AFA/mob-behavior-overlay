package com.mobbehavioroverlay;

import net.fabricmc.api.ClientModInitializer;

public class MobBehaviorOverlay implements ClientModInitializer {
	public static final String MOD_ID = "mobbehavioroverlay";

	@Override
	public void onInitializeClient() {
		// Nothing to register: the outline is driven by EntityMixin.
	}
}
