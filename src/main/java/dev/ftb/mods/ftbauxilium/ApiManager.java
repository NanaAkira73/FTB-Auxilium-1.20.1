package dev.ftb.mods.ftbauxilium;

import com.google.gson.*;
import dev.ftb.mods.ftbauxilium.auxilium.*;
import dev.ftb.mods.ftbauxilium.tasks.OptTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ApiManager {
    private static final String BASE_URL = "https://auxilium.ftb.team/";
    private static final String CLIENT_API_VERSION = "1.0.0";
    public static final ApiManager INSTANCE = new ApiManager();
    private static final Logger LOGGER = LogManager.getLogger("FTB Auxilium API");

    public boolean systemStarted(SystemStats stats) {
        JsonObject response = post("system", stats);
        if (response != null && response.get("success").getAsBoolean()) {
            LOGGER.debug("System info successfully posted to the api");
            return true;
        }
        LOGGER.debug("Failed to send system info to the api");
        return false;
    }

    public boolean modpackData(ModpackData data) {
        JsonObject response = post("modpack", data);
        if (response != null && response.get("success").getAsBoolean()) {
            LOGGER.debug("Mod pack data successfully posted to the api");
            return true;
        }
        LOGGER.debug("Failed to send mod pack data");
        return false;
    }

    public String startSession() {
        JsonObject response = post("session/start", null);
        if (response != null && response.get("success").getAsBoolean()) {
            String sessionId = response.get("sessionId").getAsString();
            LOGGER.debug("API Session has been started");
            return sessionId;
        }
        LOGGER.debug("Failed to fetch a valid session");
        return null;
    }

    public void endSession(SessionCollector session) {
        JsonObject response = post("session/stop", session);
        if (response != null && response.get("success").getAsBoolean()) {
            LOGGER.debug("API Session has been ended");
        } else {
            LOGGER.debug("Failed to send session data");
        }
    }

    public void sendCrashReport(String report) {
        post("crashes", report);
    }

    public boolean optOut(UUID identifier, OptTask.JustPackData packData) {
        return post("opt-out", packData, identifier) != null;
    }

    public boolean optIn(UUID identifier, OptTask.JustPackData packData) {
        return post("opt-in", packData, identifier) != null;
    }

    private JsonObject post(String endpoint, Object body) {
        return post(endpoint, body, null);
    }

    private JsonObject post(String endpoint, Object body, UUID identifier) {
        try {
            String url = BASE_URL + endpoint;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("API-VERSION", CLIENT_API_VERSION);
            conn.setRequestProperty("User-Agent", "FTB Auxilium Client (version: 1.0.0)");
            if (identifier != null) {
                conn.setRequestProperty("Authorization", "Bearer " + identifier.toString());
            }

            String json = new Gson().toJson(body != null ? body : new JsonObject());
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            if (status >= 200 && status < 300) {
                try (InputStream is = conn.getInputStream()) {
                    String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    return JsonParser.parseString(response).getAsJsonObject();
                }
            } else {
                LOGGER.debug("Request rejected from server at {}", url);
            }
        } catch (Exception e) {
            LOGGER.debug("Failed to make request to {} due to {}", endpoint, e.getMessage());
        }
        return null;
    }
}