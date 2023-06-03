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

    private final String _URI = "http://localhost:8080";

    private static Http INSTANCE;
    private HttpServer server;
    private HttpClient client;

    private Http() throws IOException {
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

    public void launch() {
        openDefaultContext();
        server.start();
    }

    public HttpResponse sendWelcomeRequest() throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(_URI))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response;
    }

    private void openDefaultContext() {
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String msg = "Hello world!";
                exchange.sendResponseHeaders(200, msg.length());
                exchange.getResponseBody().write(msg.getBytes());
                exchange.getResponseBody().close();
            }
        });
    }
}
