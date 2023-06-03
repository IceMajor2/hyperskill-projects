package advisor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            Http http = Http.getInstanceAndLaunch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        UserInterface userInterface = new UserInterface();
        userInterface.run();
    }
}
