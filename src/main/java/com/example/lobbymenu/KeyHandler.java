package com.example.lobbymenu;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {

    public static final KeyMapping OPEN_MENU = new KeyMapping(
            "key.lobbymenu.open",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "category.lobbymenu"
    );

    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_MENU);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        while (OPEN_MENU.consumeClick()) {
            if (mc.screen == null && mc.player != null) {
                mc.setScreen(new MenuScreen());
            }
        }
    }
}