package org.example.series.api.controller;

import org.example.series.api.service.SeriesApiService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller responsible for statistics-related endpoints.
 *
 * Base URL: /api/v1/statistics
 *
 * Provides aggregated statistical data calculated from
 * the loaded series dataset.
 */
@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    // Service layer dependency injected by Spring
    private final SeriesApiService service;

    /**
     * Constructor-based dependency injection.
     */
    public StatisticsController(SeriesApiService service) {
        this.service = service;
    }

    /**
     * Returns aggregated statistics grouped by selected attribute.
     *
     * Example:
     * GET /api/v1/statistics/genre
     * GET /api/v1/statistics/year
     *
     * Supported attributes:
     * title, genre, seasons, rating, year, finished
     *
     * @param attribute grouping attribute
     * @return map where key = attribute value, value = count
     */
    @GetMapping("/{attribute}")
    public Map<String, Integer> statistics(@PathVariable String attribute) {
        return service.statistics(attribute);
    }
}
