package advisor;

import advisor.views.UserInterface;

public class Main {

    public static String ACCESS_ARGUMENT;
    public static String RESOURCE_ARGUMENT;

    public static void main(String[] args) throws Exception {
        setArgs(args);
        UserInterface userInterface = new UserInterface();
        userInterface.run();
    }

    public static void setArgs(String[] args) {
        try {
            ACCESS_ARGUMENT = args[1];
        } catch (Exception e) {
            ACCESS_ARGUMENT = "https://accounts.spotify.com";
        }
        try {
            RESOURCE_ARGUMENT = args[3];
        } catch (Exception e) {
            RESOURCE_ARGUMENT = "https://api.spotify.com";
        }
    }
}

/**
 * TODO: secure SPOTIFY's client ID (+ secret)
 */
