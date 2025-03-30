package model;


/**
 * Interface representing a movie record with its details.
 * 
 */
public interface IMovieRecord { 

    /**
     * Returns the title of the movie.
     * 
     * @return the title of the movie
     */
    String getTitle();

    /**
     * Returns the year of the movie.
     * 
     * @return the year of the movie
     */
    String getYear();

    /**
     * Returns the director of the movie.
     * 
     * @return the director of the movie
     */
    String getDirector();

    /**
     * Returns the cast of the movie.
     * 
     * @return the cast of the movie
     */
    String getCast();

    /**
     * Returns the description of the movie.
     * 
     * @return the description of the movie
     */
    String getDescription();

    /**
     * Returns the URL of the movie poster.
     * 
     * @return the URL of the movie poster
     */
    String getPosterUrl();

    /**
     * Returns the rating of the movie.
     * 
     * @return the rating of the movie
     */
    String getRating();

    /**
     * Returns the genre of the movie.
     * 
     * @return the genre of the movie
     */
    String getGenre();

    /**
     * Returns the runtime of the movie.
     * 
     * @return the runtime of the movie
     */
    String getRuntime();

    /**
     * Returns the country of the movie.
     * 
     * @return the country of the movie
     */
    String getCountry();

    /**
     * Returns a string representation of the movie record.
     * 
     * @return a string representation of the movie record
     */
    String movieToString();

}
