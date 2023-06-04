package advisor;

import advisor.http.HttpController;
import advisor.http.HttpRequestService;
import advisor.models.Album;
import advisor.models.Category;
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
        for (Playlist playlist : featuredPlaylists) {
            System.out.println(playlist);
            System.out.println();
        }
    }

    private void printNew() throws IOException, InterruptedException {
        List<Album> newAlbums = httpController.getNew();
        for (Album album : newAlbums) {
            System.out.println(album);
            System.out.println();
        }
    }

    private void printCategories() throws IOException, InterruptedException {
        List<Category> categories = httpController.getCategories();
        for (Category category : categories) {
            System.out.println(category);
        }
    }

    private void printExit() {
        System.out.println("---GOODBYE!---");
    }

    private void printPlaylist(String category) throws IOException, InterruptedException {
        List<Playlist> playlists = httpController.getPlaylist(category);
        if(playlists.isEmpty()) {
            System.out.println("Unknown category name.");
            return;
        }
        for (Playlist playlist : playlists) {
            System.out.println(playlist);
            System.out.println();
        }
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
        if (input.contains("playlists")) {
            printPlaylist(this.getCategory(input));
            return;
        }
    }

    private String getCategory(String input) {
        return input.split(" ")[1];
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
