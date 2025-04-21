package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Integer.parseInt;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

import net.NetUtils;

public class MovieModel implements IMovieModel {

    /**
     * The list of movie records.
     */
    private final List<MRecord> records = new ArrayList<>();

    /**
     * The list of movie records in the watch list.
     */
    private final List<MRecord> watchList = new ArrayList<>();

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
        this(DATABASE);
    }

    /**
     * Creates a MovieModel with an empty records list and loads records from
     * the specified database.
     * @param databasePath the path to the database file
     */
    public MovieModel(String databasePath) {
        this.databasePath = databasePath;
        File file = new File(databasePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error creating database file: " + e.getMessage(), e);
            }
        } else { 
            try {
                loadFromDatabase(databasePath);
            } catch (IOException e) {
                throw new RuntimeException("Error loading database: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Loads records from the specified database file and adds them to the
     * records list.
     * @param databasePath the path to the database file
     */
    private void loadFromDatabase(String databasePath) throws IOException {
        File file = new File(databasePath);
        
        if (file.length() == 0) {
            return;
        }
        
        try (InputStream existingRecords = new FileInputStream(file)) {
            JsonMapper mapper = new JsonMapper();
            List<MRecord> movieRecords = mapper.readValue(existingRecords, 
                    new TypeReference<List<MRecord>>() {});
            records.addAll(movieRecords);
        } catch (Exception e) {
            throw new IOException("Error loading records from database: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a movie record to the records list if it does not already exist.
     * @param record
     * 
     */
    public void addRecord(MRecord record) {
        if (record != null && records.stream().noneMatch(r -> r.Title().equals(record.Title()))) {
            records.add(record);
            try {
                saveToDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a movie record by title. If the record is not found in the
     * local list, it fetches it from the web and adds it to the list.
     *
     * @param title the title of the movie to retrieve
     * @return the movie record, or null if not found.
     */
    @Override
    public MRecord getRecord(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        for (MRecord record : records) {
            if (record.Title().equals(title)) {
                return record;
            }
        }
        try {
            String url = NetUtils.getMovieUrl(title);
            System.out.println("Trying API UR: " + url); // seeing why not pulling from API
            try (InputStream inputStream = NetUtils.getUrlContents(url)) {
                JsonMapper mapper = new JsonMapper();
                MRecord newRecord = mapper.readValue(inputStream, MRecord.class);
                if ("True".equalsIgnoreCase(newRecord.Response())) {  // RUBEN: if response is 'null' will skip record, movie.json has "Title: Inception, Response: null" will not show in GUI
                  addRecord(newRecord);                     // changed from (newRecord.Response().equals("True")) -> ("True".equalsIgnoreCase(newRecord.Response()))
                  return newRecord;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Sets the API Key for core functionality
     * @param apiKey
     */
    @Override
    public void ApiKeySetter(String apiKey) {
        NetUtils.setAPIKey(apiKey);
    }

    /**
     * Saves all records to the default database file in JSON format.
     *
     */
    private void saveToDatabase() throws IOException {
        if (this.databasePath != null) {
            saveToDatabase(this.databasePath);
        } else {
            saveToDatabase(DATABASE);
        }
    }

    /**
     * Saves all records to the database file in JSON format
     * 
     * @param filePath the path to the database file
     */
    private void saveToDatabase(String filePath) throws IOException {
        File dbFile = new File(filePath);
        
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            throw new IOException("Cannot save to database: directory " + 
                    parentDir.getAbsolutePath() + " does not exist");
        }
        
        try (OutputStream out = new FileOutputStream(dbFile)) {
            IMovieModel.writeRecords(records, out);
        } catch (IOException e) {
            throw new IOException("Error saving records to database: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a movie record to the watch list if it does not already exist.
     * @param record the movie record to add
     */
    public void addToWatchList(MRecord record) {
        if (record != null && watchList.stream().noneMatch(r -> r.Title().equals(record.Title()))) {
            watchList.add(record);
        }
    }

    /**
     * Removes a movie record from the watch list.
     * @param record the movie record to remove
     */
    @Override
    public void removeFromWatchList(MRecord record) {
        if (record != null) {
            watchList.removeIf(r -> r.Title().equals(record.Title()));
        }
    }

    /**
     * Removes a movie record from the watch list by title.
     * @param title the title of the movie to remove
     */
    @Override
    public List<MRecord> getWatchList() {
        return this.watchList;
    }

    /**
     * Adds a movie record to the watch list by title if it is not already there
     * @param title - the title of the movie to add
     */
    @Override
    public void addFromRecordsToWatchList(String title) {
        MRecord record = getRecord(title);
        if (record != null && watchList.stream().noneMatch(r -> r.Title().equals(record.Title()))) {
            watchList.add(record);
        }
    }

    /**
     * Saves the watch list to a file in JSON format.
     *
     */
    @Override
    public void saveWatchListToFile() {
        String filePath = IMovieModel.WATCHLIST_DATABASE;
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Invalid file path for watch list");
        }
        try (OutputStream out = new FileOutputStream(filePath)) {
            IMovieModel.writeRecords(watchList, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the watch list to a file at the specified file path.
     * @param filePath the path to save the file
     */
    @Override
    public void saveWatchListToFilepath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Invalid file path for watch list");
        }
        try (OutputStream out = new FileOutputStream(filePath)) {
            IMovieModel.writeRecords(watchList, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the watch list from a file.
     */
    @Override
    public void loadWatchListFromFile() {
        String filePath = IMovieModel.WATCHLIST_DATABASE;
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Invalid file path for watch list");
        }
        try (InputStream in = new FileInputStream(filePath)) {
            JsonMapper mapper = new JsonMapper();
            List<MRecord> watchListRecords = mapper.readValue(in, 
                    new TypeReference<List<MRecord>>() {});
            watchList.addAll(watchListRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the rating of a movie in the watch list by title.
     * @param title the title of the movie
     * @param rating the new rating to set
     */
    @Override
    public void setMovieRating(String title, String rating) {
        double numRating = Double.parseDouble(rating);
        if (numRating > 10 || numRating < 0) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }
        if (title == null || title.isEmpty() || rating == null || rating.isEmpty()) {
            throw new IllegalArgumentException("Title and rating cannot be null or empty");
        }
        for (MRecord record : watchList) {
            if (record.Title().equals(title)) {
                MRecord swapRecord = new MRecord(record.Title(), record.Year(), record.Director(), record.Actors(), record.Plot(), record.Poster(), rating, record.Genre(), record.Runtime(), record.Country(), record.Response());
                watchList.remove(record);
                watchList.add(swapRecord);
                try {
                    saveToDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    /**
     * Retrieves a movie record from the watch list by title.
     * @param title the title of the movie to retrieve
     * @return the movie record, or null if not found
     */
    @Override
    public MRecord getRecordFromWatchList(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        for (MRecord record : watchList) {
            if (record.Title().equals(title)) {
                return record;
            }
        }
        System.out.println("Movie not found in watch list.");
        return null;
    }


    /**
     * Utilized to get listing of all ratings of current movies in watchlist.
     * @return List of movie ratings
     */
    public List<Double> getMovieDistributions(){

        List<Double> ratings = new ArrayList<>();

        for (MRecord record : records) {
            ratings.add(Double.parseDouble(record.imdbRating()));
        }

        return ratings.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Utilized to get listing of all ratings of current movies in the list.
     * @return List of movies
     */
    @Override
    public List<MRecord> getRecords() {
        return records;
    }

}

