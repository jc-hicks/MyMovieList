package model;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
     * Method for sorting movies based on user passed column and ordering
     * criteria.
     *
     * @param movieStream
     * @param ascOrDesc
     * @param column
     * @return List of movie records sorted in the appropriate order.
     */
    List<MRecord> sortMovieList(Stream<MRecord> movieStream, String ascOrDesc, String column);

    /**
     * Method to filter through the current list of movies based on passed
     * criteria.
     *
     * @param filterType
     * @param filterValue
     * @return Stream of movie records
     */
    Stream<MRecord> filterWatchList(String filterType, String filterValue);

    /**
     * Saves the watch list to a file in JSON format.
     *
     */
    void saveWatchListToFile();

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

    static IMovieModel getInstance() {
        return new MovieModel();
    }

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
