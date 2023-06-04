package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonService {

    public static Map<String, String> featuredJsonToMap(String featuredBody) {
        JsonObject jsonObject = JsonParser.parseString(featuredBody).getAsJsonObject();
        JsonObject playlistsJson = jsonObject.getAsJsonObject("playlists");

        Map<String, String> playlists = new LinkedHashMap<>();
        for (JsonElement playlistEl : playlistsJson.getAsJsonArray("items")) {
            JsonObject playlistObj = playlistEl.getAsJsonObject();

            String name = playlistObj.get("name").getAsString();
            if (name == null) {
                continue;
            }
            JsonObject linkObj = playlistObj.getAsJsonObject("external_urls");
            String link = linkObj.get("spotify").getAsString();

            playlists.put(name, link);
        }
        return playlists;
    }

    public static String parseAccessToken(String jsonBody) {
        JsonObject jsonObject = JsonParser.parseString(jsonBody).getAsJsonObject();
        String accessToken = jsonObject.get("access_token").getAsString();
        return accessToken;
    }
}
