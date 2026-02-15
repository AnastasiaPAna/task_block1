package org.example.series.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.example.series.core.model.*;
import org.example.series.core.loader.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SeriesLoader}.
 *
 * Verifies:
 * - single file loading
 * - folder loading
 * - JSON parsing correctness
 * - filtering of non-JSON files
 */
class SeriesLoaderTest {

    /**
     * Temporary directory automatically created
     * and cleaned by JUnit for file-based tests.
     */
    @TempDir
    Path tempDir;

    /**
     * Verifies that a single JSON file
     * is correctly parsed into a Series object.
     */
    @Test
    void load_shouldParseJsonToSeries() throws Exception {

        // Arrange: create temporary JSON file
        String json = """
                {
                  "title": "Test Series",
                  "genre": "Drama, Sci-Fi",
                  "seasons": 2,
                  "rating": 8.5,
                  "year": 2020,
                  "finished": false
                }
                """;

        Path file = tempDir.resolve("test.json");
        Files.writeString(file, json);

        // Act: load series from file
        Series s = SeriesLoader.load(file.toString());

        // Assert: verify parsed values
        assertNotNull(s);
        assertEquals("Test Series", s.getTitle());
        assertEquals("Drama, Sci-Fi", s.getGenre());
        assertEquals(2, s.getSeasons());
        assertEquals(8.5, s.getRating(), 1e-9);
        assertEquals(2020, s.getYear());
        assertFalse(s.isFinished());
    }

    /**
     * Verifies that:
     * - only .json files are loaded from folder
     * - non-JSON files are ignored
     * - all valid files are parsed
     */
    @Test
    void loadFromFolder_shouldLoadAllJsonFiles() throws Exception {

        // Arrange: create test files
        Files.writeString(tempDir.resolve("a.json"), """
                {"title":"A","genre":"Drama","seasons":1,"rating":7.0,"year":2010,"finished":true}
                """);

        Files.writeString(tempDir.resolve("b.json"), """
                {"title":"B","genre":"Drama, Horror","seasons":2,"rating":8.0,"year":2012,"finished":false}
                """);

        // Non-JSON file should be ignored
        Files.writeString(tempDir.resolve("readme.txt"), "hello");

        // Act: load all series from folder
        List<Series> list = SeriesLoader.loadFromFolder(tempDir.toString());

        // Assert: verify correct number and content
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(s -> s.getTitle().equals("A")));
        assertTrue(list.stream().anyMatch(s -> s.getTitle().equals("B")));
    }
}
