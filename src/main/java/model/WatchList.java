package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

import model.IMovieModel.MRecord;

public class WatchList {

    /**
     * The list of movie records in the watch list.
     */
    private final List<MRecord> watchList = new ArrayList<>();

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
    public void removeFromWatchList(MRecord record) {
        if (record != null) {
            watchList.removeIf(r -> r.Title().equals(record.Title()));
        }
    }

    /**
     * Removes a movie record from the watch list by title.
     * @param title the title of the movie to remove
     */
    public List getWatchList() {
        return this.watchList;
    }

    /**
     * Adds a movie record to the watch list by title if it is not already there
     * @param title - the title of the movie to add
     */
    public void addFromRecordsToWatchList(String title) {
        MRecord record = getRecord(title);
        if (record != null && watchList.stream().noneMatch(r -> r.Title().equals(record.Title()))) {
            watchList.add(record);
        }
    }


    /**
     * Retrieves a movie record from the watch list by title.
     * @param title the title of the movie to retrieve
     * @return the movie record, or null if not found
     */
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
     * Sets the rating of a movie in the watch list by title.
     * @param title the title of the movie
     * @param rating the new rating to set
     */
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
                MovieData.saveWatchListToFile(this);
                return;
            }
        }
    }

}
