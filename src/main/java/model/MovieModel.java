package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

import net.NetUtils;

public class MovieModel implements IMovieModel {

    private final List<MRecord> records = new ArrayList<>();

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
        loadFromDatabase(databasePath);
    }

    /**
     * Loads records from the specified database file and adds them to the records list.
     */
    private void loadFromDatabase(String databasePath) {
        File file = new File(databasePath);
    
        try {
            if (!file.exists()) {
                file.createNewFile();
                try (OutputStream out = new FileOutputStream(file)) {
                    out.write("[]".getBytes()); 
                }
            }
    
            if (file.length() == 0) {
                return;
            }
    
            try (InputStream existingRecords = new FileInputStream(file)) {
                JsonMapper mapper = new JsonMapper();
                List<MRecord> movieRecords = mapper.readValue(existingRecords, new TypeReference<List<MRecord>>() {});
                records.addAll(movieRecords);
            }
        } catch (Exception e) {
            System.err.println("Error loading records from database: " + e.getMessage());
        }
    }

    public void addRecord(MRecord record) {
        if (record != null && records.stream().noneMatch(r -> r.Title().equals(record.Title()))) {
            records.add(record);
            saveToDatabase();
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
        if (title == null) {
            return null;
        }
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
                addRecord(newRecord);
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


  /**
   * Method to filter through the current list of movies based on passed criteria.
   *
   * @param filterType
   * @param filterValue
   * @return Stream of movie records
   */
  public Stream<MRecord> filterWatchList(String filterType, String filterValue) {

    if (filterType == null || filterValue == null) {
      throw new IllegalArgumentException("filterType and value cannot be null");
    }

    switch (filterType.toLowerCase()) {
      case "title":
        return records.stream().filter(m -> m.Title().equalsIgnoreCase(filterValue));
      case "year":
        List<String> yearCommands = Arrays.asList(filterValue.split(" "));
        if (yearCommands.size() > 1) {
          String filterOperation = yearCommands.get(0);
          return switch (filterOperation) {
            case "=" -> records.stream().filter(m -> m.Year().equals(yearCommands.get(1)));
            case ">" -> records.stream().filter(m -> parseInt(m.Year()) > parseInt(yearCommands.get(1)));
            case "<" -> records.stream().filter(m -> parseInt(m.Year()) < parseInt(yearCommands.get(1)));
            case ">=" -> records.stream().filter(m -> parseInt(m.Year()) >= parseInt(yearCommands.get(1)));
            case "<=" -> records.stream().filter(m -> parseInt(m.Year()) <= parseInt(yearCommands.get(1)));
            default -> throw new IllegalStateException("Unexpected year filter value: " + yearCommands.get(1));
          };
        }
      case "director":
        return records.stream().filter(m -> m.Director().toLowerCase().contains(filterValue.toLowerCase()));
      case "genre":
        return records.stream().filter(m -> m.Genre().toLowerCase().contains(filterValue.toLowerCase()));
      case "actors":
        return records.stream().filter(m -> m.Actors().toLowerCase().contains(filterValue.toLowerCase()));
      case "rating":
        List<String> ratingCommands = Arrays.stream(filterValue.split(" ")).toList();
        if (ratingCommands.size() > 1) {
          String filterOperation = ratingCommands.get(0);
          return switch (filterOperation) {
            case "=" -> records.stream().filter(m -> m.imdbRating().equals(ratingCommands.get(1)));
            case ">" -> records.stream()
                .filter(m -> Double.parseDouble(m.imdbRating()) > Double.parseDouble(ratingCommands.get(1)));
            case "<" -> records.stream()
                .filter(m -> Double.parseDouble(m.imdbRating()) < Double.parseDouble(ratingCommands.get(1)));
            case ">=" -> records.stream()
                .filter(m -> Double.parseDouble(m.imdbRating()) >= Double.parseDouble(ratingCommands.get(1)));
            case "<=" -> records.stream()
                .filter(m -> Double.parseDouble(m.imdbRating()) <= Double.parseDouble(ratingCommands.get(1)));
            default -> throw new IllegalArgumentException("Invalid filter operation: " + filterOperation);
          };
        } else {
          throw new IllegalArgumentException("Rating criteria Invalid");
        }
      case "runtime":
        List<String> runtimeCommands = Arrays.stream(filterValue.split(" ")).toList();
        if (runtimeCommands.size() > 1) {
          String filterOperation = runtimeCommands.get(0);
          return switch (filterOperation) {
            case "=" -> records.stream().filter(m -> m.Runtime().split(" ")[0].equals(runtimeCommands.get(1)));
            case ">" -> records.stream()
                .filter(m -> Double.parseDouble(m.Runtime().split(" ")[0]) >
                    Double.parseDouble(runtimeCommands.get(1)));
            case "<" -> records.stream()
                .filter(m -> Double.parseDouble(m.Runtime().split(" ")[0]) <
                    Double.parseDouble(runtimeCommands.get(1)));
            case ">=" -> records.stream()
                .filter(m -> Double.parseDouble(m.Runtime().split(" ")[0]) >=
                    Double.parseDouble(runtimeCommands.get(1)));
            case "<=" -> records.stream()
                .filter(m -> Double.parseDouble(m.Runtime().split(" ")[0]) <=
                    Double.parseDouble(runtimeCommands.get(1)));
            default -> throw new IllegalArgumentException("Invalid filter operation: " + filterOperation);
          };
        } else {
          throw new IllegalArgumentException("Runtime criteria Invalid");
        }
      case "country":
        return records.stream().filter(m -> m.Country().toLowerCase().contains(filterValue.toLowerCase()));
      default:
        return records.stream(); // Return all records if filter type is not recognized
    }
  }

  /**
   * Method for sorting movies based on user passed column and ordering criteria.
   *
   * @param movieStream
   * @param ascOrDesc
   * @param column
   * @return List of movie records sorted in the appropriate order.
   */
  public List<MRecord> sortMovieList(Stream<MRecord> movieStream, String ascOrDesc, String column) {

    // Error handling for null parameters passed
    if (movieStream == null || ascOrDesc == null || column == null) {
      throw new IllegalArgumentException("Invalid arguments for Sorting Movies");
    }

    // Creating Comparator
    Comparator<MRecord> comparator;


    // Switch statement to determine what kind of comparison we need
    switch (column.toLowerCase()) {
      case "title":
        if (ascOrDesc.equals("desc")) {
          comparator = Comparator.comparing(MRecord::Title);
          break;
        } else if (ascOrDesc.equals("asc")) {
          comparator = Comparator.comparing(MRecord::Title).reversed();
          break;
        }
      case "year":
        if (ascOrDesc.equals("desc")) {
          comparator = Comparator.comparing(MRecord::Year).reversed();
          break;
        } else if (ascOrDesc.equals("asc")) {
          comparator = Comparator.comparing(MRecord::Year);
          break;
        }
      case "rating":
        if (ascOrDesc.equals("desc")) {
          comparator = Comparator.comparing(MRecord::imdbRating).reversed();
          break;
        } else if (ascOrDesc.equals("asc")) {
          comparator = Comparator.comparing(MRecord::imdbRating);
          break;
        }
      case "runtime":
        if (ascOrDesc.equals("desc")) {
          comparator = Comparator.comparing(MRecord::Runtime).reversed();
          break;
        } else if (ascOrDesc.equals("asc")) {
          comparator = Comparator.comparing(MRecord::Runtime);
          break;
        }

      default:
        throw new IllegalArgumentException("Invalid parameters for sorting movies...");
    }

    return movieStream.sorted(comparator).toList();
  }

    @Override
    public List<MRecord> getRecords() {
        return records;
    }

    public static void main(String[] args) {
        IMovieModel movieModel = IMovieModel.getInstance("jahnsfijnaiuefiuahwnef");
        List<MRecord> records = movieModel.getRecords();
        System.out.println("Total records: " + records.size());
        if (!records.isEmpty()) {
            System.out.println("First record: " + records.get(0).toString());
            String jsonOutput = IMovieModel.exportRecordAsJson(records.get(0));
            System.out.println("\nSample JSON output of first record:");
            System.out.println(jsonOutput);
        }
        
        MRecord newRecord = movieModel.getRecord("Stranger Things");
        
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
