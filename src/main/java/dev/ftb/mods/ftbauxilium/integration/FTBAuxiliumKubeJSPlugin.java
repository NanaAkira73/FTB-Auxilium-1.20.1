package dev.ftb.mods.ftbauxilium.integration;

import dev.ftb.mods.ftbauxilium.screens.OptOutScreen;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.ui.UIData;

public class FTBAuxiliumKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void init() {
        UIData.registerAction("ftbauxilium:opt_out", screen -> {
            net.minecraft.client.Minecraft.getInstance().setScreen(new OptOutScreen(screen));
        });
    }
}