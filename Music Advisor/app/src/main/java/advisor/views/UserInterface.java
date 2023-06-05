package advisor.views;

import advisor.Main;
import advisor.controllers.HttpController;
import advisor.services.HttpRequestService;

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

    public void run() throws IOException, InterruptedException {
        run("");
    }

    public void run(String input) throws IOException, InterruptedException {
        one:
        while (true) {
            input = input.isEmpty() ? scanner.nextLine() : input;
            if ("auth".equals(input)) {
                authUser();
                input = "";
                continue;
            }
            if ("exit".equals(input)) {
                printExit();
                System.exit(0);
            }
            while (logged) {
                loggedMenu(input);
                input = "";
                continue one;
            }
            input = "";
            System.out.println("Please, provide access for application.");
        }
    }

    private void loggedMenu(String input) throws IOException, InterruptedException {
        Printer printer = null;
        if ("featured".equals(input)) {
            var featured = httpController.getFeatured();
            printer = new Printer(new MusicPrintingStrategy(), this.entriesPerPage, getTotalPages(featured));
            musicMenu(printer, featured);
            return;
        }
        if ("new".equals(input)) {
            var newest = httpController.getNew();
            printer = new Printer(new MusicPrintingStrategy(), this.entriesPerPage, getTotalPages(newest));
            musicMenu(printer, newest);
            return;
        }
        if ("categories".equals(input)) {
            var categories = httpController.getCategories();
            printer = new Printer(new CategoryPrintingStrategy(), this.entriesPerPage, getTotalPages(categories));
            musicMenu(printer, categories);
            return;
        }
        if (input.contains("playlists")) {
            var playlists = httpController.getPlaylist(this.getCategory(input));
            if (playlists.isEmpty()) {
                System.out.println("Unknown category name.");
                return;
            }
            printer = new Printer(new MusicPrintingStrategy(), this.entriesPerPage, getTotalPages(playlists));
            musicMenu(printer, playlists);
            return;
        }
    }

    private void musicMenu(Printer printer, List<?> list) throws IOException, InterruptedException {
        int currentPage = 1;
        printer.print(list, currentPage);

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
                printer.print(list, currentPage);
                continue;
            }
            if ("next".equals(input)) {
                if (currentPage == totalPages) {
                    System.out.println("No more pages.");
                    continue;
                }
                currentPage++;
                printer.print(list, currentPage);
                continue;
            }
            if("exit".equals(input)) {
                return;
            }
            break;
        }
        run(input);
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

    private void printExit() {
        System.out.println("---GOODBYE!---");
    }

    // util methods
    private String getCategory(String input) {
        return input.substring(10);
    }

    private int getTotalPages(List<?> list) {
        int size = list.size();
        return size / entriesPerPage + (size % entriesPerPage == 0 ? 0 : 1);
    }
}
