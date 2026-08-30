package com.quartz.climb;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public final class QuartzClimbClient implements ClientModInitializer {
    public static final String MOD_ID = "quartzclimb";
    public static final ClimbController CONTROLLER = new ClimbController();
    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.quartzclimb.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.quartzclimb"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                CONTROLLER.toggle(client);
            }
            CONTROLLER.tick(client);
        });
    }
}
