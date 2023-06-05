package advisor.views;

import advisor.Main;

import java.util.List;

public class Printer {

    private PrintingStrategy strategy;
    private int entriesPerPage;
    private int totalPages;

    public Printer(PrintingStrategy strategy, int entriesPerPage, int totalPages) {
        this.strategy = strategy;
        this.entriesPerPage = entriesPerPage;
        this.totalPages = totalPages;
    }

    public void print(List<?> list, int page) {
        this.strategy.print(list, page, totalPages);
    }
}

interface PrintingStrategy {

    void print(List<?> list, int page, int totalPages);
}

class CategoryPrintingStrategy implements PrintingStrategy {

    @Override
    public void print(List<?> list, int page, int totalPages) {
        for (Object cat : list) {
            System.out.println(cat);
        }
        System.out.println("---PAGE %d OF %d---".formatted(page, totalPages));
    }

}

class MusicPrintingStrategy implements PrintingStrategy {

    @Override
    public void print(List<?> list, int page, int totalPages) {
        for (Object musicObject : list) {
            System.out.println(musicObject);
            System.out.println();
        }
        System.out.println("---PAGE %d OF %d---".formatted(page, totalPages));
    }
}
