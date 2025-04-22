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
    private final String databasePath;

    /**
     * Creates a MovieModel with an empty records list and loads records from
     * the default database.
     * @see IMovieModel#DATABASE
     */
    public MovieModel() {
        this.databasePath = DATABASE;
        this.movieList = new MovieList(databasePath);
        this.watchList = new WatchList(movieList);
        this.sortFilter = new MovieListSortFilter();
    }

    @Override
    public MRecord getRecord(String title) {
        return movieList.getRecord(title);
    }

    @Override
    public List<MRecord> getWatchList() {
        return watchList.getWatchList();
    }

    @Override
    public MRecord getRecordFromWatchList(String title) {
        return watchList.getRecordFromWatchList(title);
    }

    @Override
    public void removeFromWatchList(MRecord record) {
        watchList.removeFromWatchList(record);
    }

    @Override
    public void loadWatchListFromFile() {
        try {
            MovieData.loadWatchListFromFile(watchList);
        } catch (Exception e) {
            System.err.println("Error loading watchlist: " + e.getMessage());
        }
    }

    @Override
    public void loadWatchListFromFile(String filePath) {
        try {
            MovieData.loadWatchListFromFile(watchList);
        } catch (Exception e) {
            System.err.println("Error loading watchlist from file: " + e.getMessage());
        }
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

    @Override
    public Stream<MRecord> filterWatchList(String filterType, String filterValue) {
        return sortFilter.filterWatchList(filterType, filterValue, watchList.getWatchList());
    }

    @Override
    public List<MRecord> sortMovieList(Stream<MRecord> stream, String ascOrDesc, String column) {
        return sortFilter.sortMovieList(stream, ascOrDesc, column);
    }

    @Override
    public List<MRecord> getRecords() {
        return movieList.getRecords();
    }

    @Override
    public void ApiKeySetter(String apiKey) {
        NetUtils.setAPIKey(apiKey);
    }

    @Override
    public void saveWatchListToFile() {
        try {
            MovieData.saveWatchListToFile(watchList);
        } catch (Exception e) {
            System.err.println("Error saving watchlist: " + e.getMessage());
        }
    }
    
    @Override
    public void saveWatchListToFilepath(String filePath, List<MRecord> watchlist) {
        try {
            MovieData.saveWatchListToFilepath(filePath, watchList);
        } catch (Exception e) {
            System.err.println("Error saving watchlist to filepath: " + e.getMessage());
            throw new IllegalArgumentException("Failed to save watchlist: " + e.getMessage());
        }
    }

    @Override
    public void setMovieRating(String title, String rating) {
        MRecord record = getRecord(title);
        if (record != null) {
            watchList.setMovieRating(title, rating);
        } else {
            throw new IllegalArgumentException("Movie not found in watch list: " + title);
        }
    }

    @Override
    public void addFromRecordsToWatchList(String title) {
        MRecord record = getRecord(title);
        if (record != null) {
            watchList.addFromRecordsToWatchList(title);
        } else {
            throw new IllegalArgumentException("Movie not found in records: " + title);
        }
    }
}

