package org.example.series.api.mapper;

import org.example.series.api.dto.SeriesResponse;
import org.example.series.core.model.Series;

import java.util.List;

/**
 * Mapper utility responsible for converting
 * internal domain model objects (Series)
 * into API-facing DTO objects (SeriesResponse).
 *
 * This layer isolates the API contract from
 * the internal core model representation.
 *
 * If domain model changes, API remains stable.
 */
public final class SeriesMapper {

    /**
     * Private constructor to prevent instantiation.
     * This class contains only static utility methods.
     */
    private SeriesMapper() {
    }

    /**
     * Converts a single Series domain object
     * into SeriesResponse DTO.
     *
     * @param s domain model object
     * @return mapped DTO object
     */
    public static SeriesResponse toResponse(Series s) {
        if (s == null) {
            return null;
        }

        return new SeriesResponse(
                s.getTitle(),
                s.getGenre(),
                s.getSeasons(),
                s.getRating(),
                s.getYear(),
                s.isFinished()
        );
    }

    /**
     * Converts a list of Series domain objects
     * into a list of SeriesResponse DTO objects.
     *
     * @param list list of domain objects
     * @return list of DTO objects
     */
    public static List<SeriesResponse> toResponseList(List<Series> list) {
        if (list == null) {
            return List.of();
        }

        return list.stream()
                .map(SeriesMapper::toResponse)
                .toList();
    }
}
