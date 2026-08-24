package dev.ftb.mods.ftbauxilium;

import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftbauxilium.auxilium.StatManager;
import dev.ftb.mods.ftbauxilium.screens.ScreenEvents;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(FTBAuxilium.MOD_ID)
public class FTBAuxilium {
    public static final String MOD_ID = "ftbauxilium";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final StatManager STAT_MANAGER = new StatManager();
    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public FTBAuxilium() {
        LOGGER.info("FTB Auxilium loaded");
        init();
    }

    public void init() {
        FTBAuxiliumConfig.init();
        STAT_MANAGER.init();
        MinecraftForge.EVENT_BUS.addListener(this::onScreenInit);
    }

    private void onScreenInit(ScreenEvent.Init.Post event) {
        ScreenEvents.loadOptOutButton(event.getScreen(), event);
    }

    public static void runTask(Runnable task) {
        executorService.submit(task);
    }
}