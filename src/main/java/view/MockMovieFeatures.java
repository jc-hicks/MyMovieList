package view;

import model.IMovieModel;
import java.util.ArrayList;
import java.util.List;

public class MockMovieFeatures implements IMovieFeatures{

    /**
     * Method for GUI functionality in searching records
     * @param title
     */
    @Override
    public void searchMovie(String title) {
        System.out.println("Pretending to search for: " + title);
    }

    /**
     * Method for GUI functionality in adding movie objects to watchlist
     * @param title
     */
    @Override
    public void addToWatchList(String title) {
        System.out.println("Pretending to add to watchlist: " + title);
    }

    /**
     * Method for GUI functionality in returning watchlist array
     * @return
     */
    @Override
    public List<IMovieModel.MRecord> getWatchList() {
        System.out.println("Returning fake watchlist");
        return new ArrayList<>();
    }

    /**
     * Method for GUI functionality In returning all movies in record
     * @return
     */
    @Override
    public List<IMovieModel.MRecord> getAllMovies() {
        System.out.println("Returning fake list of movies");
        return new ArrayList<>();
    }

    /**
     * Method for GUI functionality in removing a movie from the user's watchlist
     * @param title
     */
    @Override
    public void removeFromWatchList(String title) {
        System.out.println("Pretending to remove from watchlist: " + title);
    }


    @Override
    public List<IMovieModel.MRecord> sortMovieList(String column, String ascOrDesc) {
        System.out.println("Pretending to sort by " + column + " in " + ascOrDesc + " order.");
        return getAllMovies();
    }

}
