package view;

import java.util.List;
import model.IMovieModel;

/**
 * This interface defines the features expected in the GUI from a controller-like object
 */
public interface IMovieFeatures {
    void searchMovie(String title);
    void addToWatchList(String title);
    void removeFromWatchList(String title);
    List<IMovieModel.MRecord> getWatchList();
    List<IMovieModel.MRecord> getAllMovies();
    List<IMovieModel.MRecord> sortMovieList(String column, String ascOrDesc);
}

