package dev.ftb.mods.ftbauxilium.tasks;

import dev.ftb.mods.ftbauxilium.ApiManager;
import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import dev.ftb.mods.ftbauxilium.auxilium.*;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;

public class LaunchTask implements Runnable {
    private final SystemStats stats;
    private final StatManager.Launchers launcher;

    public LaunchTask(Minecraft mc, StatManager.Launchers launcher) {
        this.stats = new SystemStats(mc);
        this.launcher = launcher;
    }

    @Override
    public void run() {
        ModpackData packData = FTBAuxilium.STAT_MANAGER.getPackData();
        stats.packId = packData.version;
        stats.packVersionId = packData.version;
        packData.loadTime = FTBAuxilium.STAT_MANAGER.getLoadTimeInSeconds();
        packData.modLoader = ClientBrandRetriever.getClientModName();
        packData.launcher = launcher.getName();

        boolean successful = ApiManager.INSTANCE.systemStarted(stats);
        boolean modpackDataSuccess = ApiManager.INSTANCE.modpackData(packData);

        if (!successful || !modpackDataSuccess) {
            FTBAuxilium.LOGGER.debug("Failed to send initial system data");
        }
    }
}