package com.example.lobbymenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;

public class MenuScreen extends Screen {

    private int selectedTeam = 0; 
    private long openedAt; // Время открытия для анимации

    public MenuScreen() {
        super(Component.literal("Меню"));
    }

    @Override
    protected void init() {
        this.openedAt = System.currentTimeMillis(); // Запоминаем время старта

        int btnW = 110;
        int btnH = 20;
        int margin = 15;

        this.addRenderableWidget(new DarkButton(
                margin, margin,
                btnW, btnH,
                Component.literal("Назад"),
                button -> this.onClose()
        ));

        // Кнопка "Начать игру" - теперь закрывает меню
        this.addRenderableWidget(new DarkButton(
                this.width - btnW - margin,
                this.height - btnH - margin,
                btnW, btnH,
                Component.literal("Начать игру"),
                button -> {
                    if (this.minecraft != null && this.minecraft.player != null) {
                        this.minecraft.player.connection.sendCommand("function lobby:start");
                    }
                    this.onClose(); // Закрываем меню после отправки команды
                }
        ));

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

        this.addRenderableWidget(new DarkButton(
                margin,
                this.height - (btnH * 2) - margin - 5,
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

    // Анимируем затемнение фона
    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        long elapsed = System.currentTimeMillis() - this.openedAt;
        float progress = Math.min(1.0f, elapsed / 300.0f); 
        
        float eased = 1.0f - (float)Math.pow(1.0f - progress, 3.0f);
        int bgAlpha = (int)(0xCC * eased) << 24;

        guiGraphics.fill(0, 0, this.width, this.height, bgAlpha);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        long elapsed = System.currentTimeMillis() - this.openedAt;
        float progress = Math.min(1.0f, elapsed / 300.0f);
        float eased = 1.0f - (float)Math.pow(1.0f - progress, 3.0f);

        for (var widget : this.renderables) {
            if (widget instanceof DarkButton btn) {
                btn.setAnimProgress(eased);
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (this.minecraft != null && this.minecraft.player != null) {
            int textAlpha = (int)(255 * eased) << 24;

            // Координаты для левого верхнего угла (под кнопкой Назад)
            int playerX = 70;   // Середина по X
            int nameY = 50;     // Высота ника
            int roleY = 65;     // Высота роли
            int modelY = 130;   // Низ 3D-модели

            // Никнейм
            String name = this.minecraft.player.getDisplayName().getString();
            guiGraphics.drawCenteredString(this.font, name, playerX, nameY, textAlpha | 0xFFFFFF);

            // Роль
            String role = this.selectedTeam == 0 ? "Роль: Игрок" : "Роль: Охотник";
            int roleColor = this.selectedTeam == 0 ? 0x55FF55 : 0xFF5555;
            guiGraphics.drawCenteredString(this.font, role, playerX, roleY, textAlpha | roleColor);

            // 3D Модель
            int scale = 30; // Чуть уменьшили, чтобы модель аккуратно смотрелась в углу
            float rotationX = (float)(playerX) - mouseX;
            float rotationY = (float)(modelY - 50) - mouseY;

            float modelScale = Math.max(0.01f, eased); 

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(playerX, modelY, 0);
            guiGraphics.pose().scale(modelScale, modelScale, modelScale);
            guiGraphics.pose().translate(-playerX, -modelY, 0);

            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    guiGraphics,
                    playerX, modelY,
                    scale,
                    rotationX, rotationY,
                    this.minecraft.player
            );

            guiGraphics.pose().popPose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
