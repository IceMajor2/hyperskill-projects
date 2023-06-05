package advisor.views;

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
        this.strategy.print(list, page, entriesPerPage, totalPages);
    }
}

interface PrintingStrategy {

    void print(List<?> list, int page, int entriesPerPage, int totalPages);
}

class CategoryPrintingStrategy implements PrintingStrategy {

    @Override
    public void print(List<?> list, int page, int entriesPerPage, int totalPages) {
        int startIndex = (page - 1) * entriesPerPage;
        for(int i = startIndex; i < startIndex + entriesPerPage; i++) {
            System.out.println(list.get(i));
        }
        System.out.println();
        System.out.println("---PAGE %d OF %d---".formatted(page, totalPages));
    }

}

class MusicPrintingStrategy implements PrintingStrategy {

    @Override
    public void print(List<?> list, int page, int entriesPerPage, int totalPages) {
        int startIndex = (page - 1) * entriesPerPage;
        for(int i = startIndex; i < startIndex + entriesPerPage; i++) {
            System.out.println(list.get(i));
            System.out.println();
        }
        System.out.println("---PAGE %d OF %d---".formatted(page, totalPages));
    }
}
