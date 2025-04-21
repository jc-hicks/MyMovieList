package model;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.json.JsonMapper;

import model.IMovieModel.MRecord;
import net.NetUtils;

public class MovieList {

    /**
     * Adds a movie record to the records list if it does not already exist.
     * @param record
     * 
     */
    public void addRecord(MRecord record) {
        if (record != null && records.stream().noneMatch(r -> r.Title().equals(record.Title()))) {
            records.add(record);
            try {
                saveToDatabase(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a movie record by title. If the record is not found in the
     * local list, it fetches it from the web and adds it to the list.
     *
     * @param title the title of the movie to retrieve
     * @return the movie record, or null if not found.
     */
    @Override
    public MRecord getRecord(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        for (MRecord record : records) {
            if (record.Title().equals(title)) {
                return record;
            }
        }
        try {
            String url = NetUtils.getMovieUrl(title);
            System.out.println("Trying API UR: " + url); // seeing why not pulling from API
            try (InputStream inputStream = NetUtils.getUrlContents(url)) {
                JsonMapper mapper = new JsonMapper();
                MRecord newRecord = mapper.readValue(inputStream, MRecord.class);
                if ("True".equalsIgnoreCase(newRecord.Response())) {  // RUBEN: if response is 'null' will skip record, movie.json has "Title: Inception, Response: null" will not show in GUI
                  addRecord(newRecord);                     // changed from (newRecord.Response().equals("True")) -> ("True".equalsIgnoreCase(newRecord.Response()))
                  return newRecord;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
