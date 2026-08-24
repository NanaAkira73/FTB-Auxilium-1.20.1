package dev.ftb.mods.ftbauxilium.auxilium;

import com.google.gson.*;
import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.*;

public class ModpackData {
    public String name = "unknown";
    public String type = "vanilla(bugged)";
    public int version = 0;
    public String versionId = "unknown";
    public String versionType = "UNKNOWN";
    public String modLoader = "forge";
    public long loadTime = 0;
    public String launcher = "UNKNOWN";

    public ModpackData() {
        Path configDir = Paths.get("config");
        Path metadataFile = configDir.resolve("metadata.json");
        try {
            if (Files.exists(metadataFile)) {
                JsonObject obj = JsonParser.parseReader(new FileReader(metadataFile.toFile())).getAsJsonObject();
                if (obj.has("name")) name = obj.get("name").getAsString();
                if (obj.has("type")) type = obj.get("type").getAsString();
                if (obj.has("version")) version = obj.get("version").getAsInt();
                if (obj.has("versionId")) versionId = obj.get("versionId").getAsString();
                FTBAuxilium.LOGGER.debug("Found packdata from metadata.json [data] -> {}", this);
            }
        } catch (FileNotFoundException e) {
            FTBAuxilium.LOGGER.debug("Failed to find metadata.json for pack data");
        } catch (Exception e) {
            FTBAuxilium.LOGGER.debug("Failed to parse metadata.json", e);
        }

        // Fallback to modpack.json
        Path gameDir = Minecraft.getInstance().gameDirectory.toPath();
        Path modpackFile = gameDir.resolve("modpack.json");
        try {
            if (Files.exists(modpackFile)) {
                JsonObject obj = JsonParser.parseReader(new FileReader(modpackFile.toFile())).getAsJsonObject();
                if (obj.has("name")) name = obj.get("name").getAsString();
                if (obj.has("versionId")) versionId = obj.get("versionId").getAsString();
                FTBAuxilium.LOGGER.debug("Fallback to launcher jsons [data] -> {}", this);
            }
        } catch (Exception ignored) {}

        Path versionFile = gameDir.resolve("version.json");
        try {
            if (Files.exists(versionFile)) {
                JsonObject obj = JsonParser.parseReader(new FileReader(versionFile.toFile())).getAsJsonObject();
                if (obj.has("id")) versionId = obj.get("id").getAsString();
                FTBAuxilium.LOGGER.debug("Fallback to launcher jsons [data] -> {}", this);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public String toString() {
        return "ModpackData{name='" + name + "', type='" + type + "', version=" + version + "}";
    }
}