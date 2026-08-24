package dev.ftb.mods.ftbauxilium.integration;

import dev.ftb.mods.ftbauxilium.screens.OptOutScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Consumer;

public class KubeJSActions {
    public static final Consumer<Screen> OPT_OUT = screen -> {
        Minecraft.getInstance().setScreen(new OptOutScreen(screen));
    };
}