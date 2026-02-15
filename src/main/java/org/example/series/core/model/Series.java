package org.example.series.core.model;

/**
 * Domain model representing a TV series.
 *
 * This class is used as a data holder for:
 * - JSON deserialization (via Gson)
 * - business logic operations
 * - statistics calculations
 *
 * Instances of this class are immutable from outside
 * (no setters provided).
 */
public class Series {

    /** Title of the series */
    private String title;

    /** Genre(s) of the series (comma-separated) */
    private String genre;

    /** Number of seasons */
    private int seasons;

    /** Average rating (e.g. IMDb rating) */
    private double rating;

    /** Release year */
    private int year;

    /** Indicates whether the series is finished */
    private boolean finished;

    /**
     * Default constructor required for Gson deserialization.
     */
    public Series() {
    }

    /**
     * Full constructor.
     *
     * @param title    series title
     * @param genre    genre or multiple genres
     * @param seasons  number of seasons
     * @param rating   average rating
     * @param year     release year
     * @param finished whether the series is finished
     */
    public Series(String title, String genre, int seasons, double rating, int year, boolean finished) {
        this.title = title;
        this.genre = genre;
        this.seasons = seasons;
        this.rating = rating;
        this.year = year;
        this.finished = finished;
    }

    /** @return series title */
    public String getTitle() {
        return title;
    }

    /** @return genre(s) */
    public String getGenre() {
        return genre;
    }

    /** @return number of seasons */
    public int getSeasons() {
        return seasons;
    }

    /** @return average rating */
    public double getRating() {
        return rating;
    }

    /** @return release year */
    public int getYear() {
        return year;
    }

    /** @return true if series is finished */
    public boolean isFinished() {
        return finished;
    }

    @Override
    public String toString() {
        return "Series{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", seasons=" + seasons +
                ", rating=" + rating +
                ", year=" + year +
                ", finished=" + finished +
                '}';
    }
}