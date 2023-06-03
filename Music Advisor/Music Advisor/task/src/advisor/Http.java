package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Http {

    private HttpServer httpServer;

    public Http() throws IOException {
        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
    }

    public static Http getInstanceAndLaunch() throws IOException {
        Http http = new Http();
        http.launch();
        return http;
    }

    public void launch() {
        openDefaultContext();
        httpServer.start();
    }

    public void openDefaultContext() {
        httpServer.createContext("/", new HttpHandler() {
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
