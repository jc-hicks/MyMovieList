package model;

import static java.lang.Integer.parseInt;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import net.NetUtils;

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
