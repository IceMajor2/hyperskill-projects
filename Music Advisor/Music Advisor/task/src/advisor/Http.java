package advisor;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Http {

    private static Http INSTANCE;
    private HttpClient client;
    private String AUTH_CODE;

    public static final String CLIENT_ID = "b43811db87904f6a99fc4dde9844d12c";
    public static final String REDIRECT_URI = "http://localhost:8080";
    public static final String SERVER_URI = Main.ACCESS_ARGUMENT;
    public static final String RESOURCE_URI = Main.RESOURCE_ARGUMENT;
    private final String GRANT_TYPE = "authorization_code";
    private final String CLIENT_SECRET = "89b2b199467f440db7b418efed9d5983";

    private Http() {
        this.client = HttpClient.newBuilder().build();
        this.AUTH_CODE = "";
    }

    public static Http getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new Http();
        }
        return INSTANCE;
    }

    private HttpServer initServer() throws IOException {
        return HttpServer.create(new InetSocketAddress(8080), 0);
    }

    public String listenForCodeAndShutDown() throws IOException, InterruptedException {
        AUTH_CODE = "";
        HttpServer server = initServer();

        server.createContext("/", exchange -> {
            String query = exchange.getRequestURI().getQuery();

            if (query != null && query.contains("code")) {
                AUTH_CODE = query.substring(5);
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
        while (AUTH_CODE.isEmpty()) {
            Thread.sleep(10);
        }
        // OAuth successful... stopping server
        server.stop(10);

        return AUTH_CODE;
    }

    public String accessTokenRequest() throws IOException, InterruptedException {
        // prepare request
        var request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(SERVER_URI + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=" + GRANT_TYPE +
                                "&code=" + AUTH_CODE +
                                "&redirect_uri=" + REDIRECT_URI +
                                "&client_id=" + CLIENT_ID +
                                "&client_secret=" + CLIENT_SECRET))
                .build();

        // send it
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        StringBuilder addScope = new StringBuilder(responseBody);
        return addScope.toString();
    }

    public void getFeatured() {
        var request = HttpRequest.newBuilder()
                .header("Application", "Bearer %s".formatted(AUTH_CODE))
                .uri(URI.create(RESOURCE_URI + "/v1/browse/featured-playlists"))
                .GET()
                .build();
    }
}
