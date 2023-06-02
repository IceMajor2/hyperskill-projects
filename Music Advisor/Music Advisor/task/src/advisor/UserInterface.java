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
            if("new".equals(input)) {
                printNew();
                continue;
            }
            if("categories".equals(input)) {
                printCategories();
                continue;
            }
            if("playlists Mood".equals(input)) {
                System.out.println("---MOOD PLAYLISTS---");
                System.out.println("Walk Like A Badass");
                System.out.println("Rage Beats");
                System.out.println("Arab Mood Booster");
                System.out.println("Sunday Stroll");
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

    private void printNew() {
        System.out.println("---NEW RELEASES---");
        System.out.println("Mountains [Sia, Diplo, Labrinth]");
        System.out.println("Runaway [Lil Peep]");
        System.out.println("The Greatest Show [Panic! At The Disco]");
        System.out.println("All Out Life [Slipknot]");
    }

    private void printCategories() {
        System.out.println("---CATEGORIES---");
        System.out.println("Top Lists");
        System.out.println("Pop");
        System.out.println("Mood");
        System.out.println("Latin");
    }

    private void printExit() {
        System.out.println("---GOODBYE!---");
    }
}
