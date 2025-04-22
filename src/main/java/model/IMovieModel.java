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

    /**
     * The default database file path for movie records.
     */
    String DATABASE = "data/movie.json";

    /**
     * The default database file path for the watch list.
     */
    String WATCHLIST_DATABASE = "data/myWatchList.json";
     
    /**
     * Get the records as a list.
     *
     * @return the list of records
     */
    List<MRecord> getRecords();

    /** 
     * Get a single record by title.
     */
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
     * Sets the API Key for core functionality.
     * @param apiKey
     */
    void ApiKeySetter(String apiKey);

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
            return new MovieModel();
        } catch (Exception e) {
            return new MovieModel();
        }
    }

    /**
     * Retrieves a movie record from the watch list by title.
     * @param title the title of the movie to retrieve
     * @return the movie record, or null if not found
     */
    MRecord getRecordFromWatchList(String title);

    /**
     * Adds a movie record to the watch list by title if it is not already there.
     * @param title
     */
    void addFromRecordsToWatchList(String title);

    /**
     * Removes a movie record from the watch list.
     * @param record the movie record to remove
     */
    void removeFromWatchList(MRecord movie);

    /**
     * Method for sorting movies based on user passed column and ordering
     * criteria.
     *
     * @param movieStream
     * @param ascOrDesc
     * @param column
     * @return List of movie records sorted in the appropriate order.
     */
    List<MRecord> sortMovieList(Stream<MRecord> stream, String ascOrDesc, String column);

     /**
     * Saves the watch list to a file in JSON format.
     *
     */
    void saveWatchListToFile();

    /**
     * Saves the watch list to a file at the specified file path.
     * @param filePath the path to save the file
     */
    void saveWatchListToFilepath(String filePath, List<MRecord> watchList);

    /**
     * Sets the rating of a movie in the watch list by title.
     * @param title the title of the movie
     * @param rating the new rating to set
     */
    void setMovieRating(String title, String rating);

     /**
     * Method to filter through the current list of movies based on passed
     * criteria.
     *
     * @param filterType
     * @param filterValue
     * @return Stream of movie records
     */
    Stream<MRecord> filterWatchList(String field, String criteria);

    /**
     * Removes a movie record from the watch list by title.
     * @param title the title of the movie to remove
     */
    List<MRecord> getWatchList();

    /**
     * Loads the watch list from a file at the specified file path.
     * @param filePath
     */
    void loadWatchListFromFile(String filePath);

    /**
     * Loads the watch list from the default file.
     */
    void loadWatchListFromFile();
    
    /**
     * Movie record class representing a movie with various attributes.
     * This class is used for JSON serialization and deserialization.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JacksonXmlRootElement(localName = "movie")
    @JsonPropertyOrder({"Title", "Year", "Director", "Actors", "Plot", "Poster",
            "imdbRating", "Genre", "Runtime", "Country", "Response"})
    record MRecord(String Title, String Year, String Director,
                   String Actors, String Plot, String Poster, String imdbRating,
                   String Genre, String Runtime, String Country, String Response) {
    }
}
