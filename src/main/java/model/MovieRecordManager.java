package model;

import java.io.InputStream;

import com.fasterxml.jackson.databind.json.JsonMapper;

import model.MovieRecord.MRecord;
import net.NetUtils;

public class MovieRecordManager {

    /**
     * Creates a new movie record with the specified title.
     * 
     * @param movieTitle the title of the movie
     * @return a new movie record with the specified title using the API.
     */
    public MRecord createRecord(String movieTitle) {
        try {
            String url = NetUtils.getMovieUrl(movieTitle);
            InputStream inputStream = NetUtils.getUrlContents(url);
            JsonMapper mapper = new JsonMapper();
            MRecord movieRecord = mapper.readValue(inputStream, MRecord.class);
            return movieRecord;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        MovieRecordManager manager = new MovieRecordManager();
        MRecord movieRecord = manager.createRecord("Inception");
        if (movieRecord != null) {
            System.out.println(movieRecord.toString());
        } else {
            System.out.println("Failed to create movie record.");
        }
    }
}


