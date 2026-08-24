package dev.ftb.mods.ftbauxilium.auxilium;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBrandRetriever;

import java.util.*;

public class SystemStats {
    public String timeZone;
    public String javaVersion;
    public String javaVendor;
    public long memoryTotal;
    public long memoryMax;
    public int cpuCores;
    public String operatingSystemArch;
    public String operatingSystem;
    public String version;
    public String launchVersion;
    public String clientBrand;
    public int guiScale;
    public String resourcePacks;
    public int refreshRate;
    public boolean vsync;
    public boolean fullscreen;
    public String lang;
    public int packId;
    public int packVersionId;

    public SystemStats(Minecraft mc) {
        Runtime runtime = Runtime.getRuntime();
        javaVersion = System.getProperty("java.version");
        javaVendor = System.getProperty("java.vendor");
        memoryTotal = runtime.totalMemory();
        memoryMax = runtime.maxMemory();
        cpuCores = runtime.availableProcessors();
        operatingSystemArch = System.getProperty("os.arch");
        operatingSystem = Util.getPlatform().toString();
        timeZone = System.getProperty("user.timezone", "unknown");
        version = SharedConstants.getCurrentVersion().getName();
        launchVersion = mc.getLaunchedVersion();
        clientBrand = ClientBrandRetriever.getClientModName();

        Options options = mc.options;
        guiScale = options.guiScale().get();
        fullscreen = options.fullscreen().get();
        vsync = options.enableVsync().get();

        lang = mc.getLanguageManager().getSelected();

        PackRepository packRepo = mc.getResourcePackRepository();
        StringBuilder packs = new StringBuilder();
        for (Pack pack : packRepo.getSelectedPacks()) {
            if (packs.length() > 0) packs.append(",");
            packs.append(pack.getId());
        }
        resourcePacks = packs.toString();

        refreshRate = mc.getWindow().getRefreshRate();
    }

    @Override
    public String toString() {
        return "SystemStats{" +
            "javaVersion='" + javaVersion + ''' +
            ", cpuCores=" + cpuCores +
            ", memoryMax=" + memoryMax +
            ", operatingSystem='" + operatingSystem + ''' +
            '}';
    }
}