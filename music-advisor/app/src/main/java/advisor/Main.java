package advisor;

import advisor.services.HttpRequestService;
import advisor.views.UserInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static String ACCESS_ARGUMENT;
    public static String RESOURCE_ARGUMENT;
    public static int PAGE_ARGUMENT;

    public static void main(String[] args) throws Exception {
        setArgs(args);
        loadSpotifyCredentials();
        UserInterface userInterface = new UserInterface();
        userInterface.run();
    }

    public static void setArgs(String[] args) {
        ACCESS_ARGUMENT = System.getProperty("access") == null ? "https://accounts.spotify.com" : System.getProperty("access");
        RESOURCE_ARGUMENT = System.getProperty("resource") == null ? "https://api.spotify.com" : System.getProperty("resource");
        PAGE_ARGUMENT = System.getProperty("page") == null ? 5 : Integer.valueOf(System.getProperty("page"));
    }

    private static void loadSpotifyCredentials() {
        Properties properties = new Properties();
        InputStream is = Main.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HttpRequestService.setClientId(properties.getProperty("spotify.credentials.client-id"));
        HttpRequestService.setClientSecret(properties.getProperty("spotify.credentials.client-secret"));
    }
}
