package advisor;

import advisor.models.Album;
import advisor.models.Category;
import advisor.models.Playlist;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class JsonService {

    public static List<Playlist> featuredJsonToModel(String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        JsonObject playlistsJson = jsonObject.getAsJsonObject("playlists");

        List<Playlist> playlists = new ArrayList<>();
        for (JsonElement playlistEl : playlistsJson.getAsJsonArray("items")) {
            JsonObject playlistObj = playlistEl.getAsJsonObject();

            String name = playlistObj.get("name").getAsString();
            if (name == null) {
                continue;
            }
            JsonObject linkObj = playlistObj.getAsJsonObject("external_urls");
            String link = linkObj.get("spotify").getAsString();

            Playlist playlist = new Playlist(name, link);
            playlists.add(playlist);
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

    public static List<Category> categoriesJsonToModel(String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        JsonObject categoriesObj = jsonObject.getAsJsonObject("categories");

        List<Category> categories = new ArrayList<>();
        for(JsonElement categoryEl : categoriesObj.getAsJsonArray("items")) {
            JsonObject categoryObj = categoryEl.getAsJsonObject();

            String id = categoryObj.get("id").getAsString();
            String name = categoryObj.get("name").getAsString();

            Category category = new Category(id, name);
            categories.add(category);
        }
        return categories;
    }

    public static String parseAccessToken(String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        String accessToken = jsonObject.get("access_token").getAsString();
        return accessToken;
    }
}
