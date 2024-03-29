package advisor.services;

import advisor.Main;
import advisor.models.Category;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestService {

    // singleton
    private static HttpRequestService INSTANCE;

    private String authCode;
    private String accessToken;

    // constants
    public static String CLIENT_ID;
    public static final String REDIRECT_URI = "http://localhost:8080";
    public static final String SERVER_URI = Main.ACCESS_ARGUMENT;
    private static final String RESOURCE_URI = Main.RESOURCE_ARGUMENT;
    private final String GRANT_TYPE = "authorization_code";
    private static String CLIENT_SECRET;

    private HttpRequestService() {
        this.authCode = "";
        this.accessToken = "";
    }

    public static HttpRequestService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpRequestService();
        }

        return INSTANCE;
    }

    public HttpRequest postAccessTokenRequest() {
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(SERVER_URI + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=" + GRANT_TYPE +
                                "&code=" + authCode +
                                "&redirect_uri=" + REDIRECT_URI +
                                "&client_id=" + CLIENT_ID +
                                "&client_secret=" + CLIENT_SECRET))
                .build();
    }

    public HttpRequest getFeaturedRequest() {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer %s".formatted(accessToken))
                .uri(URI.create(RESOURCE_URI + "/v1/browse/featured-playlists"))
                .GET()
                .build();
    }

    public HttpRequest getNewRequest() {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer %s".formatted(accessToken))
                .uri(URI.create(RESOURCE_URI + "/v1/browse/new-releases"))
                .GET()
                .build();
    }

    public HttpRequest getCategoriesRequest() {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer %s".formatted(accessToken))
                .uri(URI.create(RESOURCE_URI + "/v1/browse/categories"))
                .GET()
                .build();
    }

    public HttpRequest getPlaylistRequest(Category category) {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer %s".formatted(accessToken))
                .uri(URI.create(RESOURCE_URI + "/v1/browse/categories/%s/playlists"
                        .formatted(category.getId())))
                .GET()
                .build();
    }

    public String authenticationListener() throws IOException, InterruptedException {
        HttpServer server = initServer();

        server.createContext("/", exchange -> {
            String query = exchange.getRequestURI().getQuery();

            if (query != null && query.contains("code")) {
                authCode = query.substring(5);
                query = "Got the code. Return back to your program.";
            } else {
                query = "Authorization code not found. Try again.";
            }
            exchange.sendResponseHeaders(200, query.length());
            exchange.getResponseBody().write(query.getBytes());
            exchange.getResponseBody().close();
        });
        server.start();
        // keeping server alive, while the OAuth awaits
        while (authCode.isEmpty()) {
            Thread.sleep(10);
        }
        // OAuth successful... stopping server
        server.stop(10);
        return authCode;
    }

    private HttpServer initServer() throws IOException {
        return HttpServer.create(new InetSocketAddress(8080), 0);
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static void setClientId(String clientId) {
        CLIENT_ID = clientId;
    }

    public static void setClientSecret(String clientSecret) {
        CLIENT_SECRET = clientSecret;
    }
}
