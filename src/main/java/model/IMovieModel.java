package model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Interface representing a movie record with its details.
 * 
 */
public interface IMovieModel { 

    String DATABASE = "movie.xml";
     
    /**
     * Get the records as a list.
     * 
     * @return the list of records
     */
    List<MRecord> getRecords();

    MRecord getRecord(String title);

    static void writeRecords(List<MRecord> records) {
        // Implementation for writing records to a file or database
    }

    static IMovieModel getInstance() {
        return getInstance(DATABASE);
    }

    static IMovieModel getInstance(String database) {
        // Implementation for getting an instance of the movie model
        return null;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JacksonXmlRootElement(localName = "movie")
    @JsonPropertyOrder({"Title", "Year", "Director", "Actors", "Plot", "Poster", 
        "imdbRating", "Genre", "Runtime", "Country"})
    record MRecord(String Title, String Year, String Director, 
                String Actors, String Plot,String Poster,String imdbRating,
                String Genre,String Runtime,String Country) {
}
}
