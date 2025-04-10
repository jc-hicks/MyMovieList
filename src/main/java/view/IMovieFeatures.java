package view;

import java.util.List;
import model.IMovieModel;

/**
 * This interface defines the features expected in the GUI from a controller-like object
 */
public interface IMovieFeatures {
    void searchMNovie(String title);
    void addToWatchList(String title);
    List<IMovieModel.MRecord> getWatchList();
    List<IMovieModel.MRecord> getAllMovies();
}

