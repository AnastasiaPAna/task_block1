package org.example.series.api.dto;

import jakarta.validation.constraints.Min;

/**
 * SeriesListRequest component.
 */
public class SeriesListRequest {

    private Long studioId;

    @Min(value = 0, message = "Rating must be >= 0")
    private Double minRating;

    private Integer year;

    private String genre;

    @Min(1)
    private int page = 1;

    @Min(1)
    private int size = 10;

    private String sortBy = "id";

    private String direction = "ASC";

    private String format; // csv | xlsx | json

    private Boolean async;

    public Long getStudioId() { return studioId; }
    public void setStudioId(Long studioId) { this.studioId = studioId; }

    public Double getMinRating() { return minRating; }
    public void setMinRating(Double minRating) { this.minRating = minRating; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public Boolean getAsync() { return async; }
    public void setAsync(Boolean async) { this.async = async; }
}
