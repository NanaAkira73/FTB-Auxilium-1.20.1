package dev.ftb.mods.ftbauxilium;

import java.nio.file.Path;

public class AuxiliumExpectPlatform {
    public static Path getConfigDirectory() {
        return net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get();
    }

    public static String getVersionArg() {
        return System.getProperty("minecraft.version", "unknown");
    }
}