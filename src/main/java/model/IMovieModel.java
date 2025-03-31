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
    } // <-- Missing brace added here

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
            e.printStackTrace();
            return new MovieModel(List.of());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JacksonXmlRootElement(localName = "movie")
    @JsonPropertyOrder({"Title", "Year", "Director", "Actors", "Plot", "Poster",
            "imdbRating", "Genre", "Runtime", "Country"})
    record MRecord(String Title, String Year, String Director,
                   String Actors, String Plot, String Poster, String imdbRating,
                   String Genre, String Runtime, String Country) {
    }
}
