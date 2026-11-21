package com.example.lobbymenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MenuScreen extends Screen {

    public MenuScreen() {
        super(Component.literal("Меню"));
    }

    @Override
    protected void init() {
        int btnW = 160;
        int btnH = 22;
        int margin = 15; // Отступ от краев экрана

        // 1. Кнопка "Начать игру" — в правом нижнем углу (остается без изменений)
        this.addRenderableWidget(new DarkButton(
                this.width - btnW - margin,
                this.height - btnH - margin,
                btnW, btnH,
                Component.literal("Начать игру"),
                button -> {
                    if (this.minecraft != null && this.minecraft.player != null) {
                        this.minecraft.player.connection.sendCommand("function lobby:start");
                    }
                }
        ));

        // 2. Кнопка "Игрок" — в левом нижнем углу
        this.addRenderableWidget(new DarkButton(
                margin,
                this.height - btnH - margin,
                btnW, btnH,
                Component.literal("Игрок"),
                button -> {
                    if (this.minecraft != null && this.minecraft.player != null) {
                        this.minecraft.player.connection.sendCommand("team join runners @s");
                    }
                }
        ));

        // 3. Кнопка "Охотник" — в левом нижнем углу, рядом с "Игрок"
        // (margin + ширина кнопки + 10 пикселей отступа между ними)
        this.addRenderableWidget(new DarkButton(
                margin + btnW + 10,
                this.height - btnH - margin,
                btnW, btnH,
                Component.literal("Охотник"),
                button -> {
                    if (this.minecraft != null && this.minecraft.player != null) {
                        this.minecraft.player.connection.sendCommand("team join hunters @s");
                    }
                }
        ));

        // 4. Кнопка "Назад" — теперь в ЛЕВОМ ВЕРХНЕМ углу
        this.addRenderableWidget(new DarkButton(
                margin,
                margin,
                btnW, btnH,
                Component.literal("Назад"),
                button -> this.onClose()
        ));
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, this.width, this.height, 0x99000000);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
