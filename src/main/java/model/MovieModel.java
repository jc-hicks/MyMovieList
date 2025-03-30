package model;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.ArrayList;
import java.util.stream.Stream;
import net.NetUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class MovieModel implements IMovieModel {

    private final List<MRecord> records;

    public MovieModel(List<MRecord> records) {
        this.records = records;
    }

    @Override
    public MRecord getRecord(String title) {
        // Check the records first
        for (MRecord record : records) {
            if (record.Title().equals(title)) {
                return record;
            }
        }
        try {
            String url = NetUtils.getMovieUrl(title); // Get the URL
            try (InputStream inputStream = NetUtils.getUrlContents(url)) {
                JsonMapper mapper = new JsonMapper();
                return mapper.readValue(inputStream, MRecord.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Method to filter through the current list of movies based on passed criteria.
     * @param filterType
     * @param filterValue
     * @return Stream of movie records
     */
    public Stream<MRecord> filterWatchList(String filterType, String filterValue) {

        switch (filterType.toLowerCase()) {
            case "title":
                return records.stream().filter(m -> m.Title().equalsIgnoreCase(filterValue));
            case "year":
                return records.stream().filter(m -> m.Year().equals(filterValue));
            case "director":
                return records.stream().filter(m -> m.Director().toLowerCase().contains(filterValue.toLowerCase()));
            case "genre":
                return records.stream().filter(m -> m.Genre().toLowerCase().contains(filterValue.toLowerCase()));
            /*
            case "actors":
                return records.stream().filter(m -> m.Actors().toLowerCase().contains(filterValue.toLowerCase()));
            case "rating":
                return records.stream().filter(m -> m.imdbRating().equals(filterValue));
            case "runtime":
                return records.stream().filter(m -> m.Runtime().equals(filterValue));
            case "country":
                return records.stream().filter(m -> m.Country().equalsIgnoreCase(filterValue));
             */
            default:
                return records.stream(); // Return all records if filter type is not recognized
        }
    }

    @Override
    public List<MRecord> getRecords() {
        return List.copyOf(records);
    }


    /**
     * Main testing method for JSON file
     */
    public static void main(String[] args) {

        List<MRecord> records = new ArrayList<>();
        IMovieModel movieModel = new MovieModel(new ArrayList<>());

        records.add(movieModel.getRecord("The Matrix"));
        records.add(movieModel.getRecord("Inception"));
        records.add(movieModel.getRecord("City of God"));
        records.add(movieModel.getRecord("Rango"));
        records.add(movieModel.getRecord("Titanic"));

        IMovieModel.writeRecords(records);

    }
}
