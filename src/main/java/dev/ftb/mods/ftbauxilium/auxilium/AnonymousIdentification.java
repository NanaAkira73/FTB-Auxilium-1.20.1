package dev.ftb.mods.ftbauxilium.auxilium;

import com.google.gson.*;
import dev.ftb.mods.ftbauxilium.FTBAuxilium;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.UUID;
import java.util.prefs.Preferences;

public class AnonymousIdentification {
    private static final Preferences prefs = Preferences.userRoot().node("dev/ftb/mod/ftbauxilium");
    private final Path dataPath;
    private final Path identityFile;
    private final Path optOutFlag;
    private UUID identifier;
    private boolean optedOut;

    public AnonymousIdentification() {
        String home = System.getProperty("user.home");
        dataPath = Paths.get(home, ".ftb", "mods", "ftbauxilium");
        identityFile = dataPath.resolve("identity.json");
        optOutFlag = dataPath.resolve("dont-track.flag");
        optedOut = Files.exists(optOutFlag);
    }

    public UUID getIdentifier() {
        if (identifier == null) {
            identifier = getOrCreate();
        }
        return identifier;
    }

    private UUID getOrCreate() {
        try {
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
            if (Files.exists(identityFile)) {
                JsonObject obj = JsonParser.parseReader(new FileReader(identityFile.toFile())).getAsJsonObject();
                if (obj.has("uuid")) {
                    return UUID.fromString(obj.get("uuid").getAsString());
                }
            }
        } catch (Exception e) {
            FTBAuxilium.LOGGER.debug("Failed to create required directories", e);
        }
        UUID uuid = UUID.randomUUID();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("uuid", uuid.toString());
            try (FileWriter writer = new FileWriter(identityFile.toFile())) {
                new Gson().toJson(obj, writer);
            }
        } catch (Exception e) {
            FTBAuxilium.LOGGER.debug("Failed to save identity", e);
        }
        return uuid;
    }

    public boolean isOptedOut() {
        return optedOut;
    }

    public void optOut() {
        optedOut = true;
        try {
            Files.write(optOutFlag, "You have been opted out of FTB auxilium. Remove this file if you would like to opt-in to the stats system."
                .getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Minecraft.getInstance().execute(() -> {
            FTBAuxilium.runTask(new dev.ftb.mods.ftbauxilium.tasks.OptTask(true, getIdentifier()));
        });
    }

    public void optIn() {
        optedOut = false;
        try {
            Files.deleteIfExists(optOutFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Minecraft.getInstance().execute(() -> {
            FTBAuxilium.runTask(new dev.ftb.mods.ftbauxilium.tasks.OptTask(false, getIdentifier()));
        });
    }
}