package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.*;

public class HttpController {

    // singleton
    private static HttpController INSTANCE;

    private HttpClient client;
    private HttpRequestService httpRequestService;

    private HttpController() {
        this.client = HttpClient.newBuilder().build();
        this.httpRequestService = HttpRequestService.getInstance();
    }

    public static HttpController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpController();
        }
        return INSTANCE;
    }

    public String listenForCodeAndShutDown() throws IOException, InterruptedException {
        return httpRequestService.authenticationListener();
    }

    public String accessTokenRequest() throws IOException, InterruptedException {
        var request = httpRequestService.postAccessTokenRequest();
        String responseJsonBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        String accessToken = parseAccessToken(responseJsonBody);
        httpRequestService.setAccessToken(accessToken);
        return responseJsonBody;
    }

    public Map<String, String> getFeatured() throws IOException, InterruptedException {
        var request = httpRequestService.getFeaturedRequest();
        String responseJsonBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        var playlists = featuredJsonToMap(responseJsonBody);
        return playlists;
    }

    private Map<String, String> featuredJsonToMap(String featuredBody) {
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

    private String parseAccessToken(String jsonBody) {
        JsonObject jsonObject = JsonParser.parseString(jsonBody).getAsJsonObject();
        String accessToken = jsonObject.get("access_token").getAsString();
        return accessToken;
    }
}
