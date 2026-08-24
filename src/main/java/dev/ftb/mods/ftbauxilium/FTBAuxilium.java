package dev.ftb.mods.ftbauxilium;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.ftb.mods.ftbauxilium.auxilium.StatManager;
import dev.ftb.mods.ftbauxilium.screens.ScreenEvents;
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
        ClientGuiEvent.INIT_POST.register(ScreenEvents::loadOptOutButton);
    }

    public static void runTask(Runnable task) {
        executorService.submit(task);
    }
}