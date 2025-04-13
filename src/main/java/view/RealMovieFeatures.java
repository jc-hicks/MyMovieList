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
        System.out.println("Pretending to save watchlist.");
    }

    @Override
    public void setMyRating(String title, String rating) {
        System.out.println("Feature not implemented: setMyRating(" + title + ", " + rating + ")");
    }
}
