package org.example.series.api.service;

import org.example.series.api.dto.SeriesResponse;
import org.example.series.core.model.Series;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeriesApiServiceTest {

    private SeriesApiService service;

    @BeforeEach
    void setup() {
        List<Series> list = List.of(
                new Series("A", "Drama", 1, 7.5, 2019, false),
                new Series("B", "Horror", 2, 9.0, 2020, true)
        );

        service = new SeriesApiService(list);
    }

    @Test
    void getAll_shouldReturnAllSeries() {
        List<SeriesResponse> result = service.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void top_shouldReturnHighestRated() {
        List<SeriesResponse> result = service.top(1);

        assertEquals(1, result.size());
        assertEquals("B", result.get(0).getTitle());
    }

    @Test
    void search_shouldReturnMatchingSeries() {
        SeriesResponse result = service.search("A");
        assertEquals("A", result.getTitle());
    }

    @Test
    void search_shouldThrowIfNotFound() {
        assertThrows(RuntimeException.class,
                () -> service.search("Unknown"));
    }
}
