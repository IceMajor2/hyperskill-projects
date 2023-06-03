package advisor;

import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;
    private boolean logged;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.logged = false;
    }

    public void run() {
        while (true) {
            String input = scanner.nextLine();
            if ("auth".equals(input)) {
                authUser();
                continue;
            }
            if ("exit".equals(input)) {
                printExit();
                return;
            }
            while (logged) {
                loggedMenu(input);
                continue;
            }
            System.out.println("Please, provide access for application.");
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

    private void loggedMenu(String input) {
        if ("featured".equals(input)) {
            printFeatured();
            return;
        }
        if ("new".equals(input)) {
            printNew();
            return;
        }
        if ("categories".equals(input)) {
            printCategories();
            return;
        }
        if ("playlists Mood".equals(input)) {
            System.out.println("---MOOD PLAYLISTS---");
            System.out.println("Walk Like A Badass");
            System.out.println("Rage Beats");
            System.out.println("Arab Mood Booster");
            System.out.println("Sunday Stroll");
            return;
        }
    }


    private void authUser() {
        System.out.println(
                "https://accounts.spotify.com/" +
                        "authorize?client_id=b43811db87904f6a99fc4dde9844d12c" +
                        "&redirect_uri=http://localhost:8080" +
                        "&response_type=code");
        System.out.println("---SUCCESS---");
        this.logged = true;
    }
}
