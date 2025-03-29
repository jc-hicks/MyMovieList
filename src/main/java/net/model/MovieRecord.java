package net.model;

public class MovieRecord implements IMovieRecord {
    private static String title;
    private static double year;
    private static String director;
    private static String cast;
    private static String description;
    private static String posterUrl;
    private static String rating;
    private static String genre;
    private static String runtime;
    private static String country;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public double getYear() {
        return year;
    }

    @Override
    public String getDirector() {
        return director;
    }

    @Override
    public String getCast() {
        return cast;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPosterUrl() {
        return posterUrl;
    }

    @Override
    public String getRating() {
        return rating;
    }

    @Override
    public String getGenre() {
        return genre;
    }

    @Override
    public String getRuntime() {
        return runtime;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String movieToString() {
        return String.format("Title: %s\nYear: %.0f\nDirector: %s\nCast: %s\nDescription: %s\nPoster URL: %s\nRating: %s\nGenre: %s\nRuntime: %s\nCountry: %s",
                title, year, director, cast, description, posterUrl, rating, genre, runtime, country);
    }

}
