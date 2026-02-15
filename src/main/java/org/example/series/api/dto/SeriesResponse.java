package org.example.series.api.dto;

/**
 * Data Transfer Object (DTO) used for exposing
 * Series data via REST API.
 *
 * This class represents the public-facing view of the domain model.
 * Internal entity (core.model.Series) must not be exposed directly.
 *
 * DTOs allow:
 * - decoupling API layer from domain layer
 * - controlling exposed fields
 * - future transformation or versioning
 */
public class SeriesResponse {

    // Series title
    private String title;

    // Comma-separated genres
    private String genre;

    // Number of seasons
    private int seasons;

    // Average rating (e.g. 8.5)
    private double rating;

    // Release year
    private int year;

    // Indicates whether series is finished
    private boolean finished;

    /**
     * Creates immutable DTO instance.
     *
     * @param title    series title
     * @param genre    genre string
     * @param seasons  number of seasons
     * @param rating   rating value
     * @param year     release year
     * @param finished finished flag
     */
    public SeriesResponse(String title,
                          String genre,
                          int seasons,
                          double rating,
                          int year,
                          boolean finished) {
        this.title = title;
        this.genre = genre;
        this.seasons = seasons;
        this.rating = rating;
        this.year = year;
        this.finished = finished;
    }

    /**
     * @return series title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return genre string
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @return number of seasons
     */
    public int getSeasons() {
        return seasons;
    }

    /**
     * @return rating value
     */
    public double getRating() {
        return rating;
    }

    /**
     * @return release year
     */
    public int getYear() {
        return year;
    }

    /**
     * @return true if series is finished
     */
    public boolean isFinished() {
        return finished;
    }
}
