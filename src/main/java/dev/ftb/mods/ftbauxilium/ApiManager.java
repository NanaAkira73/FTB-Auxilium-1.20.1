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

    public static class CrashWrapper {
        String crashData;
        String packId;
        String packVersion;

        public CrashWrapper(String crashData) {
            this.crashData = crashData;
            ModpackData packData = FTBAuxilium.STAT_MANAGER.getPackData();
            this.packId = String.valueOf(packData.version);
            this.packVersion = packData.versionId;
        }
    }

    public static class PostRequest {
        private final String endpoint;
        private final Object body;
        private final UUID identifier;
        private boolean wasSuccessful;
        private JsonObject response;

        public PostRequest(String endpoint, Object body) {
            this(endpoint, body, null);
        }

        public PostRequest(String endpoint, Object body, UUID identifier) {
            this.endpoint = endpoint;
            this.body = body;
            this.identifier = identifier;
        }

        public JsonObject post() {
            try {
                String url = BASE_URL + endpoint;
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestProperty("Content-Type", "application/json");
                if (identifier != null) {
                    conn.setRequestProperty("Authorization", "Bearer " + identifier.toString());
                }
                conn.setRequestProperty("API-VERSION", CLIENT_API_VERSION);
                conn.setRequestProperty("User-Agent", "FTB Auxilium Client (version: 1.0.0)");

                String json = new Gson().toJson(body != null ? body : new JsonObject());
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(json.getBytes(StandardCharsets.UTF_8));
                }

                int status = conn.getResponseCode();
                if (status >= 200 && status < 300) {
                    try (InputStream is = conn.getInputStream()) {
                        String resp = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        response = JsonParser.parseString(resp).getAsJsonObject();
                        wasSuccessful = response.has("success") && response.get("success").getAsBoolean();
                        LOGGER.debug("Successful post response from {}", endpoint);
                    }
                } else {
                    LOGGER.debug("Request rejected from server at {}", url);
                }
            } catch (JsonParseException e) {
                LOGGER.debug("Failed to parse json response from {}", endpoint);
            } catch (IOException e) {
                LOGGER.debug("Failed to make request to {} due to {}", endpoint, e.getMessage());
            }
            return response;
        }
    }

    public boolean systemStarted(SystemStats stats) {
        PostRequest request = new PostRequest("system", stats);
        JsonObject response = request.post();
        if (response != null && request.wasSuccessful) {
            LOGGER.debug("System info successfully posted to the api");
            return true;
        }
        LOGGER.debug("Failed to send system info to the api");
        return false;
    }

    public boolean modpackData(ModpackData data) {
        PostRequest request = new PostRequest("modpack", data);
        JsonObject response = request.post();
        if (response != null && request.wasSuccessful) {
            LOGGER.debug("Mod pack data successfully posted to the api");
            return true;
        }
        LOGGER.debug("Failed to send mod pack data");
        return false;
    }

    public String startSession() {
        PostRequest request = new PostRequest("session/start", null);
        JsonObject response = request.post();
        if (response != null && response.has("sessionId")) {
            LOGGER.debug("API Session has been started");
            return response.get("sessionId").getAsString();
        }
        LOGGER.debug("Failed to fetch a valid session");
        return null;
    }

    public void endSession(SessionCollector session) {
        PostRequest request = new PostRequest("session/stop", session);
        JsonObject response = request.post();
        if (response != null && request.wasSuccessful) {
            LOGGER.debug("API Session has been ended");
        } else {
            LOGGER.debug("Failed to send session data");
        }
    }

    public void sendCrashReport(String report) {
        new PostRequest("crashes", new CrashWrapper(report)).post();
    }

    public boolean optOut(UUID identifier, OptTask.JustPackData packData) {
        PostRequest request = new PostRequest("opt-out", packData, identifier);
        return request.post() != null && request.wasSuccessful;
    }

    public boolean optIn(UUID identifier, OptTask.JustPackData packData) {
        PostRequest request = new PostRequest("opt-in", packData, identifier);
        return request.post() != null && request.wasSuccessful;
    }
}