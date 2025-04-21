package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import model.IMovieModel.MRecord;

public class MovieData {

    /**
     * Write the records to the specified output stream in JSON format.
     *
     * @param records the collection of records to write
     * @param out     the output stream to write to
     */
    public static void writeRecords(Collection<MRecord> records, OutputStream out) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(out, records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads records from the specified database file and adds them to the
     * records list.
     * @param databasePath the path to the database file
     */
    public static void loadFromDatabase(String databasePath, MovieList movieList) throws IOException {
        File file = new File(databasePath);
        if (file.length() == 0) {
            return;
        }
        try (InputStream existingRecords = new FileInputStream(file)) {
            JsonMapper mapper = new JsonMapper();
            List<MRecord> movieRecords = mapper.readValue(existingRecords, new TypeReference<List<MRecord>>() {
            });
            movieList.getRecords().addAll(movieRecords);
        } catch (Exception e) {
            throw new IOException("Error loading records from database: " + e.getMessage(), e);
        }
    }

    /**
     * Saves all records to the default database file in JSON format.
     *
     */
    public static void saveToDatabase(MovieList movieList) throws IOException {
        if (movieList.getDatabasePath() != null) {
            saveToDatabase(movieList.getDatabasePath(), movieList.getRecords());
        } else {
            String filepath = IMovieModel.DATABASE;
            saveToDatabase(filepath, movieList.getRecords());
        }
    }

    /**
     * Saves all records to the database file in JSON format
     *
     * @param filePath the path to the database file
     */
    private static void saveToDatabase(String filePath, List<MRecord> records) throws IOException {
        File dbFile = new File(filePath);
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            throw new IOException("Cannot save to database: directory " + parentDir.getAbsolutePath() + " does not exist");
        }
        try (OutputStream out = new FileOutputStream(dbFile)) {
            IMovieModel.writeRecords(records, out);
        } catch (IOException e) {
            throw new IOException("Error saving records to database: " + e.getMessage(), e);
        }
    }

    /**
     * Saves the watch list to a file in JSON format.
     *
     */
    public static void saveWatchListToFile(WatchList watchList) {
        String filePath = IMovieModel.WATCHLIST_DATABASE;
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Invalid file path for watch list");
        }
        try (OutputStream out = new FileOutputStream(filePath)) {
            IMovieModel.writeRecords(watchList.getWatchList(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the watch list to a file at the specified file path.
     * @param filePath the path to save the file
     */
    public static void saveWatchListToFilepath(String filePath, WatchList watchList) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Invalid file path for watch list");
        }
        try (OutputStream out = new FileOutputStream(filePath)) {
            IMovieModel.writeRecords(watchList.getWatchList(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the watch list from a file.
     */
    public static void loadWatchListFromFile(WatchList watchList) {
        String filePath = IMovieModel.WATCHLIST_DATABASE;
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Invalid file path for watch list");
        }
        try (InputStream in = new FileInputStream(filePath)) {
            JsonMapper mapper = new JsonMapper();
            List<MRecord> watchListRecords = mapper.readValue(in, new TypeReference<List<MRecord>>() {
            });
            watchList.getWatchList().addAll(watchListRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
