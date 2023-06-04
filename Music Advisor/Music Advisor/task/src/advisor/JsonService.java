package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonService {

    public static Map<String, String> featuredJsonToMap(String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
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

    public static List<Album> newJsonToModel(String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        JsonObject albumsJson = jsonObject.getAsJsonObject("albums");

        List<Album> albums = new ArrayList<>();
        for(JsonElement albumEl : albumsJson.getAsJsonArray("items")) {
            JsonObject albumObj = albumEl.getAsJsonObject();

            JsonObject linkObj = albumObj.getAsJsonObject("external_urls");
            String link = linkObj.get("spotify").getAsString();
            String name = albumObj.get("name").getAsString();

            List<String> artists = new ArrayList<>();
            for(JsonElement artistEl : albumObj.getAsJsonArray("artists")) {
                JsonObject artistObj = artistEl.getAsJsonObject();

                String artist = artistObj.get("name").getAsString();
                artists.add(artist);
            }

            Album album = new Album(name, artists, link);
            albums.add(album);
        }
        return albums;
    }

    public static String parseAccessToken(String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        String accessToken = jsonObject.get("access_token").getAsString();
        return accessToken;
    }
}
