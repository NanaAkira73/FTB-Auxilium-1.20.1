package dev.ftb.mods.ftbauxilium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.*;

public class FTBAuxiliumConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ConfigData data = new ConfigData();

    public static class ConfigData {
        public boolean isEnabled = true;
    }

    public static void init() {
        load();
    }

    public static void load() {
        Path configFile = getConfigPath();
        if (Files.exists(configFile)) {
            try (Reader reader = new FileReader(configFile.toFile())) {
                data = GSON.fromJson(reader, ConfigData.class);
            } catch (Exception e) {
                FTBAuxilium.LOGGER.warn("Failed to load config: {}", e.getMessage());
            }
        }
        save();
    }

    public static void save() {
        Path configFile = getConfigPath();
        try {
            Files.createDirectories(configFile.getParent());
            try (Writer writer = new FileWriter(configFile.toFile())) {
                GSON.toJson(data, writer);
            }
        } catch (Exception e) {
            FTBAuxilium.LOGGER.warn("Failed to save config: {}", e.getMessage());
        }
    }

    private static Path getConfigPath() {
        return Paths.get("config", "ftbauxilium.json");
    }

    public static boolean isEnabled() {
        return data.isEnabled;
    }

    public static ConfigData get() {
        return data;
    }
}