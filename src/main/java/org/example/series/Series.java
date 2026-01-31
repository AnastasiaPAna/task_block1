package org.example.series;

public class Series {

    private String title;
    private String genre;
    private int seasons;
    private double rating;
    private int year;
    private boolean finished;

    // порожній конструктор для Gson
    public Series() {
    }

    public Series(String title, String genre, int seasons, double rating, int year, boolean finished) {
        this.title = title;
        this.genre = genre;
        this.seasons = seasons;
        this.rating = rating;
        this.year = year;
        this.finished = finished;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getSeasons() {
        return seasons;
    }

    public double getRating() {
        return rating;
    }

    public int getYear() {
        return year;
    }

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