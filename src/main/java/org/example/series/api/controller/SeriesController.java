package org.example.series.api.controller;

import org.example.series.api.service.SeriesApiService;
import org.example.series.api.dto.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * REST controller responsible for handling HTTP requests
 * related to Series resources.
 *
 * Base URL: /api/v1/series
 *
 * This controller delegates all business logic to SeriesApiService
 * and returns DTO objects instead of exposing internal domain models.
 */
@RestController
@RequestMapping("/api/v1/series")
public class SeriesController {

    // Service layer dependency injected by Spring
    private final SeriesApiService service;

    /**
     * Constructor-based dependency injection.
     */
    public SeriesController(SeriesApiService service) {
        this.service = service;
    }

    /**
     * Returns all available series.
     *
     * GET /api/v1/series
     *
     * @return list of series DTO objects
     */
    @GetMapping
    public List<SeriesResponse> getAll() {
        return service.getAll();
    }

    /**
     * Returns top N series sorted by rating (descending).
     *
     * GET /api/v1/series/top?n=5
     *
     * @param n number of top records (default = 5)
     * @return list of top-rated series
     */
    @GetMapping("/top")
    public List<SeriesResponse> top(
            @RequestParam(defaultValue = "5") int n) {

        if (n <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Parameter 'n' must be greater than 0"
            );
        }

        return service.top(n);
    }

    /**
     * Searches for a series by partial title match.
     *
     * GET /api/v1/series/search?query=game
     *
     * @param query part of title to search
     * @return found series DTO
     */
    @GetMapping("/search")
    public SeriesResponse search(
            @RequestParam(required = false) String query) {

        if (query == null || query.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Query parameter is required"
            );
        }

        return service.search(query);
    }
}
