package model;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Interface representing a movie record with its details.
 */
public interface IMovieModel {

    String DATABASE = "data/movie.json";
    String WATCHLIST_DATABASE = "data/myWatchList.json";
     
    /**
     * Get the records as a list.
     *
     * @return the list of records
     */
    List<MRecord> getRecords();

    MRecord getRecord(String title);

    /**
     * Write the records to the specified output stream in JSON format.
     *
     * @param records the collection of records to write
     * @param out     the output stream to write to
     */
    static void writeRecords(Collection<MRecord> records, OutputStream out) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(out, records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the watch list from a file.
     */
    void loadWatchListFromFile();

    /**
     * Sets the rating of a movie in the watch list by title.
     * @param title the title of the movie
     * @param rating the new rating to set
     */
    void setMovieRating(String title, String rating);

    /**
     * Saves the watch list to a file in JSON format.
     *
     */
    void saveWatchListToFile();

    /**
     * Saves the watch list to a file at the specified file path.
     * @param filePath the path to save the file
     */
    void saveWatchListToFilepath(String filePath);

    /**
     * Removes a movie record from the watch list by title.
     * @param title the title of the movie to remove
     */
    List<MRecord> getWatchList();

    /**
     * Retrieves a movie record from the watch list by title.
     * @param title the title of the movie to retrieve
     * @return the movie record, or null if not found
     */
    MRecord getRecordFromWatchList(String watchListTitle);

    /**
     * Removes a movie record from the watch list.
     * @param record the movie record to remove
     */
    void removeFromWatchList(MRecord record);


    /**
     * Sets the API Key for core functionality.
     * @param apiKey
     */
    void ApiKeySetter(String apiKey);


    /**
     * Adds a movie record to the watch list by title if it is not already there.
     * @param title
     */
    void addFromRecordsToWatchList(String title);

    /**
     * Method to convert a record to JSON string.
     *
     * @param record the movie record
     * @return JSON formatted string
     */
    static String exportRecordAsJson(MRecord record) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(record);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets an instance of the movie model.
     * * @return an instance of IMovieModel
     */
    static IMovieModel getInstance() {
        return new MovieModel();
    }

    /**
     * Gets an instance of the movie model with a specified database.
     * @param database the database to use
     * @return an instance of IMovieModel
     */
    static IMovieModel getInstance(String database) {
        try {
            return new MovieModel(database);
        } catch (Exception e) {
            return new MovieModel();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JacksonXmlRootElement(localName = "movie")
    @JsonPropertyOrder({"Title", "Year", "Director", "Actors", "Plot", "Poster",
            "imdbRating", "Genre", "Runtime", "Country", "Response"})
    record MRecord(String Title, String Year, String Director,
                   String Actors, String Plot, String Poster, String imdbRating,
                   String Genre, String Runtime, String Country, String Response) {
    }
}
