package org.example.series;

import com.google.gson.Gson;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SeriesLoader {

    private static final Gson GSON = new Gson();

    public static Series load(String path) {
        try (Reader reader = Files.newBufferedReader(Path.of(path))) {
            return GSON.fromJson(reader, Series.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load series from file: " + path, e);
        }
    }

    public static List<Series> loadAll(List<String> paths) {
        List<Series> list = new ArrayList<>();
        for (String p : paths) {
            list.add(load(p));
        }
        return list;
    }

    public static List<Series> loadFromFolder(String folderPath) {
        return loadFromFolder(Path.of(folderPath), 4); // 4 потоки за замовчуванням
    }


    public static List<Series> loadFromFolder(Path folder, int threads) {
        try (var paths = Files.list(folder);
             ExecutorService pool = Executors.newFixedThreadPool(threads)) {

            List<Path> files = paths
                    .filter(p -> p.toString().endsWith(".json"))
                    .toList();

            List<Future<Series>> futures = new ArrayList<>();
            for (Path f : files) {
                futures.add(pool.submit(() -> parseOneFile(f)));
            }

            List<Series> result = new ArrayList<>();
            for (Future<Series> fut : futures) {
                result.add(fut.get());
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load from folder: " + folder, e);
        }
    }

    private static Series parseOneFile(Path file) {
        try (Reader r = Files.newBufferedReader(file)) {
            return GSON.fromJson(r, Series.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse file: " + file, e);
        }
    }
}
