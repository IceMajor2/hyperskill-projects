package advisor;

public class Main {
    public static void main(String[] args) throws Exception {
        Http http = Http.getInstanceAndLaunch();
        http.makeHttpRequest();

        UserInterface userInterface = new UserInterface();
        userInterface.run();
    }
}
