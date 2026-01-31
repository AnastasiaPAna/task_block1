package org.example.series;

import java.util.List;

public class SeriesPrinter {

    public static void printShortList(List<Series> list) {
        if (list.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        int i = 1;
        for (Series s : list) {
            System.out.printf("%d) %s | seasons: %d | rating: %.1f | finished: %s%n",
                    i++, s.getTitle(), s.getSeasons(), s.getRating(), s.isFinished());
        }
    }

    public static void printSimpleList(List<Series> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        for (Series s : list) {
            System.out.println("- " + s.getTitle() + " (" + s.getRating() + ")");
        }
    }

    public static void printOne(Series s) {
        if (s == null) {
            System.out.println("(null)");
            return;
        }
        System.out.println("Title    : " + s.getTitle());
        System.out.println("Genre    : " + s.getGenre());
        System.out.println("Seasons  : " + s.getSeasons());
        System.out.println("Rating   : " + s.getRating());
        System.out.println("Year     : " + s.getYear());
        System.out.println("Finished : " + s.isFinished());
    }
}
