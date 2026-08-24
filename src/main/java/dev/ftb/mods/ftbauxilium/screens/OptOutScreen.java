package dev.ftb.mods.ftbauxilium.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OptOutScreen extends Screen {
    private final Screen parent;

    public OptOutScreen(Screen parent) {
        super(Component.translatable("ftbauxilium.opt_out"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int w = 200;
        int h = 20;
        int x = (this.width - w) / 2;
        int y = this.height / 2 - 30;

        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
            Component.translatable("ftbauxilium.btn.opt_out"),
            btn -> {
                // Opt-out logic
                if (this.minecraft != null && this.minecraft.player != null) {
                    this.minecraft.player.displayClientMessage(
                        Component.translatable("ftbauxilium.msg.opted_out"), false);
                }
                this.minecraft.setScreen(parent);
            }
        ).bounds(x, y, w, h).build());

        addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
            Component.translatable("ftbauxilium.btn_cancel"),
            btn -> this.minecraft.setScreen(parent)
        ).bounds(x, y + 30, w, h).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
}