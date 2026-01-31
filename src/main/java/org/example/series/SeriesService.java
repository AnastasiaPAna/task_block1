package org.example.series;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SeriesService {

    // 1) Фільтр: серіали з рейтингом >= minRating
    public static List<Series> filterByRating(List<Series> list, double minRating) {
        return list.stream()
                .filter(s -> s.getRating() >= minRating)
                .toList();
    }

    // 2) Фільтр: завершені / не завершені
    public static List<Series> filterByFinished(List<Series> list, boolean finished) {
        return list.stream()
                .filter(s -> s.isFinished() == finished)
                .toList();
    }

    // 3) Сортування за рейтингом (спадання)
    public static List<Series> sortByRatingDesc(List<Series> list) {
        return list.stream()
                .sorted(Comparator.comparingDouble(Series::getRating).reversed())
                .toList();
    }

    // 4) Топ N за рейтингом
    public static List<Series> topNByRating(List<Series> list, int n) {
        return sortByRatingDesc(list).stream()
                .limit(n)
                .toList();
    }

    // 5) Пошук за назвою (часткове співпадіння)
    public static Optional<Series> findByTitleContains(List<Series> list, String query) {
        String q = query.toLowerCase();
        return list.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(q))
                .findFirst();
    }

    // 6) Середній рейтинг
    public static double averageRating(List<Series> list) {
        return list.stream()
                .mapToDouble(Series::getRating)
                .average()
                .orElse(0.0);
    }

    // 7) Найдовший серіал за кількістю сезонів (max)
    public static Optional<Series> maxSeasons(List<Series> list) {
        return list.stream()
                .max(Comparator.comparingInt(Series::getSeasons));
    }
}