package advisor;

import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while(true) {
            String input = scanner.nextLine();
            if("featured".equals(input)) {
                printFeatured();
                continue;
            }
            if("exit".equals(input)) {
                printExit();
                break;
            }
        }
    }

    private void printFeatured() {
        System.out.println("---FEATURED---");
        System.out.println("Mellow Morning");
        System.out.println("Wake Up and Smell the Coffee");
        System.out.println("Monday Motivation");
        System.out.println("Songs to Sing in the Shower");
    }

    private void printExit() {
        System.out.println("---GOODBYE!---");
    }
}
