package com.quartz.climb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.player.LocalPlayer;

/** Reserved hook for renderer/pose integration. */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
}
