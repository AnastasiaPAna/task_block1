package org.example.series.api.controller;

import jakarta.validation.Valid;
import org.example.series.api.dto.SeriesListRequest;
import org.example.series.api.dto.SeriesRequest;
import org.example.series.api.dto.SeriesResponse;
import org.example.series.api.service.SeriesApiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * REST controller for CRUD operations, search, pagination, report generation and JSON import for TV series.
 */
@RestController
@RequestMapping("/api/v1/series")
public class SeriesController {

    private final SeriesApiService service;

    public SeriesController(SeriesApiService service) {
        this.service = service;
    }

    /**
     * Returns all series.
     *
     * @return list of series DTOs
     */
    @GetMapping
    public List<SeriesResponse> getAll() {
        return service.getAll();
    }

    /**
     * Returns a single series by id.
     *
     * @param id series id
     * @return series DTO
     */
    @GetMapping("/{id}")
    public SeriesResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * Returns top N series ordered by rating (descending).
     *
     * @param n number of items to return (must be > 0)
     * @return list of series DTOs
     */
    @GetMapping("/top")
    public List<SeriesResponse> top(@RequestParam(defaultValue = "5") int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Parameter 'n' must be greater than 0");
        }
        return service.top(n);
    }

    /**
     * Searches for a series by free-text query (e.g., by title).
     *
     * @param query query string (required, not blank)
     * @return matched series DTO
     */
    @GetMapping("/search")
    public SeriesResponse search(@RequestParam(required = false) String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query parameter is required");
        }
        return service.search(query);
    }

    /**
     * Creates a new series.
     *
     * @param request series create request
     * @return created series DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeriesResponse create(@Valid @RequestBody SeriesRequest request) {
        return service.create(request);
    }

    /**
     * Updates an existing series.
     *
     * @param id series id
     * @param request update request
     * @return updated series DTO
     */
    @PutMapping("/{id}")
    public SeriesResponse update(@PathVariable Long id,
                                 @Valid @RequestBody SeriesRequest request) {
        return service.update(id, request);
    }

    /**
     * Deletes a series by id.
     *
     * @param id series id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /**
     * Returns a paged list of series with optional filters.
     * Page number in API is 1-based: page=1 corresponds to the first page.
     *
     * @param request list request with filters and paging/sorting options
     * @return map containing keys: 'list' and 'totalPages'
     */
    @PostMapping("/_list")
    public Map<String, Object> list(@Valid @RequestBody SeriesListRequest request) {

        Sort sort = Sort.by(
                Sort.Direction.fromString(request.getDirection()),
                request.getSortBy()
        );

        // In API we use 1-based page number (as in the task description): page=1 -> first page
        int pageIndex = Math.max(0, request.getPage() - 1);

        Pageable pageable = PageRequest.of(
                pageIndex,
                request.getSize(),
                sort
        );

        Page<SeriesResponse> page = service.search(
                request.getStudioId(),
                request.getMinRating(),
                request.getYear(),
                request.getGenre(),
                pageable
        );

        return Map.of(
                "list", page.getContent(),
                "totalPages", page.getTotalPages()
        );
    }

    /**
     * Generates a report for the requested filters.
     * Supports sync and async modes depending on request flags.
     *
     * @param request report request (same as list filters)
     * @return response entity containing file bytes or async job info
     */
    @PostMapping("/_report")
    public ResponseEntity<?> report(@Valid @RequestBody SeriesListRequest request) {
        return service.generateReport(request);
    }

    /**
     * Imports series from a JSON file (multipart/form-data).
     *
     * @param file JSON file to import
     * @return import summary: success/failed/errors
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        return service.upload(file);
    }

    /**
     * Downloads a previously generated asynchronous report.
     *
     * @param jobId job identifier returned by async report generation
     * @return file bytes as response entity
     */
    @GetMapping("/_report/{jobId}")
    public ResponseEntity<byte[]> downloadReport(@PathVariable String jobId) {
        return service.downloadReport(jobId);
    }
}
