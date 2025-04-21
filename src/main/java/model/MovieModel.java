package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.NetUtils;

public class MovieModel implements IMovieModel {
    private final MovieList movieList;
    private final WatchList watchList;
    private final ISortandFilter sortFilter; 

    /**
     * The list of movie records.
     */
    private final List<MRecord> records = new ArrayList<>();

    /**
     * The path to the database file, which contains the default set of movies.
     */
    private String databasePath;

    /**
     * Creates a MovieModel with an empty records list and loads records from
     * the default database.
     * @see IMovieModel#DATABASE
     */
    public MovieModel() {
        this.databasePath = databasePath;
        this.movieList = new MovieList(databasePath);
        this.watchList = new WatchList(movieList);
        this.sortFilter = new MovieListSortFilter();
    }

    @Override
    public MRecord getRecord(String title) {
        return movieList.getRecord(title);
    }

    public void addToWatchList(MRecord record) {
        watchList.addToWatchList(record);
    }

    public void addToWatchList(String title) {
        MRecord record = getRecord(title);
        if (record != null) {
            watchList.addToWatchList(record);
        }
    }

    public Stream<MRecord> filterMovies(String filterType, String filterValue) {
        return sortFilter.filterWatchList(filterType, filterValue, movieList, watchList.getWatchList());
    }

    @Override
    public List<MRecord> getRecords() {
        return movieList.getRecords();
    }

    @Override
    public void ApiKeySetter(String apiKey) {
        NetUtils.setAPIKey(apiKey);
    }
}

