package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Interface representing a movie record with its details.
 * 
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

    static MovieModel writeRecords(List<MRecord> records) {
        try {
            JsonMapper mapper = new JsonMapper();
            mapper.writeValue(new File(DATABASE), records);

            return new MovieModel(records);
        } catch (Exception e) {
            e.printStackTrace();
            return new MovieModel(new ArrayList<>()); // Return empty list on error
        }
    }


    static IMovieModel getInstance() {
        return getInstance(DATABASE);
    }

    static IMovieModel getInstance(String database) {
        try {
            JsonMapper mapper = new JsonMapper();
            InputStream existingRecords = new FileInputStream(database);
            List<MRecord> movieRecords = mapper.readValue(existingRecords, new TypeReference<List<MRecord>>() { });
            return new MovieModel(movieRecords);
        } catch (Exception e) {
            e.printStackTrace();
            return new MovieModel(new ArrayList<>()); // Return empty list instead of null
        }
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
