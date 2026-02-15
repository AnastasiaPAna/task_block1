package org.example.series.core;

import org.junit.jupiter.api.Test;
import org.example.series.core.model.*;
import org.example.series.core.service.*;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link StatisticsService}.
 *
 * Verifies:
 * - grouping logic by different attributes
 * - correct value formatting
 * - correct counting
 * - sorting rules (count DESC, key ASC)
 * - validation for unsupported attributes
 */
class StatisticsServiceTest {

    /**
     * Verifies that:
     * - genres are split by comma
     * - values are trimmed
     * - counts are calculated correctly
     */
    @Test
    void countByAttribute_genre_shouldSplitByCommaTrimAndCount() {

        Series s1 = new Series("S1", "Drama, Sci-Fi", 1, 8.0, 2020, false);
        Series s2 = new Series("S2", "Drama", 2, 7.5, 2019, true);
        Series s3 = new Series("S3", "Sci-Fi, Horror", 3, 8.2, 2021, false);

        LinkedHashMap<String, Integer> stats =
                StatisticsService.countByAttribute(List.of(s1, s2, s3), "genre");

        assertEquals(2, stats.get("Drama"));
        assertEquals(2, stats.get("Sci-Fi"));
        assertEquals(1, stats.get("Horror"));
    }

    /**
     * Verifies grouping by title.
     * Duplicate titles must be counted properly.
     */
    @Test
    void countByAttribute_title_shouldCountEachTitle() {

        Series a = new Series("AAA", "Drama", 1, 8.0, 2020, false);
        Series b = new Series("BBB", "Drama", 1, 8.0, 2020, false);
        Series c = new Series("AAA", "Horror", 1, 8.0, 2021, true);

        var stats = StatisticsService.countByAttribute(List.of(a, b, c), "title");

        assertEquals(2, stats.get("AAA"));
        assertEquals(1, stats.get("BBB"));
    }

    /**
     * Verifies grouping by numeric attribute (seasons).
     * Values must be converted to String.
     */
    @Test
    void countByAttribute_seasons_shouldCountNumericValues() {

        Series a = new Series("A", "Drama", 2, 8.0, 2020, false);
        Series b = new Series("B", "Drama", 2, 7.0, 2020, false);
        Series c = new Series("C", "Drama", 5, 9.0, 2021, true);

        var stats = StatisticsService.countByAttribute(List.of(a, b, c), "seasons");

        assertEquals(2, stats.get("2"));
        assertEquals(1, stats.get("5"));
    }

    /**
     * Verifies grouping by release year.
     */
    @Test
    void countByAttribute_year_shouldCountYears() {

        Series a = new Series("A", "Drama", 1, 8.0, 2020, false);
        Series b = new Series("B", "Drama", 1, 8.0, 2020, true);
        Series c = new Series("C", "Drama", 1, 8.0, 2021, false);

        var stats = StatisticsService.countByAttribute(List.of(a, b, c), "year");

        assertEquals(2, stats.get("2020"));
        assertEquals(1, stats.get("2021"));
    }

    /**
     * Verifies grouping by boolean attribute (finished).
     */
    @Test
    void countByAttribute_finished_shouldCountTrueFalse() {

        Series a = new Series("A", "Drama", 1, 8.0, 2020, false);
        Series b = new Series("B", "Drama", 1, 8.0, 2020, true);
        Series c = new Series("C", "Drama", 1, 8.0, 2021, true);

        var stats = StatisticsService.countByAttribute(List.of(a, b, c), "finished");

        assertEquals(1, stats.get("false"));
        assertEquals(2, stats.get("true"));
    }

    /**
     * Verifies that rating values are formatted
     * to one decimal place before grouping.
     */
    @Test
    void countByAttribute_rating_shouldFormatToOneDecimalAndCount() {

        Series a = new Series("A", "Drama", 1, 8.74, 2020, false);
        Series b = new Series("B", "Drama", 1, 8.75, 2020, false);
        Series c = new Series("C", "Drama", 1, 9.20, 2021, true);

        var stats = StatisticsService.countByAttribute(List.of(a, b, c), "rating");

        // Because rating is formatted using "%.1f"
        assertEquals(1, stats.get("9.2"));
        assertNotNull(stats.get("8.7"));
    }

    /**
     * Verifies that unsupported attribute
     * results in IllegalArgumentException.
     */
    @Test
    void countByAttribute_shouldThrowForUnknownAttribute() {

        Series a = new Series("A", "Drama", 1, 8.0, 2020, false);

        assertThrows(IllegalArgumentException.class,
                () -> StatisticsService.countByAttribute(List.of(a), "unknown"));
    }

    /**
     * Verifies sorting order:
     * 1) count DESC
     * 2) key ASC
     */
    @Test
    void countByAttribute_shouldSortByCountDesc_thenKeyAsc() {

        // Drama=2, Horror=1, Sci-Fi=1
        Series a = new Series("A", "Drama, Sci-Fi", 1, 8.0, 2020, false);
        Series b = new Series("B", "Drama", 1, 8.0, 2020, false);
        Series c = new Series("C", "Horror", 1, 8.0, 2020, false);

        var stats = StatisticsService.countByAttribute(List.of(a, b, c), "genre");

        String firstKey = stats.keySet().iterator().next();

        assertEquals("Drama", firstKey);
        assertEquals(2, stats.get("Drama"));
    }
}
