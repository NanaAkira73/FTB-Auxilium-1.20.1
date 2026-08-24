package dev.ftb.mods.ftbauxilium.integration;

import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import dev.ftb.mods.ftbauxilium.screens.OptOutScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ModList;

import java.util.function.Consumer;

public class FTBAuxiliumKubeJSPlugin {
    public static final String OPT_OUT_ACTION = "ftbauxilium:opt_out";

    public static void init() {
        if (!ModList.get().isLoaded("kubejs")) {
            return;
        }

        try {
            Class<?> uiDataClass = Class.forName("dev.latvian.mods.kubejs.ui.UIData");
            java.lang.reflect.Method registerAction = uiDataClass.getMethod("registerAction", String.class, Consumer.class);

            Consumer<Object> action = screen -> {
                Minecraft.getInstance().setScreen(new OptOutScreen(
                    (net.minecraft.client.gui.screens.Screen) screen
                ));
            };
            registerAction.invoke(null, OPT_OUT_ACTION, action);
            FTBAuxilium.LOGGER.info("Registered KubeJS action: {}", OPT_OUT_ACTION);
        } catch (Exception e) {
            FTBAuxilium.LOGGER.warn("Failed to register KubeJS action: {}", e.getMessage());
        }
    }
}