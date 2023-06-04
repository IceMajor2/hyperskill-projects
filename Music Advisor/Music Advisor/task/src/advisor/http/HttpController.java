package advisor.http;

import advisor.repositories.CategoriesRepository;
import advisor.JsonService;
import advisor.models.Album;
import advisor.models.Category;
import advisor.models.Playlist;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.*;

public class HttpController {

    // singleton
    private static HttpController INSTANCE;

    private HttpClient client;
    private HttpRequestService httpRequestService;
    private CategoriesRepository categoriesRepository;

    private HttpController() {
        this.client = HttpClient.newBuilder().build();
        this.httpRequestService = HttpRequestService.getInstance();
        this.categoriesRepository = new CategoriesRepository();
    }

    public static HttpController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpController();
        }
        return INSTANCE;
    }

    public String getAuthenticationCode() throws IOException, InterruptedException {
        return httpRequestService.authenticationListener();
    }

    public String accessTokenRequest() throws IOException, InterruptedException {
        var request = httpRequestService.postAccessTokenRequest();
        String responseJsonBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        String accessToken = JsonService.parseAccessToken(responseJsonBody);
        httpRequestService.setAccessToken(accessToken);
        return responseJsonBody;
    }

    public List<Album> getNew() throws IOException, InterruptedException {
        var request = httpRequestService.getNewRequest();
        String responseJsonBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        var albums = JsonService.newJsonToModel(responseJsonBody);
        return albums;
    }

    public List<Playlist> getFeatured() throws IOException, InterruptedException {
        var request = httpRequestService.getFeaturedRequest();
        String responseJsonBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        var playlists = JsonService.playlistsJsonToModel(responseJsonBody);
        return playlists;
    }

    public List<Category> getCategories() throws IOException, InterruptedException {
        var request = httpRequestService.getCategoriesRequest();
        String responseJsonBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        var categories = JsonService.categoriesJsonToModel(responseJsonBody);
        categoriesRepository.put(categories);
        return categories;
    }

    public List<Playlist> getPlaylist(Category category) throws IOException, InterruptedException {
        var request = httpRequestService.getPlaylistRequest(category);
        String responseJsonBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        var playlists = JsonService.playlistsJsonToModel(responseJsonBody);
        return playlists;
    }
}
