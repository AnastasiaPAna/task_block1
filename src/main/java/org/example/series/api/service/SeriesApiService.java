package org.example.series.api.service;

import org.example.series.api.dto.SeriesResponse;
import org.example.series.api.mapper.SeriesMapper;
import org.example.series.core.model.Series;
import org.example.series.core.service.SeriesService;
import org.example.series.core.service.StatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * API-level service layer.
 *
 * Acts as an intermediary between REST controllers
 * and core business logic.
 *
 * Responsibilities:
 * - Orchestrate core services
 * - Convert domain models to DTOs
 * - Provide clean API-facing responses
 *
 * This layer must not contain heavy business logic.
 */
@Service
public class SeriesApiService {

    // In-memory dataset loaded via Spring configuration (DataConfig)
    private final List<Series> seriesList;

    /**
     * Constructor-based dependency injection.
     *
     * Spring injects the shared dataset bean.
     */
    public SeriesApiService(List<Series> seriesList) {
        this.seriesList = seriesList;
    }

    /**
     * Returns all series mapped to DTO objects.
     *
     * @return list of SeriesResponse
     */
    public List<SeriesResponse> getAll() {
        return SeriesMapper.toResponseList(seriesList);
    }

    /**
     * Returns top N series sorted by rating (descending).
     *
     * @param n number of records
     * @return list of SeriesResponse
     */
    public List<SeriesResponse> top(int n) {
        return SeriesMapper.toResponseList(
                SeriesService.topNByRating(seriesList, n)
        );
    }

    /**
     * Searches for a series by partial title match.
     *
     * @param query search text
     * @return SeriesResponse DTO
     * @throws RuntimeException if not found
     */
    public SeriesResponse search(String query) {

        Series s = SeriesService
                .findByTitleContains(seriesList, query)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Series not found"
                        )
                );

        return SeriesMapper.toResponse(s);
    }

    /**
     * Returns aggregated statistics grouped by attribute.
     *
     * Supported attributes:
     * title, genre, seasons, rating, year, finished
     *
     * @param attribute grouping attribute
     * @return map of values with occurrence count
     */
    public Map<String, Integer> statistics(String attribute) {
        return StatisticsService.countByAttribute(seriesList, attribute);
    }
}
