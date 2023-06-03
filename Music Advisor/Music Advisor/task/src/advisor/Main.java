package advisor;

public class Main {

    public static String ACCESS_ARGUMENT;

    public static void main(String[] args) throws Exception {

        if(args.length == 0) {
            ACCESS_ARGUMENT = "https://accounts.spotify.com";
        } else {
            ACCESS_ARGUMENT = args[1];
        }

        UserInterface userInterface = new UserInterface();
        userInterface.run();
    }
}
