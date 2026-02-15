package org.example.series.core.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.example.series.core.model.Series;

/**
 * Service layer containing business logic operations
 * for {@link Series} collections.
 *
 * Provides filtering, sorting, searching and
 * statistical calculations.
 *
 * This class is stateless and all methods are static.
 */
public class SeriesService {

    /**
     * Filters series by minimum rating.
     *
     * @param list       list of series
     * @param minRating  minimum rating threshold
     * @return list of series with rating >= minRating
     */
    public static List<Series> filterByRating(List<Series> list, double minRating) {
        return list.stream()
                .filter(s -> s.getRating() >= minRating)
                .toList();
    }

    /**
     * Filters series by finished status.
     *
     * @param list      list of series
     * @param finished  true to get finished series, false otherwise
     * @return filtered list
     */
    public static List<Series> filterByFinished(List<Series> list, boolean finished) {
        return list.stream()
                .filter(s -> s.isFinished() == finished)
                .toList();
    }

    /**
     * Sorts series by rating in descending order.
     *
     * @param list list of series
     * @return sorted list (highest rating first)
     */
    public static List<Series> sortByRatingDesc(List<Series> list) {
        return list.stream()
                .sorted(Comparator.comparingDouble(Series::getRating).reversed())
                .toList();
    }

    /**
     * Returns top N series by rating.
     *
     * @param list list of series
     * @param n    number of top items to return
     * @return list containing top N series
     */
    public static List<Series> topNByRating(List<Series> list, int n) {
        return sortByRatingDesc(list).stream()
                .limit(n)
                .toList();
    }

    /**
     * Finds first series whose title contains given query (case-insensitive).
     *
     * @param list  list of series
     * @param query search substring
     * @return Optional containing matching series if found
     */
    public static Optional<Series> findByTitleContains(List<Series> list, String query) {
        String q = query.toLowerCase();
        return list.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(q))
                .findFirst();
    }

    /**
     * Calculates average rating across all series.
     *
     * @param list list of series
     * @return average rating or 0.0 if list is empty
     */
    public static double averageRating(List<Series> list) {
        return list.stream()
                .mapToDouble(Series::getRating)
                .average()
                .orElse(0.0);
    }

    /**
     * Finds series with the maximum number of seasons.
     *
     * @param list list of series
     * @return Optional containing series with most seasons
     */
    public static Optional<Series> maxSeasons(List<Series> list) {
        return list.stream()
                .max(Comparator.comparingInt(Series::getSeasons));
    }
}
