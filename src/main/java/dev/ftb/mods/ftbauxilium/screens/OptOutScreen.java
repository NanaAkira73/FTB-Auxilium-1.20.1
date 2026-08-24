package dev.ftb.mods.ftbauxilium.screens;

import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import dev.ftb.mods.ftbauxilium.auxilium.AnonymousIdentification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class OptOutScreen extends Screen {
    private final Screen parent;
    private final Map<String, MultiLineLabel> lineCache = new HashMap<>();
    private Button optInBtn;
    private Button optOutBtn;

    public OptOutScreen(Screen parent) {
        super(Component.translatable("ftbauxilium.opt_out"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        AnonymousIdentification id = FTBAuxilium.STAT_MANAGER.IDENTIFIER;
        boolean opted = id.isOptedOut();

        int startX = this.width / 2 - 100;

        optOutBtn = Button.builder(
            Component.translatable(opted ? "ftbauxilium.btn.opt-out" : "ftbauxilium.btn-opt-in"),
            btn -> {
                if (opted) {
                    id.optIn();
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.translatable("ftbauxilium.msg.opted-in"), false);
                    }
                } else {
                    id.optOut();
                    if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.translatable("ftbauxilium.msg.opted-out"), false);
                    }
                }
                minecraft.setScreen(parent);
            }
        ).bounds(startX, this.height / 2 + 40, 200, 20).build();
        addRenderableWidget(optOutBtn);

        addRenderableWidget(Button.builder(
            Component.translatable("ftbauxilium.btn-cancel"),
            btn -> minecraft.setScreen(parent)
        ).bounds(startX, this.height / 2 + 70, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        int alignX = this.width / 2;
        int alignY = 20;

        drawCenteredString(graphics, font, Component.translatable("ftbauxilium.heading-what-is"), alignX, alignY, 0xFFFFFF);
        alignY += 15;
        drawCenteredString(graphics, font, Component.translatable("ftbauxilium.what-is"), alignX, alignY, 0xBBBBBB);
        alignY += 25;
        drawCenteredString(graphics, font, Component.translatable("ftbauxilium.heading-what-info"), alignX, alignY, 0xFFFFFF);
        alignY += 15;
        drawCenteredString(graphics, font, Component.translatable("ftbauxilium.what-info"), alignX, alignY, 0xBBBBBB);
        alignY += 25;
        drawCenteredString(graphics, font, Component.translatable(
            FTBAuxilium.STAT_MANAGER.IDENTIFIER.isOptedOut() ? "ftbauxilium.heading-opt-in" : "ftbauxilium.heading-opt-out"
        ), alignX, alignY, 0xFFFFFF);
        alignY += 15;
        drawCenteredString(graphics, font, Component.translatable(
            FTBAuxilium.STAT_MANAGER.IDENTIFIER.isOptedOut() ? "ftbauxilium.opt-in" : "ftbauxilium.opt-out"
        ), alignX, alignY, 0xBBBBBB);
    }
}