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

    // Анимируем затемнение фона (В Forge 1.20.1 тут только один аргумент!)
    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        long elapsed = System.currentTimeMillis() - this.openedAt;
        float progress = Math.min(1.0f, elapsed / 300.0f); // Анимация длится 300мс
        
        // Ease-out cubic (плавное замедление к концу)
        float eased = 1.0f - (float)Math.pow(1.0f - progress, 3.0f);
        int bgAlpha = (int)(0xCC * eased) << 24;

        guiGraphics.fill(0, 0, this.width, this.height, bgAlpha);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        long elapsed = System.currentTimeMillis() - this.openedAt;
        float progress = Math.min(1.0f, elapsed / 300.0f);
        float eased = 1.0f - (float)Math.pow(1.0f - progress, 3.0f);

        // Передаем прогресс анимации во все кнопки перед их отрисовкой
        for (var widget : this.renderables) {
            if (widget instanceof DarkButton btn) {
                btn.setAnimProgress(eased);
            }
        }

        // Стандартный рендер экрана (вызывает renderBackground и рисует кнопки)
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Рисуем никнейм, текст и модель
        if (this.minecraft != null && this.minecraft.player != null) {
            int textAlpha = (int)(255 * eased) << 24;

            // Никнейм (проявляется)
            String name = this.minecraft.player.getDisplayName().getString();
            guiGraphics.drawCenteredString(this.font, name, this.width / 2, this.height / 2 - 90, textAlpha | 0xFFFFFF);

            // Роль (проявляется)
            String role = this.selectedTeam == 0 ? "Роль: Игрок" : "Роль: Охотник";
            int roleColor = this.selectedTeam == 0 ? 0x55FF55 : 0xFF5555;
            guiGraphics.drawCenteredString(this.font, role, this.width / 2, this.height / 2 - 75, textAlpha | roleColor);

            // 3D Модель
            int scale = 40;
            int modelX = this.width / 2;
            int modelY = this.height / 2 + 60;
            float rotationX = (float)(modelX) - mouseX;
            float rotationY = (float)(modelY - 50) - mouseY;

            // Анимация масштабирования модели (вырастает из 0 до 1)
            // Защита от нулевого масштаба, чтобы игра не крашнулась на первом кадре
            float modelScale = Math.max(0.01f, eased); 

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(modelX, modelY, 0);
            guiGraphics.pose().scale(modelScale, modelScale, modelScale);
            guiGraphics.pose().translate(-modelX, -modelY, 0);

            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    guiGraphics,
                    modelX, modelY,
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
