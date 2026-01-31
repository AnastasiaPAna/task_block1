package org.example.series;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static boolean prettyMode = true;

    public static void main(String[] args) {

         // MODE 1: запуск з параметрами
        // args[0] = folderPath
        // args[1] = attribute (title/genre/seasons/rating/year/finished)
        // args[2] = output mode (optional): pretty/simple
        if (args.length >= 2) {
            String folderPath = args[0];
            String attribute = args[1];

            if (args.length >= 3) {
                applyModeArg(args[2]);
            }

            List<Series> seriesList = SeriesLoader.loadFromFolder(folderPath);

            System.out.println("=== Loaded series (" + seriesList.size() + ") | mode: "
                    + (prettyMode ? "PRETTY" : "SIMPLE") + " ===");
            printList(seriesList);

            try {
                var stats = StatisticsService.countByAttribute(seriesList, attribute);

                Path out = Path.of("statistics_by_" + attribute.toLowerCase() + ".xml");
                XmlStatisticsWriter.write(stats, attribute.toLowerCase(), out);

                System.out.println("Saved: " + out.toAbsolutePath());
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
            return;
        }


        List<String> files = List.of(
                "data/01_stranger_things.json",
                "data/02_wednesday.json",
                "data/03_vampire_diaries.json",
                "data/04_the_originals.json",
                "data/05_legacies.json",
                "data/06_shield.json",
                "data/07_good_doctor.json",
                "data/08_locke_and_key.json",
                "data/09_game_of_thrones.json"
        );

        List<Series> seriesList = SeriesLoader.loadAll(files);

        System.out.println("=== Series app started! Loaded: " + seriesList.size() + " ===");

        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                printMenu();
                System.out.print("Choose option: ");

                String input = sc.nextLine().trim();

                switch (input) {
                    case "1" -> {
                        System.out.println("\n=== All series | mode: " + (prettyMode ? "PRETTY" : "SIMPLE") + " ===");
                        printList(seriesList);
                    }
                    case "2" -> {
                        System.out.print("Enter min rating (example 8.5): ");
                        double minRating = readDouble(sc);

                        System.out.println("\n=== Rating >= " + minRating + " ===");
                        var filtered = SeriesService.filterByRating(seriesList, minRating);
                        printList(filtered);
                    }
                    case "3" -> {
                        System.out.print("Finished only? (true/false): ");
                        boolean finished = readBoolean(sc);

                        System.out.println("\n=== finished = " + finished + " ===");
                        var filtered = SeriesService.filterByFinished(seriesList, finished);
                        printList(filtered);
                    }
                    case "4" -> {
                        System.out.print("Enter N for TOP by rating: ");
                        int n = readInt(sc);

                        System.out.println("\n=== TOP " + n + " by rating ===");
                        var top = SeriesService.topNByRating(seriesList, n);
                        printList(top);
                    }
                    case "5" -> {
                        System.out.print("Search by title (part of name): ");
                        String q = sc.nextLine().trim();

                        System.out.println("\n=== Search: '" + q + "' ===");
                        SeriesService.findByTitleContains(seriesList, q)
                                .ifPresentOrElse(
                                        s -> {
                                            System.out.println("Found:");
                                            SeriesPrinter.printOne(s);
                                        },
                                        () -> System.out.println("Not found")
                                );
                    }
                    case "6" -> {
                        System.out.println("\n=== Sorted by rating (DESC) ===");
                        var sorted = SeriesService.sortByRatingDesc(seriesList);
                        printList(sorted);
                    }
                    case "7" -> {
                        System.out.println("\n=== Statistics ===");
                        double avg = SeriesService.averageRating(seriesList);
                        System.out.printf("Average rating = %.2f%n", avg);

                        SeriesService.maxSeasons(seriesList)
                                .ifPresent(s ->
                                        System.out.println("Most seasons: " + s.getTitle() +
                                                " (" + s.getSeasons() + ")"));
                    }

                    case "8" -> {
                        System.out.println("\n=== Export statistics to XML ===");
                        System.out.print("Enter attribute (title/genre/seasons/rating/year/finished): ");
                        String attr = sc.nextLine().trim();

                        try {
                            var stats = StatisticsService.countByAttribute(seriesList, attr);

                            stats.forEach((k, v) -> System.out.println(k + " = " + v));

                            var out = Path.of("statistics_by_" + attr.toLowerCase() + ".xml");
                            XmlStatisticsWriter.write(stats, attr.toLowerCase(), out);
                            System.out.println("Saved: " + out.toAbsolutePath());
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                    case "9" -> {
                        System.out.println("\nChoose display mode:");
                        System.out.println("1 - SIMPLE");
                        System.out.println("2 - PRETTY");
                        System.out.print("Enter 1 or 2: ");

                        String mode = sc.nextLine().trim();
                        if (mode.equals("1")) {
                            prettyMode = false;
                            System.out.println("Mode set to SIMPLE");
                        } else if (mode.equals("2")) {
                            prettyMode = true;
                            System.out.println("Mode set to PRETTY");
                        } else {
                            System.out.println("Invalid choice, mode unchanged");
                        }
                    }
                    case "0" -> {
                        System.out.println("Bye!");
                        running = false;
                    }
                    default -> System.out.println("Unknown option. Try again.");
                }

                System.out.println();
            }
        }
    }

    private static void printList(List<Series> list) {
        if (prettyMode) {
            SeriesPrinter.printShortList(list);
        } else {
            SeriesPrinter.printSimpleList(list);
        }
    }

    private static void applyModeArg(String modeArg) {
        if (modeArg == null) return;
        if (modeArg.equalsIgnoreCase("simple")) prettyMode = false;
        if (modeArg.equalsIgnoreCase("pretty")) prettyMode = true;
    }

    private static void printMenu() {
        System.out.println("""
                -------------------------
                1 - Show all series
                2 - Filter by min rating
                3 - Filter by finished (true/false)
                4 - TOP N by rating
                5 - Search by title
                6 - Sort by rating DESC
                7 - Statistics (avg + max seasons)
                8 - Export statistics to XML (choose attribute)
                9 - Change display mode (simple/pretty)
                0 - Exit
                -------------------------
                """);
    }

    private static int readInt(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.print("Please enter an integer: ");
            }
        }
    }

    private static double readDouble(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                System.out.print("Please enter a number (example 8.5): ");
            }
        }
    }

    private static boolean readBoolean(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("true")) return true;
            if (s.equals("false")) return false;
            System.out.print("Please enter true or false: ");
        }
    }
}
