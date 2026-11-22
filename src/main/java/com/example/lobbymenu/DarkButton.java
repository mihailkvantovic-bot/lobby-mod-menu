package com.example.lobbymenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class DarkButton extends Button {

    private float animProgress = 1.0f; // По умолчанию 1 (без прозрачности)

    public DarkButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    // Метод для управления анимацией
    public void setAnimProgress(float progress) {
        this.animProgress = progress;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean highlighted = this.isHoveredOrFocused();

        // Высчитываем прозрачность (альфа-канал) в зависимости от прогресса анимации
        int alpha = (int) (0xCC * animProgress);
        int bg = (alpha << 24) | (highlighted ? 0x333333 : 0x111111);
        int border = (alpha << 24) | (highlighted ? 0x777777 : 0x444444);

        guiGraphics.fill(this.getX(), this.getY(),
                this.getX() + this.width, this.getY() + this.height, bg);
        guiGraphics.renderOutline(this.getX(), this.getY(),
                this.width, this.height, border);

        // Текст тоже плавно проявляется
        int textAlpha = (int) (255 * animProgress);
        int textColor = (textAlpha << 24) | 0xFFFFFF;

        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + this.width / 2,
                this.getY() + (this.height - 8) / 2,
                textColor
        );
    }
}
