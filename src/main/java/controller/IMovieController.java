package controller;

import java.util.List;

import model.IMovieModel;

/**
 * This interface defines the features expected in the GUI from a controller-like object
 */
public interface IMovieController {
    
    /**
     * Searches record for specific title
     * @param title
     */
    void searchMovie(String title);

    /**
     * Adds record to personal watchlist
     * @param title
     */
    void addToWatchList(String title);

    /**
     * Removes record from personal watchlist
     * @param title
     */
    void removeFromWatchList(String title);

    /**
     * Saves the watchlist to a file.
     * 
     */
    void saveWatchList();

    /**
     * Loads the watchlist from a file on startup.
     * 
     */
    List<IMovieModel.MRecord> getWatchList();

    /**
     * Loads the masterlist from a file on startup.
     *
     */
    List<IMovieModel.MRecord> getAllMovies();

    /**
     * Sorts movie list by ascending or descending order
     * @param column column is set by year, title, director, rating
     * @param ascOrDesc sorts in asc/desc
     * @return sorted list
     */
    List<IMovieModel.MRecord> sortMovieList(String column, String ascOrDesc);

    /**
     * right click option to add personal rating to watchlist json
     * @param title title of the movie record
     * @param rating rating int 1-10
     */
    void setMyRating(String title, String rating);

    /**
     * Accesses set personal set rating
     * @param title title of the movie record
     * @return returns int 10
     */
    String getMyRating(String title);

    /**
     * Filters list of movies accessed from record or API
     * @param field string of movie records
     * @param criteria include title, year, director, rating
     * @return filtered list.
     */
    List<IMovieModel.MRecord> filterMovieList(String field, String criteria);

    /**
     * Saves watchlist as json file to user specified location
     * @param filePath path to save the file
     */
    void saveWatchListToFilepath(String filePath);

    /**
     * Calls the model to set the users API Key.
     * @param apiKey
     */
    void modelSetAPIKey(String apiKey);
}

