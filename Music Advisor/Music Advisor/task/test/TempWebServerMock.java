import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.WebPage;
import org.hyperskill.hstest.mocks.web.WebServerMock;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.request.HttpRequestParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TempWebServerMock extends WebServerMock {

    private static final String DELIM = "/";
    private static final String fictiveAccessToken = "456456";
    private static final String authorizationHeader = "Authorization";

//    public static void main(String[] args) { // for testing
//        org.hyperskill.hstest.mocks.web.WebServerMock ws = new org.hyperskill.hstest.mocks.web.WebServerMock(12345);
//        ws.start();
//        ws.run();
//    }

    private ServerSocket serverSocket;
    private final Map<String, String> pages = new HashMap<>();
    private final int port;

    private boolean isStarted = false;
    private boolean isStopped = false;

    private String access_token = "";

    public TempWebServerMock(int port) {
        super(port);
        this.port = port;
    }

    public TempWebServerMock setPage(String url, String content) {
        return setPage(url, new WebPage().setContent(content));
    }

    public TempWebServerMock setPage(String url, WebPage page) {
        if (!url.startsWith(DELIM)) {
            url = DELIM + url;
        }
        pages.put(url, page.contentWithHeader());
        return this;
    }

    public String getAccess_token() {
        return access_token;
    }

    private void checkRequestHeaders(HttpRequest request) throws WrongAnswer {
        Map<String,String> requestHeaders = request.getHeaders();

        if(requestHeaders.containsKey(authorizationHeader)) {
            if(requestHeaders.get(authorizationHeader).contains(fictiveAccessToken)) {
                access_token = requestHeaders.get(authorizationHeader);
            }
        }
    }

    private String resolveRequest(DataInputStream input) {
        HttpRequest request = HttpRequestParser.parse(input);
        checkRequestHeaders(request);
        return request != null ? request.getEndpoint() : null;
    }

    private void sendResponse(String path, DataOutputStream output) throws Exception {
        String response;
        if (path == null) {
            response = "Webpage not found!";
        } else {
            if (!path.startsWith(DELIM)) {
                path = DELIM + path;
            }
            response = pages.getOrDefault(path, "Webpage not found!");
        }
        for (char c : response.toCharArray()) {
            output.write(c);
        }
    }

    private void handle(Socket socket) throws Exception {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        sendResponse(resolveRequest(input), output);
        input.close();
        output.close();
        socket.close();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ignored) { }
    }

    public void run() {
        try {
            while (serverSocket != null && !serverSocket.isClosed()) {
                isStarted = true;
                isStopped = false;
                handle(serverSocket.accept());
            }
        } catch (Exception ignored) { }
        isStarted = false;
        isStopped = true;
    }

    public void stop() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ignored) { }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isStopped() {
        return isStopped;
    }
}
