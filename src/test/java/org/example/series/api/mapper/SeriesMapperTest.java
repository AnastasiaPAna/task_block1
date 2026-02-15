package org.example.series.api.mapper;

import org.example.series.api.dto.SeriesResponse;
import org.example.series.core.model.Series;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeriesMapperTest {

    @Test
    void toResponse_shouldMapAllFieldsCorrectly() {

        Series s = new Series("Test", "Drama", 2, 8.5, 2020, true);

        SeriesResponse dto = SeriesMapper.toResponse(s);

        assertNotNull(dto);
        assertEquals("Test", dto.getTitle());
        assertEquals("Drama", dto.getGenre());
        assertEquals(2, dto.getSeasons());
        assertEquals(8.5, dto.getRating());
        assertEquals(2020, dto.getYear());
        assertTrue(dto.isFinished());
    }

    @Test
    void toResponseList_shouldMapListCorrectly() {

        List<Series> list = List.of(
                new Series("A", "Drama", 1, 7.0, 2019, false),
                new Series("B", "Horror", 2, 8.0, 2020, true)
        );

        var result = SeriesMapper.toResponseList(list);

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getTitle());
        assertEquals("B", result.get(1).getTitle());
    }
}
