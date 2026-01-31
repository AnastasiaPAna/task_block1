package org.example.series;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsService {

    /**
     * Рахує статистику по атрибуту (title/genre/seasons/rating/year/finished)
     * Повертає відсортовану мапу:
     *  - спочатку за кількістю DESC
     *  - потім за ключем ASC
     */
    public static LinkedHashMap<String, Integer> countByAttribute(List<Series> list, String attribute) {
        if (list == null) return new LinkedHashMap<>();
        if (attribute == null || attribute.isBlank()) {
            throw new IllegalArgumentException("Attribute is empty. Supported: title, genre, seasons, rating, year, finished");
        }

        String a = attribute.trim().toLowerCase();
        Map<String, Integer> counts = new HashMap<>();

        for (Series s : list) {
            if (s == null) continue;

            List<String> keys = extractKeys(s, a);
            for (String key : keys) {
                counts.put(key, counts.getOrDefault(key, 0) + 1);
            }
        }

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

    private static List<String> extractKeys(Series s, String attribute) {
        return switch (attribute) {
            case "title" -> List.of(safe(s.getTitle()));
            case "genre" -> splitGenres(s.getGenre());
            case "seasons" -> List.of(String.valueOf(s.getSeasons()));
            case "rating" -> List.of(String.format(Locale.US, "%.1f", s.getRating()));
            case "year" -> List.of(String.valueOf(s.getYear()));
            case "finished" -> List.of(String.valueOf(s.isFinished()));
            default -> throw new IllegalArgumentException(
                    "Unsupported attribute: " + attribute +
                            ". Supported: title, genre, seasons, rating, year, finished"
            );
        };
    }

    private static List<String> splitGenres(String genreLine) {
        if (genreLine == null || genreLine.isBlank()) return List.of("unknown");

        String[] parts = genreLine.split(",");
        List<String> genres = new ArrayList<>();

        for (String p : parts) {
            String g = p.trim();
            if (!g.isEmpty()) genres.add(g);
        }

        return genres.isEmpty() ? List.of("unknown") : genres;
    }

    private static String safe(String v) {
        if (v == null || v.isBlank()) return "unknown";
        return v.trim();
    }
}
