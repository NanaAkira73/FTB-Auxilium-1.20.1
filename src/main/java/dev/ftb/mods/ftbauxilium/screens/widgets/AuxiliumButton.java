package dev.ftb.mods.ftbauxilium.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AuxiliumButton extends Button {
    private static final ResourceLocation BACKGROUND = new ResourceLocation("ftbauxilium", "textures/auxilium-button.png");

    public AuxiliumButton(int x, int y, int w, int h, OnPress onPress) {
        super(x, y, w, h, Component.empty(), onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, BACKGROUND);
        RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

        int offset = isHoveredOrFocused() ? 20 : 0;
        graphics.blit(BACKGROUND, getX(), getY(), 0, offset, width, height, 200, 40);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.disableBlend();
    }
}