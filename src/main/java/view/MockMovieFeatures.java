package view;

import controller.IController;
import controller.IMovieController;
import model.IMovieModel;
import java.util.ArrayList;
import java.util.List;

public class MockMovieFeatures implements IMovieController {

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
     * Calls the model to set the users API Key.
     * @param apiKey
     */
    public void modelSetAPIKey(String apiKey){

    };

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

    @Override
    public void saveWatchList() {
        System.out.println("Pretending to save watchlist.");
    }

    @Override
    public void setMyRating(String title, String rating) {
        System.out.println("Pretending to rate movie: " + title + " as " + rating);
    }

    @Override
    public String getMyRating(String title) {
        return null;
    }

    @Override
    public List<IMovieModel.MRecord> filterMovieList(String field, String criteria) {
        System.out.println("Pretending to filter movies by " + field + " with " + criteria);
        return new ArrayList<>();
    }
}
