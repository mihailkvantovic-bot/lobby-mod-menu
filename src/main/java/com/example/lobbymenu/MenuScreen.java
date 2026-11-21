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

        // Кнопка "Начать игру" — в правом нижнем углу
        this.addRenderableWidget(new DarkButton(
                this.width - btnW - 15,
                this.height - btnH - 15,
                btnW, btnH,
                Component.literal("Начать игру"),
                button -> {
                    if (this.minecraft != null && this.minecraft.player != null) {
                        this.minecraft.player.connection.sendCommand("function lobby:start");
                    }
                }
        ));

        // Кнопка "Назад" — по центру
        this.addRenderableWidget(new DarkButton(
                this.width / 2 - btnW / 2,
                this.height / 2 - btnH / 2,
                btnW, btnH,
                Component.literal("Назад"),
                button -> this.onClose()
        ));
    }

    // Полупрозрачный фон на весь экран (перекрываем стандартный фон/блюр)
    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, 0x99000000);
    }

    // Не ставим игру на паузу
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}