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
    private final String GRANT_TYPE = "authorization_code";
    private final String CLIENT_SECRET = "89b2b199467f440db7b418efed9d5983";
    private final String REDIRECT_URI = "http://localhost:8080";
    private final String SPOTIFY_URI = "https://accounts.spotify.com";


    private Http() throws IOException {
        this.client = HttpClient.newBuilder().build();
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
                AUTH_CODE = null;
                query = "Authorization code not found. Try again.";
            }

            exchange.sendResponseHeaders(200, query.length());
            exchange.getResponseBody().write(query.getBytes());
            exchange.getResponseBody().close();
        });
        server.start();
        // keeping server alive, while the OAuth awaits
        while (AUTH_CODE != null && AUTH_CODE.isEmpty()) {
            Thread.sleep(10);
        }
        // OAuth received... stopping server
        server.stop(10);

        return AUTH_CODE;
    }

    public String accessTokenRequest() throws IOException, InterruptedException {
        // prepare request
        var request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(SPOTIFY_URI + "/api/token"))
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
        return responseBody;
    }
}
