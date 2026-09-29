package com.mobbehavioroverlay.mixin;

import com.mobbehavioroverlay.MobStance;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Makes the renderer draw the outline for mobs we colour. */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Inject(method = "shouldEntityAppearGlowing(Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
	private void mbo$outline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (MobStance.outlineFor(entity) != null) {
			cir.setReturnValue(true);
		}
	}
}
