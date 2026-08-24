package dev.ftb.mods.ftbauxilium.screens;

import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.ftb.mods.ftbauxilium.screens.widgets.AuxiliumButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;

public class ScreenEvents {
    public static void loadOptOutButton(Screen screen, ScreenAccess screenAccess) {
        if (screen instanceof OptionsScreen) {
            AuxiliumButton button = new AuxiliumButton(
                screen.width / 2 - 100, screen.height / 6 + 144,
                200, 20,
                btn -> Minecraft.getInstance().setScreen(new OptOutScreen(screen))
            );
            screenAccess.addRenderableWidget(button);
        }
    }
}