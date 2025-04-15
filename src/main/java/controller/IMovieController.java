package controller;

import java.util.List;
import model.IMovieModel;

/**
 * This interface defines the features expected in the GUI from a controller-like object
 */
public interface IMovieController {
    void searchMovie(String title);
    void addToWatchList(String title);
    void removeFromWatchList(String title);
    void saveWatchList();
    List<IMovieModel.MRecord> getWatchList();
    List<IMovieModel.MRecord> getAllMovies();
    List<IMovieModel.MRecord> sortMovieList(String column, String ascOrDesc);
    void setMyRating(String title, String rating);
    String getMyRating(String title);
    List<IMovieModel.MRecord> filterMovieList(String field, String criteria);

    /**
     * Calls the model to set the users API Key.
     * @param apiKey
     */
    void modelSetAPIKey(String apiKey);
}

