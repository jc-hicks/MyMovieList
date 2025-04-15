package controller;


import model.IMovieModel;
import model.MovieModel;
import java.util.List;


/**
 * This class connects the GUI to the real data model for add/remove/search/sort functionality
 */
public class Controller implements IMovieController {

    private final IMovieModel model;

    /**
     * Constructor for the Controller taking in an IMovieModel.
     * @param model
     */
    public Controller(IMovieModel model) {
        this.model = model;  // Loads from movie.json
    }


    public void loadWatchlistOnStartup() {
        ((MovieModel) model).loadWatchListFromFile();
    }


    /**(
     * Searches record for specific title
     * @param title
     */
    @Override
    public void searchMovie(String title) {
        System.out.println("searchMovie called with: " + title);
        IMovieModel.MRecord result = model.getRecord(title);
    }

    /**
     * Adds record to personal watchlist
     * @param title
     */
    @Override
    public void addToWatchList(String title) {
        model.addFromRecordsToWatchList(title);
    }


    /**
     * Removes record from personal watchlist
     * @param title
     */
    @Override
    public void removeFromWatchList(String title) {
        ((MovieModel) model).removeFromWatchList(
                ((MovieModel) model).getRecordFromWatchList(title)
        );
    }


    /**
     * Pulls current watchlist
     * @return json of movies added to list
     */
    @Override
    public List<IMovieModel.MRecord> getWatchList() {
        return ((MovieModel) model).getWatchList();
    }


    /**
     * Accesses and displays record of all 200 movies
     * @return list of 200 movie records
     */
    @Override
    public List<IMovieModel.MRecord> getAllMovies() {
        return model.getRecords();  // Uses IMovieModel interface
    }


    /**
     * Sorts movie list by ascending or descending order
     * @param column column is set by year, title, director, rating
     * @param ascOrDesc sorts in asc/desc
     * @return sorted list
     */
    @Override
    public List<IMovieModel.MRecord> sortMovieList(String column, String ascOrDesc) {
        return ((MovieModel) model).sortMovieList(
                model.getRecords().stream(),
                ascOrDesc,
                column.toLowerCase()
        );
    }


    /**
     * Saves watchlist as json file and calls current version
     */
    @Override
    public void saveWatchList() {
        ((MovieModel) model).saveWatchListToFile();
    }


    /**
     * right click option to add personal rating to watchlist json
     * @param title title of the movie record
     * @param rating rating int 1-10
     */
    @Override
    public void setMyRating(String title, String rating) {
        ((MovieModel) model).setMovieRating(title, rating);
    }


    /**
     * Accesses set personal set rating
     * @param title title of the movie record
     * @return returns int 10
     */
    @Override
    public String getMyRating(String title) {
        IMovieModel.MRecord record = ((MovieModel) model).getRecordFromWatchList(title);
        return (record != null) ? record.imdbRating() : "N/A";
    }


    /**
     * Filters list of movies accessed from record or API
     * @param field string of movie records
     * @param criteria include title, year, director, rating
     * @return filtered list.
     */
    @Override
    public List<IMovieModel.MRecord> filterMovieList(String field, String criteria) {
        return ((MovieModel) model).filterWatchList(field, criteria).toList();
    }

}
