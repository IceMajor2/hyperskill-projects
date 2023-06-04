package advisor;

import advisor.http.HttpController;
import advisor.http.HttpRequestService;
import advisor.models.Album;
import advisor.models.Playlist;

import java.io.IOException;
import java.util.Scanner;
import java.util.List;

public class UserInterface {

    private HttpController httpController;
    private Scanner scanner;
    private boolean logged;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.logged = false;
    }

    public void run() throws IOException, InterruptedException {
        one:
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
                continue one;
            }
            System.out.println("Please, provide access for application.");
        }
    }

    private void printFeatured() throws IOException, InterruptedException {
        List<Playlist> featuredPlaylists = httpController.getFeatured();
        for(Playlist playlist : featuredPlaylists) {
            System.out.println(playlist);
            System.out.println();
        }
    }

    private void printNew() throws IOException, InterruptedException {
        List<Album> newAlbums = httpController.getNew();
        for(Album album : newAlbums) {
            System.out.println(album);
            System.out.println();
        }
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

    private void loggedMenu(String input) throws IOException, InterruptedException {
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

    private void authUser() throws IOException, InterruptedException {
        this.httpController = HttpController.getInstance();

        System.out.println("use this link to request the access code:");
        System.out.println(
                HttpRequestService.SERVER_URI +
                        "/authorize?client_id=" + HttpRequestService.CLIENT_ID +
                        "&redirect_uri=" + HttpRequestService.REDIRECT_URI +
                        "&response_type=code");
        System.out.println("waiting for code...");

        httpController.getAuthenticationCode();

        System.out.println("code received");
        System.out.println("making http request for access_token...");
        System.out.println("response:");
        System.out.println(httpController.accessTokenRequest());
        System.out.println("---SUCCESS---");
        this.logged = true;
    }
}
