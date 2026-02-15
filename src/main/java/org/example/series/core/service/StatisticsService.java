package org.example.series.core.service;

import java.util.*;
import java.util.stream.Collectors;

import org.example.series.core.model.Series;

/**
 * Service responsible for calculating aggregated statistics
 * over a collection of {@link Series}.
 *
 * Supports grouping by:
 * - title
 * - genre
 * - seasons
 * - rating
 * - year
 * - finished
 *
 * Returned statistics are:
 * 1) sorted by count DESC
 * 2) then sorted by key ASC
 */
public class StatisticsService {

    /**
     * Counts occurrences grouped by selected attribute.
     *
     * @param list      list of series
     * @param attribute attribute name to group by
     * @return sorted LinkedHashMap (count DESC, key ASC)
     */
    public static LinkedHashMap<String, Integer> countByAttribute(
            List<Series> list,
            String attribute
    ) {

        // Defensive null check
        if (list == null) return new LinkedHashMap<>();

        if (attribute == null || attribute.isBlank()) {
            throw new IllegalArgumentException(
                    "Attribute is empty. Supported: title, genre, seasons, rating, year, finished"
            );
        }

        String a = attribute.trim().toLowerCase();

        // Temporary map for counting occurrences
        Map<String, Integer> counts = new HashMap<>();

        for (Series s : list) {

            if (s == null) continue;

            // Extract one or multiple grouping keys
            List<String> keys = extractKeys(s, a);

            // Increase counter for each key
            for (String key : keys) {
                counts.put(key, counts.getOrDefault(key, 0) + 1);
            }
        }

        // Sort:
        // 1) by value descending
        // 2) by key ascending (case-insensitive)
        return counts.entrySet().stream()
                .sorted((e1, e2) -> {
                    int c = Integer.compare(e2.getValue(), e1.getValue());
                    if (c != 0) return c;
                    return e1.getKey().compareToIgnoreCase(e2.getKey());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> x,
                        LinkedHashMap::new
                ));
    }

    /**
     * Extracts grouping keys from a series
     * depending on selected attribute.
     *
     * Some attributes (like genre) may produce multiple keys.
     */
    private static List<String> extractKeys(Series s, String attribute) {

        return switch (attribute) {
            case "title" ->
                    List.of(safe(s.getTitle()));

            case "genre" ->
                    splitGenres(s.getGenre());

            case "seasons" ->
                    List.of(String.valueOf(s.getSeasons()));

            case "rating" ->
                // Format rating to one decimal place
                    List.of(String.format(Locale.US, "%.1f", s.getRating()));

            case "year" ->
                    List.of(String.valueOf(s.getYear()));

            case "finished" ->
                    List.of(String.valueOf(s.isFinished()));

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported attribute: " + attribute +
                                    ". Supported: title, genre, seasons, rating, year, finished"
                    );
        };
    }

    /**
     * Splits comma-separated genre string into individual values.
     *
     * @param genreLine raw genre string
     * @return list of cleaned genre values
     */
    private static List<String> splitGenres(String genreLine) {

        if (genreLine == null || genreLine.isBlank())
            return List.of("unknown");

        String[] parts = genreLine.split(",");
        List<String> genres = new ArrayList<>();

        for (String p : parts) {
            String g = p.trim();
            if (!g.isEmpty()) genres.add(g);
        }

        return genres.isEmpty() ? List.of("unknown") : genres;
    }

    /**
     * Normalizes string values.
     * Returns "unknown" for null or blank values.
     */
    private static String safe(String v) {
        if (v == null || v.isBlank()) return "unknown";
        return v.trim();
    }
}
