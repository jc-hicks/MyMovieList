package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

import net.NetUtils;

public class MovieModel implements IMovieModel {

    private final List<MRecord> records;

    /**
     * Creates a MovieModel with an empty records list and loads records from the default database.
     */
    public MovieModel() {
        this(DATABASE);
    }

    /**
     * Creates a MovieModel with an empty records list and loads records from the specified database.
     */
    public MovieModel(String databasePath) {
        this.records = new ArrayList<>();
        loadFromDatabase(databasePath);
    }

    /**
     * Creates a MovieModel with the provided records.
     */
    public MovieModel(List<MRecord> records) {
        this.records = new ArrayList<>(records);
    }
    
    /**
     * Loads records from the specified database file and adds them to the records list.
     */
    private void loadFromDatabase(String databasePath) {
        try {
            JsonMapper mapper = new JsonMapper();
            InputStream existingRecords = new FileInputStream(databasePath);
            List<MRecord> movieRecords = mapper.readValue(existingRecords, new TypeReference<List<MRecord>>() { });
            records.addAll(movieRecords);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * Retrieves a movie record by title. If the record is not found in the local list, it fetches it from the web and adds it to the list.
     * 
     * @param title the title of the movie to retrieve
     * @return the movie record, or null if not found.
     */
    @Override
    public MRecord getRecord(String title) {
        for (MRecord record : records) {
            if (record.Title().equals(title)) {
                return record;
            }
        }
        try {
            String url = NetUtils.getMovieUrl(title);
            try (InputStream inputStream = NetUtils.getUrlContents(url)) {
                JsonMapper mapper = new JsonMapper();
                MRecord newRecord = mapper.readValue(inputStream, MRecord.class);

                if (newRecord != null && records.stream().noneMatch(record -> record.Title().equals(newRecord.Title()))) {
                    records.add(newRecord);
                    saveToDatabase();
                }
                return newRecord;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves all records to the database file in JSON format
     */
    private void saveToDatabase() {
        try (OutputStream out = new FileOutputStream(new File(DATABASE))) {
            IMovieModel.writeRecords(records, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MRecord> getRecords() {
        return records;
    }

    public static void main(String[] args) {
        IMovieModel movieModel = IMovieModel.getInstance();
        List<MRecord> records = movieModel.getRecords();
        System.out.println("Total records: " + records.size());
        if (!records.isEmpty()) {
            System.out.println("First record: " + records.get(0).toString());
            String jsonOutput = IMovieModel.exportRecordAsJson(records.get(0));
            System.out.println("\nSample JSON output of first record:");
            System.out.println(jsonOutput);
        }
        
        MRecord newRecord = movieModel.getRecord("Taylor Swift");
        
        if (newRecord != null) {
            System.out.println("Successfully retrieved: " + newRecord.Title());
        } else {
            System.out.println("Movie not found or error occurred");
        }
        
        // Print final state
        records = movieModel.getRecords();
        System.out.println("\nFinal total records: " + records.size());
    }
}
