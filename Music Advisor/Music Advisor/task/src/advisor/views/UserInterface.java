package advisor.views;

import advisor.Main;
import advisor.controllers.HttpController;
import advisor.services.HttpRequestService;
import advisor.models.Album;
import advisor.models.Category;
import advisor.models.Playlist;

import java.io.IOException;
import java.util.Scanner;
import java.util.List;

public class UserInterface {

    private HttpController httpController;
    private Scanner scanner;
    private int entriesPerPage;
    private boolean logged;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.logged = false;
        this.entriesPerPage = Main.PAGE_ARGUMENT;
    }

    //public void run() throws IOException, InterruptedException {
        //run("");
    //}

    public void run() throws IOException, InterruptedException {
        one:
        while (true) {
            String input = scanner.nextLine();
           // input = input.isEmpty() ? scanner.nextLine() : input;
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

    private void loggedMenu(String input) throws IOException, InterruptedException {
        if ("featured".equals(input)) {
            var featured = httpController.getFeatured();
            musicMenu(featured);
            return;
        }
        if ("new".equals(input)) {
            var newest = httpController.getNew();
            musicMenu(newest);
            return;
        }
        if ("categories".equals(input)) {
            var categories = httpController.getCategories();
            musicMenu(categories);
            return;
        }
        if (input.contains("playlists")) {
            var playlists = httpController.getPlaylist(this.getCategory(input));
            if(playlists.isEmpty()) {
                System.out.println("Unknown category name.");
                return;
            }
            musicMenu(playlists);
            return;
        }
    }

    private void musicMenu(List<?> list) throws IOException, InterruptedException {
        int currentPage = 1;
        printList(list, currentPage);

        int totalPages = getTotalPages(list);

        String input = "";
        while (true) {
            input = scanner.nextLine();
            if ("prev".equals(input)) {
                if (currentPage == 1) {
                    System.out.println("No more pages.");
                    continue;
                }
                currentPage--;
                printList(list, currentPage);
                continue;
            }
            if ("next".equals(input)) {
                if (currentPage == totalPages) {
                    System.out.println("No more pages.");
                    continue;
                }
                currentPage++;
                printList(list, currentPage);
                continue;
            }
            break;
        }
        //run(input);
    }

    private void printList(List<?> list, int page) {
        int startIndex = (page - 1) * entriesPerPage;
        for (int i = startIndex; i < startIndex + entriesPerPage; i++) {
            System.out.println(list.get(i));
            System.out.println();
        }
        System.out.println("---PAGE %d OF %d---"
                .formatted(page, this.getTotalPages(list)));
    }

    private void printExit() {
        System.out.println("---GOODBYE!---");
    }

    private String getCategory(String input) {
        return input.substring(10);
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

    private int getTotalPages(List<?> list) {
        int size = list.size();
        return size / entriesPerPage + (size % entriesPerPage == 0 ? 0 : 1);
    }
}