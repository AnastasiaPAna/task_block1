package org.example.series.console;

import org.example.series.core.export.XmlStatisticsWriter;
import org.example.series.core.loader.SeriesLoader;
import org.example.series.core.model.Series;
import org.example.series.core.service.SeriesService;
import org.example.series.core.service.StatisticsService;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for interactive and parameter-based execution.
 *
 * Supports two modes:
 *
 * 1) Argument mode:
 *    args[0] = folder path
 *    args[1] = attribute for statistics
 *    args[2] = output mode (optional): pretty/simple
 *
 * 2) Interactive mode:
 *    Loads predefined data and provides CLI menu.
 *
 * This class acts as presentation layer for console usage.
 */
public class ConsoleRunner {

    /** Controls output formatting mode */
    private static boolean prettyMode = true;

    /**
     * Entry method for console execution.
     *
     * @param args command-line arguments
     */
    public static void run(String[] args) {

        // =========================
        // MODE 1: Parameter-based execution
        // =========================
        if (args.length >= 2) {

            String folderPath = args[0];
            String attribute = args[1];

            // Optional formatting argument
            if (args.length >= 3) {
                applyModeArg(args[2]);
            }

            // Load all series from provided folder
            List<Series> seriesList = SeriesLoader.loadFromFolder(folderPath);

            System.out.println("=== Loaded series (" + seriesList.size()
                    + ") | mode: "
                    + (prettyMode ? "PRETTY" : "SIMPLE") + " ===");

            printList(seriesList);

            try {
                // Generate statistics
                var stats = StatisticsService.countByAttribute(seriesList, attribute);

                // Export statistics to XML
                Path out = Path.of("statistics_by_" + attribute.toLowerCase() + ".xml");
                XmlStatisticsWriter.write(stats, attribute.toLowerCase(), out);

                System.out.println("Saved: " + out.toAbsolutePath());

            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }

            return;
        }

        // =========================
        // MODE 2: Interactive menu
        // =========================

        // Predefined dataset
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

        System.out.println("=== Series app started! Loaded: "
                + seriesList.size() + " ===");

        try (Scanner sc = new Scanner(System.in)) {

            boolean running = true;

            while (running) {

                printMenu();
                System.out.print("Choose option: ");
                String input = sc.nextLine().trim();

                switch (input) {

                    case "1" -> {
                        // Show all series
                        System.out.println("\n=== All series | mode: "
                                + (prettyMode ? "PRETTY" : "SIMPLE") + " ===");
                        printList(seriesList);
                    }

                    case "2" -> {
                        // Filter by minimum rating
                        System.out.print("Enter min rating (example 8.5): ");
                        double minRating = readDouble(sc);

                        var filtered = SeriesService.filterByRating(seriesList, minRating);
                        printList(filtered);
                    }

                    case "3" -> {
                        // Filter by finished status
                        System.out.print("Finished only? (true/false): ");
                        boolean finished = readBoolean(sc);

                        var filtered = SeriesService.filterByFinished(seriesList, finished);
                        printList(filtered);
                    }

                    case "4" -> {
                        // Top N by rating
                        System.out.print("Enter N for TOP by rating: ");
                        int n = readInt(sc);

                        var top = SeriesService.topNByRating(seriesList, n);
                        printList(top);
                    }

                    case "5" -> {
                        // Search by partial title
                        System.out.print("Search by title (part of name): ");
                        String q = sc.nextLine().trim();

                        SeriesService.findByTitleContains(seriesList, q)
                                .ifPresentOrElse(
                                        SeriesPrinter::printOne,
                                        () -> System.out.println("Not found")
                                );
                    }

                    case "6" -> {
                        // Sort by rating descending
                        var sorted = SeriesService.sortByRatingDesc(seriesList);
                        printList(sorted);
                    }

                    case "7" -> {
                        // Display statistics summary
                        double avg = SeriesService.averageRating(seriesList);
                        System.out.printf("Average rating = %.2f%n", avg);

                        SeriesService.maxSeasons(seriesList)
                                .ifPresent(s ->
                                        System.out.println("Most seasons: "
                                                + s.getTitle()
                                                + " (" + s.getSeasons() + ")"));
                    }

                    case "8" -> {
                        // Export statistics to XML
                        System.out.print("Enter attribute (title/genre/seasons/rating/year/finished): ");
                        String attr = sc.nextLine().trim();

                        try {
                            var stats = StatisticsService.countByAttribute(seriesList, attr);

                            stats.forEach((k, v) ->
                                    System.out.println(k + " = " + v));

                            var out = Path.of("statistics_by_" + attr.toLowerCase() + ".xml");
                            XmlStatisticsWriter.write(stats, attr.toLowerCase(), out);

                            System.out.println("Saved: " + out.toAbsolutePath());

                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                    case "9" -> {
                        // Change output formatting mode
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
                            System.out.println("Invalid choice");
                        }
                    }

                    case "0" -> {
                        // Exit application
                        System.out.println("Bye!");
                        running = false;
                    }

                    default -> System.out.println("Unknown option");
                }

                System.out.println();
            }
        }
    }

    /** Prints list using current display mode */
    private static void printList(List<Series> list) {
        if (prettyMode) {
            SeriesPrinter.printShortList(list);
        } else {
            SeriesPrinter.printSimpleList(list);
        }
    }

    /** Applies formatting mode from CLI argument */
    private static void applyModeArg(String modeArg) {
        if (modeArg == null) return;
        if (modeArg.equalsIgnoreCase("simple")) prettyMode = false;
        if (modeArg.equalsIgnoreCase("pretty")) prettyMode = true;
    }

    /** Prints console menu */
    private static void printMenu() {
        System.out.println("""
                -------------------------
                1 - Show all series
                2 - Filter by min rating
                3 - Filter by finished
                4 - TOP N by rating
                5 - Search by title
                6 - Sort by rating DESC
                7 - Statistics
                8 - Export statistics to XML
                9 - Change display mode
                0 - Exit
                -------------------------
                """);
    }

    /** Reads integer input with validation */
    private static int readInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Please enter an integer: ");
            }
        }
    }

    /** Reads double input with validation */
    private static double readDouble(Scanner sc) {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (Exception e) {
                System.out.print("Please enter a number: ");
            }
        }
    }

    /** Reads boolean input with validation */
    private static boolean readBoolean(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("true")) return true;
            if (s.equals("false")) return false;
            System.out.print("Please enter true or false: ");
        }
    }
}
