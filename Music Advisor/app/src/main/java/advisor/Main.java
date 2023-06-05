package advisor;

import advisor.views.UserInterface;

public class Main {

    public static String ACCESS_ARGUMENT;
    public static String RESOURCE_ARGUMENT;
    public static int PAGE_ARGUMENT;

    public static void main(String[] args) throws Exception {
        setArgs(args);
        UserInterface userInterface = new UserInterface();
        userInterface.run();
    }

    public static void setArgs(String[] args) {
		ACCESS_ARGUMENT = System.getProperty("access") == null ? "https://accounts.spotify.com" : System.getProperty("access");
		RESOURCE_ARGUMENT = System.getProperty("resource") == null ? "https://api.spotify.com" : System.getProperty("resource");
		PAGE_ARGUMENT = System.getProperty("page") == null ? 5 : Integer.valueOf(System.getProperty("page"));
    }
}
