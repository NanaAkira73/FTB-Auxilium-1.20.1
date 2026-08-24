package dev.ftb.mods.ftbauxilium.tasks;

import dev.ftb.mods.ftbauxilium.ApiManager;
import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import dev.ftb.mods.ftbauxilium.auxilium.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;

public class LevelLoadTask implements Runnable {
    @Override
    public void run() {
        String sessionId = ApiManager.INSTANCE.startSession();
        if (sessionId != null) {
            FTBAuxilium.STAT_MANAGER.setSessionToken(sessionId);
        }

        Minecraft mc = Minecraft.getInstance();
        IntegratedServer server = mc.getSingleplayerServer();
        if (server == null) {
            FTBAuxilium.LOGGER.debug("Failed to send session data because no server was found?");
            return;
        }

        ModpackData packData = FTBAuxilium.STAT_MANAGER.getPackData();
        SessionCollector session = new SessionCollector(
            server.isPublished(),
            server.isHardcore(),
            server.isPublished(),
            sessionId,
            packData.version,
            packData.version
        );
        FTBAuxilium.STAT_MANAGER.setSession(session);
    }
}