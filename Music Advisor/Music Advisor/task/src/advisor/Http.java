package advisor;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;

public class Http {

    private static Http INSTANCE;

    private String AUTH_CODE;
    private final String _URI = "http://localhost:8080";

    private HttpClient client;

    private Http() throws IOException {
        this.client = HttpClient.newBuilder().build();
    }

    public static Http getInstance() throws IOException {
        if(INSTANCE == null) {
            INSTANCE = new Http();
        }
        return INSTANCE;
    }

    private HttpServer initServer() throws IOException {
        return HttpServer.create(new InetSocketAddress(8080), 0);
    }

    public void listenForCodeAndShutDown() throws IOException, InterruptedException {
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
    }
}
