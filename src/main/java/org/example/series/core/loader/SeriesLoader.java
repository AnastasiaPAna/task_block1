package org.example.series.core.loader;

import com.google.gson.Gson;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.example.series.core.model.Series;

/**
 * Utility class responsible for loading {@link Series} objects
 * from JSON files.
 *
 * Supports:
 * - loading a single file
 * - loading multiple files
 * - loading all JSON files from a folder (optionally in parallel)
 *
 * This class is stateless and thread-safe.
 * All methods are static and intended for utility-style usage.
 */
public class SeriesLoader {

    /** Gson instance used for JSON deserialization */
    private static final Gson GSON = new Gson();

    /**
     * Loads a single JSON file and converts it into a {@link Series} object.
     *
     * @param path path to JSON file
     * @return parsed Series instance
     */
    public static Series load(String path) {
        try (Reader reader = Files.newBufferedReader(Path.of(path))) {
            return GSON.fromJson(reader, Series.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load series from file: " + path, e);
        }
    }

    /**
     * Loads multiple JSON files sequentially.
     *
     * @param paths list of file paths
     * @return list of parsed Series objects
     */
    public static List<Series> loadAll(List<String> paths) {
        List<Series> list = new ArrayList<>();
        for (String p : paths) {
            list.add(load(p));
        }
        return list;
    }

    /**
     * Loads all JSON files from a folder using default thread count (4).
     *
     * @param folderPath path to directory
     * @return list of parsed Series objects
     */
    public static List<Series> loadFromFolder(String folderPath) {
        return loadFromFolder(Path.of(folderPath), 4); // 4 потоки за замовчуванням
    }

    /**
     * Loads all JSON files from a folder in parallel.
     *
     * @param folder  directory containing JSON files
     * @param threads number of threads to use
     * @return list of parsed Series objects
     */
    public static List<Series> loadFromFolder(Path folder, int threads) {

        // Create thread pool for parallel file processing
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        try (var paths = Files.list(folder)) {

            // Filter only JSON files
            List<Path> files = paths
                    .filter(p -> p.toString().endsWith(".json"))
                    .toList();

            // Submit parsing tasks to thread pool
            List<Future<Series>> futures = new ArrayList<>();
            for (Path f : files) {
                futures.add(pool.submit(() -> parseOneFile(f)));
            }

            // Collect results
            List<Series> result = new ArrayList<>();
            for (Future<Series> fut : futures) {
                result.add(fut.get());
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load from folder: " + folder, e);
        } finally {
            // Always shutdown thread pool
            pool.shutdown();
        }
    }

    /**
     * Parses a single JSON file into a Series object.
     *
     * @param file path to JSON file
     * @return parsed Series instance
     */
    private static Series parseOneFile(Path file) {
        try (Reader r = Files.newBufferedReader(file)) {
            return GSON.fromJson(r, Series.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse file: " + file, e);
        }
    }
}
