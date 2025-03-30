package net.model;

public class MovieRecord implements IMovieRecord {
    /** Title of the movie. */
    private String title;
    /** Year of the movie. */
    private double year;
    /** Director of the movie. */
    private String director;
    /** Cast of the movie. */
    private String cast;
    /** Description of the movie. */
    private String description;
    /** URL of the movie poster. */
    private String posterUrl;
    /** Rating of the movie. */
    private String rating;
    /** Genre of the movie. */
    private String genre;
    /** Runtime of the movie. */
    private String runtime;
    /** Country of the movie. */
    private String country;

    /**
     * Constructor to initialize a MovieRecord object with the given parameters.
     * 
     * @param title       Title of the movie
     * @param year        Year of the movie
     * @param director    Director of the movie
     * @param cast        Cast of the movie
     * @param description Description of the movie
     * @param posterUrl   URL of the movie poster
     * @param rating      Rating of the movie
     * @param genre       Genre of the movie
     * @param runtime     Runtime of the movie
     * @param country     Country of the movie
     */
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

    /**
     * Returns the title of the movie.
     * 
     * @return the title of the movie
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Returns the year of the movie.
     * 
     * @return the year of the movie
     */
    @Override
    public double getYear() {
        return year;
    }

    /**
     * Returns the director of the movie.
     * 
     * @return the director of the movie
     */
    @Override
    public String getDirector() {
        return director;
    }

    /**
     * Returns the cast of the movie.
     * 
     * @return the cast of the movie
     */
    @Override
    public String getCast() {
        return cast;
    }

    /**
     * Returns the description of the movie.
     * 
     * @return the description of the movie
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns the URL of the movie poster.
     * 
     * @return the URL of the movie poster
     */
    @Override
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * Returns the rating of the movie.
     * 
     * @return the rating of the movie
     */
    @Override
    public String getRating() {
        return rating;
    }

    /**
     * Returns the genre of the movie.
     * 
     * @return the genre of the movie
     */
    @Override
    public String getGenre() {
        return genre;
    }

    /**
     * Returns the runtime of the movie.
     * 
     * @return the runtime of the movie
     */
    @Override
    public String getRuntime() {
        return runtime;
    }

    /**
     * Returns the country of the movie.
     * 
     * @return the country of the movie
     */
    @Override
    public String getCountry() {
        return country;
    }

    /**
     * Returns a string representation of the movie record.
     * 
     * @return a string representation of the movie record
     */
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
