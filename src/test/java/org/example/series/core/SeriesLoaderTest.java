package org.example.series.core;

import org.example.series.core.loader.SeriesLoader;
import org.example.series.core.model.Series;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeriesLoaderTest {

    @Test
    void shouldLoadSingleSeries() {

        // Loader now works via static method
        List<Series> list =
                SeriesLoader.loadAll(List.of("data/series.json"));

        assertNotNull(list);
        assertFalse(list.isEmpty());

        Series series = list.get(0);

        assertNotNull(series.getTitle());
        assertTrue(series.getSeasons() > 0);
        assertTrue(series.getYear() > 1900);
        assertNotNull(series.getStudio());
    }
}
