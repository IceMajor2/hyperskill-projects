package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Http {

    private static Http INSTANCE;

    private static String AUTH_CODE;
    private final String _URI = "http://localhost:8080";

    private static HttpServer server;
    private static HttpClient client;

    private Http() throws IOException {
        this.AUTH_CODE = "";
        this.server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);

        this.client = HttpClient.newBuilder().build();
    }

    public static Http getInstanceAndLaunch() throws IOException {
        if(INSTANCE == null) {
            INSTANCE = new Http();
        }
        INSTANCE.launch();
        return INSTANCE;
    }

    private void launch() {
        getSpotifyCode();
        server.start();
    }

    public static void shutDown() {
        server.stop(1);
    }

    private void getSpotifyCode() {
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();

                if(query.contains("code")) {
                    AUTH_CODE = query.substring(5);
                    query = "Got the code. Return back to your program.";
                } else {
                    query = "Authorization code not found. Try again.";
                }

                exchange.sendResponseHeaders(401, query.length());
                exchange.getResponseBody().write(query.getBytes());
                exchange.getResponseBody().close();
            }
        });
    }

    public static boolean isAuthorized() {
        return !AUTH_CODE.isEmpty();
    }

//    Simple hello world request
//    public HttpResponse sendWelcomeRequest() throws InterruptedException, IOException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(_URI))
//                .GET()
//                .build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
//        return response;
//    }
}
