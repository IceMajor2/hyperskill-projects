package tracker;

public class Main {

    public static Students students;

    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        UserInterface UI = new UserInterface();
        UI.run();
    }
}
