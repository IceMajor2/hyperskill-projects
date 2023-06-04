package advisor;

public class Main {

    public static String ACCESS_ARGUMENT;
    public static String RESOURCE_ARGUMENT;

    public static void main(String[] args) throws Exception {

        if(args.length == 0) {
            ACCESS_ARGUMENT = "https://accounts.spotify.com";
        } else {
            ACCESS_ARGUMENT = args[1];
        }

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
