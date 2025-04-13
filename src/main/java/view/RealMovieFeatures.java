package view;


import model.IMovieModel;
import model.MovieModel;
import java.util.List;


/**
 * This class connects the GUI to the real data model for add/remove/search/sort functionality
 */
public class RealMovieFeatures implements IMovieFeatures {

    private final IMovieModel model;

    public RealMovieFeatures() {
        this.model = IMovieModel.getInstance();  // Loads from movie.json
    }

    public void loadWatchlistOnStartup() {
        ((MovieModel) model).loadWatchListFromFile();
    }

    @Override
    public void searchMovie(String title) {
        model.getRecord(title);   // Get and add to record list
    }

    @Override
    public void addToWatchList(String title) {
        ((MovieModel) model).addFromRecordsToWatchList(title);
    }

    @Override
    public void removeFromWatchList(String title) {
        ((MovieModel) model).removeFromWatchList(
                ((MovieModel) model).getRecordFromWatchList(title)
        );
    }

    @Override
    public List<IMovieModel.MRecord> getWatchList() {
        return ((MovieModel) model).getWatchList();
    }

    @Override
    public List<IMovieModel.MRecord> getAllMovies() {
        return model.getRecords();  // Uses IMovieModel interface
    }

    @Override
    public List<IMovieModel.MRecord> sortMovieList(String column, String ascOrDesc) {
        return ((MovieModel) model).sortMovieList(
                model.getRecords().stream(),
                ascOrDesc,
                column.toLowerCase()
        );
    }

    @Override
    public void saveWatchList() {
        ((MovieModel) model).saveWatchListToFile();
    }

    @Override
    public void setMyRating(String title, String rating) {
        ((MovieModel) model).setMovieRating(title, rating);
    }

    @Override
    public String getMyRating(String title) {
        IMovieModel.MRecord record = ((MovieModel) model).getRecordFromWatchList(title);
        return (record != null) ? record.imdbRating() : "N/A";
    }

    @Override
    public List<IMovieModel.MRecord> filterMovieList(String field, String criteria) {
        return ((MovieModel) model).filterWatchList(field, criteria).toList();
    }

}
