package com.example.lobbymenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class DarkButton extends Button {

    public DarkButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean highlighted = this.isHoveredOrFocused();

        int bg     = highlighted ? 0xE0333333 : 0xCC111111;
        int border = highlighted ? 0xFF777777 : 0xFF444444;

        guiGraphics.fill(this.getX(), this.getY(),
                this.getX() + this.width, this.getY() + this.height, bg);
        guiGraphics.renderOutline(this.getX(), this.getY(),
                this.width, this.height, border);

        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + this.width / 2,
                this.getY() + (this.height - 8) / 2,
                0xFFFFFF
        );
    }
}
