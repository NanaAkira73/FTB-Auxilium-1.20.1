package dev.ftb.mods.ftbauxilium.tasks;

import dev.ftb.mods.ftbauxilium.ApiManager;
import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import dev.ftb.mods.ftbauxilium.auxilium.ModpackData;

import java.util.UUID;

public class OptTask implements Runnable {
    public static class JustPackData {
        public int packId;
        public int packVersion;
        public JustPackData(int packId, int packVersion) {
            this.packId = packId;
            this.packVersion = packVersion;
        }
    }

    private final boolean optOut;
    private final UUID identifier;
    private JustPackData data;

    public OptTask(boolean optOut, UUID identifier) {
        this.optOut = optOut;
        this.identifier = identifier;
    }

    @Override
    public void run() {
        ModpackData packData = FTBAuxilium.STAT_MANAGER.getPackData();
        data = new JustPackData(packData.version, packData.version);
        boolean successful;
        if (optOut) {
            successful = ApiManager.INSTANCE.optOut(identifier, data);
        } else {
            successful = ApiManager.INSTANCE.optIn(identifier, data);
        }
        if (!successful) {
            FTBAuxilium.LOGGER.debug("Failed to send initial system data");
        }
    }
}