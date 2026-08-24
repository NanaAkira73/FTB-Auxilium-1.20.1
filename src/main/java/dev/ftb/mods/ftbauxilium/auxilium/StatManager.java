package dev.ftb.mods.ftbauxilium.auxilium;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import dev.ftb.mods.ftbauxilium.FTBAuxiliumConfig;
import dev.ftb.mods.ftbauxilium.tasks.LaunchTask;
import dev.ftb.mods.ftbauxilium.tasks.LevelExitTask;
import dev.ftb.mods.ftbauxilium.tasks.LevelLoadTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.io.File;
import java.util.*;

public class StatManager {
    public enum Launchers {
        FTB_APP("FTB App", "/version.json", null),
        CURSE("Curse", "/.curseclient", null),
        POLYMC("PolyMC", null, v -> v != null && v.contains("polymc")),
        MULTI("MultiMc", null, v -> v != null && v.contains("multimc")),
        GDLAUNCHER("GDLauncher", "/config.json", null),
        ATLAUNCHER("ATLauncher", "/instance.png", null),
        TLAUNCHER("TLancher", "/../../tlauncher_libraries", null),
        UNKNOWN("unknown", null, null);

        public final String name;
        public final String identifierPath;
        public final java.util.function.Function<String, Boolean> checker;

        Launchers(String name, String identifierPath, java.util.function.Function<String, Boolean> checker) {
            this.name = name;
            this.identifierPath = identifierPath;
            this.checker = checker;
        }

        public String getName() { return name; }
    }

    public final AnonymousIdentification IDENTIFIER = new AnonymousIdentification();
    public long instanceStartTime = System.currentTimeMillis();
    public long loadTimeInSeconds;
    public ModpackData packData;
    public String sessionToken;
    public SessionCollector session;
    public Launchers launcher = Launchers.UNKNOWN;

    public void init() {
        FTBAuxilium.LOGGER.debug("Starting stat system");
        packData = new ModpackData();

        ClientLifecycleEvent.CLIENT_SETUP.register(this::clientLoaded);
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(this::worldLoaded);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(this::onLevelExit);
    }

    private void clientLoaded(Minecraft mc) {
        FTBAuxilium.runTask(new LaunchTask(mc, launcher));
    }

    private void worldLoaded(LocalPlayer player) {
        if (IDENTIFIER.isOptedOut() || !FTBAuxiliumConfig.isEnabled()) return;
        Minecraft.getInstance().execute(() -> FTBAuxilium.runTask(new LevelLoadTask()));
    }

    private void onLevelExit(LocalPlayer player) {
        FTBAuxilium.runTask(new LevelExitTask());
    }

    public Launchers findLauncher() {
        File gameDir = Minecraft.getInstance().gameDirectory.getAbsoluteFile();
        for (Launchers l : Launchers.values()) {
            if (l.identifierPath != null && new File(gameDir, l.identifierPath).exists()) {
                FTBAuxilium.LOGGER.info("Launcher found: {}", l.getName());
                return l;
            }
            if (l.checker != null) {
                String versionArg = getVersionArg();
                if (l.checker.apply(versionArg)) {
                    FTBAuxilium.LOGGER.info("Launcher found: {}", l.getName());
                    return l;
                }
            }
        }
        return Launchers.UNKNOWN;
    }

    public String getVersionArg() {
        return System.getProperty("minecraft.version", "unknown");
    }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String t) { sessionToken = t; }
    public ModpackData getPackData() { return packData; }
    public long getLoadTimeInSeconds() { return loadTimeInSeconds; }
    public SessionCollector getSession() { return session; }
    public void setSession(SessionCollector s) { session = s; }
}