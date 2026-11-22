package com.example.lobbymenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;

public class MenuScreen extends Screen {

    // 0 = Игрок (Runner), 1 = Охотник (Hunter)
    private int selectedTeam = 0; 

    public MenuScreen() {
        super(Component.literal("Меню"));
    }

    @Override
    protected void init() {
        int btnW = 110; // Сделали кнопки короче
        int btnH = 20;
        int margin = 15;

        // 1. Кнопка "Назад" — в ЛЕВОМ ВЕРХНЕМ углу
        this.addRenderableWidget(new DarkButton(
                margin, margin,
                btnW, btnH,
                Component.literal("Назад"),
                button -> this.onClose()
        ));

        // 2. Кнопка "Начать игру" — в правом нижнем углу
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

        // 3. Кнопка "Игрок" — в левом нижнем углу
        this.addRenderableWidget(new DarkButton(
                margin,
                this.height - btnH - margin,
                btnW, btnH,
                Component.literal("Игрок"),
                button -> {
                    this.selectedTeam = 0;
                    if (this.minecraft != null && this.minecraft.player != null) {
                        this.minecraft.player.connection.sendCommand("team join runners @s");
                    }
                }
        ));

        // 4. Кнопка "Охотник" — НАД кнопкой "Игрок"
        this.addRenderableWidget(new DarkButton(
                margin,
                this.height - (btnH * 2) - margin - 5, // 5 пикселей отступа между кнопками
                btnW, btnH,
                Component.literal("Охотник"),
                button -> {
                    this.selectedTeam = 1;
                    if (this.minecraft != null && this.minecraft.player != null) {
                        this.minecraft.player.connection.sendCommand("team join hunters @s");
                    }
                }
        ));
    }

    // Затемняем экран сильнее
    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, this.width, this.height, 0xCC000000);
    }

    // Отрисовка 3D-модели и текста
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (this.minecraft != null && this.minecraft.player != null) {
            // 1. Рисуем никнейм по центру сверху
            String name = this.minecraft.player.getDisplayName().getString();
            guiGraphics.drawCenteredString(this.font, name, this.width / 2, this.height / 2 - 90, 0xFFFFFF);

            // 2. Рисуем текст выбранной роли
            String role = this.selectedTeam == 0 ? "Роль: Игрок" : "Роль: Охотник";
            int color = this.selectedTeam == 0 ? 0x55FF55 : 0xFF5555; // Зеленый для игрока, красный для охотника
            guiGraphics.drawCenteredString(this.font, role, this.width / 2, this.height / 2 - 75, color);

            // 3. Рисуем 3D-модель игрока
            int scale = 40; // Размер модели
            int modelX = this.width / 2;
            int modelY = this.height / 2 + 60; // Сдвиг вниз от центра

            // Формула поворота модели за мышкой (как в инвентаре)
            float rotationX = (float)(modelX) - mouseX;
            float rotationY = (float)(modelY - 50) - mouseY;

            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    guiGraphics,
                    modelX, modelY,
                    scale,
                    rotationX, rotationY,
                    this.minecraft.player
            );
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
