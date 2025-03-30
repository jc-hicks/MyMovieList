package net.model;

public class MovieRecord implements IMovieRecord {
    private String title;
    private double year;
    private String director;
    private String cast;
    private String description;
    private String posterUrl;
    private String rating;
    private String genre;
    private String runtime;
    private String country;

    public MovieRecord(String title, double year, String director, String cast, String description,
     String posterUrl, String rating, String genre, String runtime, String country) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.cast = cast;
        this.description = description;
        this.posterUrl = posterUrl;
        this.rating = rating;
        this.genre = genre;
        this.runtime = runtime;
        this.country = country;
    }

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

    public static void main(String[] args) {
        MovieRecord movie = new MovieRecord("Inception", 2010, "Christopher Nolan", "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
                "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.",
                "https://example.com/poster.jpg", "8.8", "Action, Sci-Fi", "148 min", "USA");

        System.out.println(movie.movieToString());
    }

}
