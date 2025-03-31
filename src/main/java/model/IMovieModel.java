package model;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    /**
     * Write the records to the specified output stream in JSON format.
     * 
     * @param records the collection of records to write
     * @param out the output stream to write to
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

    // Method to convert a record to JSON string
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
            return new MovieModel(new ArrayList<>());
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
