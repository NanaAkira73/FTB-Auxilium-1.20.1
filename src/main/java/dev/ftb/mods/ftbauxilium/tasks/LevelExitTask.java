package dev.ftb.mods.ftbauxilium.tasks;

import dev.ftb.mods.ftbauxilium.ApiManager;
import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import dev.ftb.mods.ftbauxilium.auxilium.SessionCollector;

public class LevelExitTask implements Runnable {
    @Override
    public void run() {
        String sessionToken = FTBAuxilium.STAT_MANAGER.getSessionToken();
        SessionCollector session = FTBAuxilium.STAT_MANAGER.getSession();
        if (sessionToken == null || session == null) {
            FTBAuxilium.LOGGER.debug("Failed to send session data as session was not active / setup");
            return;
        }
        ApiManager.INSTANCE.endSession(session);
    }
}