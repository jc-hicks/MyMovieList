package model;

import static java.lang.Integer.parseInt;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import model.IMovieModel.MRecord;

public class MovieListSortFilter implements  ISortandFilter {

    /**
     * A private method utilized to simplify tv show year ranges to default to pilot year.
     * @param inputYear
     * @return pilot year in string form.
     */
    private static String multiYearParse(String inputYear) {
        if (inputYear.contains("\u2013")) {
            List<String> years = Arrays.asList(inputYear.split("\u2013"));
            return years.get(0);
        } else {
            return inputYear;
        }
    }

    /**
     * Method to filter through the current list of movies based on passed
     * criteria.
     *
     * @param filterType
     * @param filterValue
     * @return Stream of movie records
     */
    @Override
    public Stream<IMovieModel.MRecord> filterWatchList(String filterType, String filterValue, List<MRecord> records) {
        if (filterType == null || filterValue == null) {
            throw new IllegalArgumentException("filterType and value cannot be null");
        }
        switch (filterType.toLowerCase()) {
            case "title":
                return records.stream().filter(m -> !m.Title().equals("N/A") && m.Title().toLowerCase().contains(filterValue.toLowerCase()));
            case "year":
                List<String> yearCommands = Arrays.asList(filterValue.split(" "));
                System.out.println("Year Commands: " + yearCommands);
                if (yearCommands.size() > 1) {
                    String filterOperation = yearCommands.get(0);
                    return switch (filterOperation) {
                        case "=" -> records.stream().filter(m -> !m.Year().equals("N/A") && multiYearParse(m.Year()).equals(yearCommands.get(1)));
                        case ">" -> records.stream().filter(m -> !m.Year().equals("N/A") && parseInt(multiYearParse(m.Year())) > parseInt(yearCommands.get(1)));
                        case "<" -> records.stream().filter(m -> !m.Year().equals("N/A") && parseInt(multiYearParse(m.Year())) < parseInt(yearCommands.get(1)));
                        case ">=" -> records.stream().filter(m -> !m.Year().equals("N/A") && parseInt(multiYearParse(m.Year())) >= parseInt(yearCommands.get(1)));
                        case "<=" -> records.stream().filter(m -> !m.Year().equals("N/A") && parseInt(multiYearParse(m.Year())) <= parseInt(yearCommands.get(1)));
                        default -> throw new IllegalStateException("Unexpected year filter value: " + yearCommands.get(1));
                    };
                }
            case "director":
                return records.stream().filter(m -> !m.Director().equals("N/A") && m.Director().toLowerCase().contains(filterValue.toLowerCase()));
            case "genre":
                return records.stream().filter(m -> !m.Genre().equals("N/A") && m.Genre().toLowerCase().contains(filterValue.toLowerCase()));
            case "actors":
                return records.stream().filter(m -> !m.Actors().equals("N/A") && m.Actors().toLowerCase().contains(filterValue.toLowerCase()));
            case "rating":
                List<String> ratingCommands = Arrays.stream(filterValue.split(" ")).toList();
                if (ratingCommands.size() > 1) {
                    String filterOperation = ratingCommands.get(0);
                    return switch (filterOperation) {
                        case "=" -> records.stream().filter(m -> !m.imdbRating().equals("N/A") && m.imdbRating().equals(ratingCommands.get(1)));
                        case ">" -> records.stream().filter(m -> !m.imdbRating().equals("N/A") && Double.parseDouble(m.imdbRating()) > Double.parseDouble(ratingCommands.get(1)));
                        case "<" -> records.stream().filter(m -> !m.imdbRating().equals("N/A") && Double.parseDouble(m.imdbRating()) < Double.parseDouble(ratingCommands.get(1)));
                        case ">=" -> records.stream().filter(m -> !m.imdbRating().equals("N/A") && Double.parseDouble(m.imdbRating()) >= Double.parseDouble(ratingCommands.get(1)));
                        case "<=" -> records.stream().filter(m -> !m.imdbRating().equals("N/A") && Double.parseDouble(m.imdbRating()) <= Double.parseDouble(ratingCommands.get(1)));
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
                        case "=" -> records.stream().filter(m -> !m.Runtime().equals("N/A") && m.Runtime().split(" ")[0].equals(runtimeCommands.get(1)));
                        case ">" -> records.stream().filter(m -> !m.Runtime().equals("N/A") && Double.parseDouble(m.Runtime().split(" ")[0]) > Double.parseDouble(runtimeCommands.get(1)));
                        case "<" -> records.stream().filter(m -> !m.Runtime().equals("N/A") && Double.parseDouble(m.Runtime().split(" ")[0]) < Double.parseDouble(runtimeCommands.get(1)));
                        case ">=" -> records.stream().filter(m -> !m.Runtime().equals("N/A") && Double.parseDouble(m.Runtime().split(" ")[0]) >= Double.parseDouble(runtimeCommands.get(1)));
                        case "<=" -> records.stream().filter(m -> !m.Runtime().equals("N/A") && Double.parseDouble(m.Runtime().split(" ")[0]) <= Double.parseDouble(runtimeCommands.get(1)));
                        default -> throw new IllegalArgumentException("Invalid filter operation: " + filterOperation);
                    };
                } else {
                    throw new IllegalArgumentException("Runtime criteria Invalid");
                }
            case "country":
                return records.stream().filter(m -> !m.Country().equals("N/A") && m.Country().toLowerCase().contains(filterValue.toLowerCase()));
            default:
                return records.stream(); // Return all records if filter type is not recognized
        }
    }

    /**
     * Method for sorting movies based on user passed column and ordering
     * criteria.
     *
     * @param movieStream
     * @param ascOrDesc
     * @param column
     * @return List of movie records sorted in the appropriate order.
     */
    @Override
    public List<IMovieModel.MRecord> sortMovieList(Stream<IMovieModel.MRecord> movieStream, String ascOrDesc, String column) {
        // Error handling for null parameters passed
        if (movieStream == null || ascOrDesc == null || column == null) {
            throw new IllegalArgumentException("Invalid arguments for Sorting Movies");
        }
        // Creating Comparator
        Comparator<IMovieModel.MRecord> comparator;
        // Switch statement to determine what kind of comparison we need
        switch (column.toLowerCase()) {
            case "title":
                if (ascOrDesc.equals("desc")) {
                    comparator = Comparator.comparing(IMovieModel.MRecord::Title);
                    break;
                } else if (ascOrDesc.equals("asc")) {
                    comparator = Comparator.comparing(IMovieModel.MRecord::Title).reversed();
                    break;
                }
            case "director":
                if (ascOrDesc.equals("desc")) {
                    comparator = Comparator.comparing(IMovieModel.MRecord::Director);
                    break;
                } else if (ascOrDesc.equals("asc")) {
                    comparator = Comparator.comparing(IMovieModel.MRecord::Director).reversed();
                    break;
                }
            case "year":
                if (ascOrDesc.equals("desc")) {
                    comparator = Comparator.comparing((IMovieModel.MRecord m) -> parseInt(multiYearParse(m.Year()))).reversed();
                    break;
                } else if (ascOrDesc.equals("asc")) {
                    comparator = Comparator.comparing((IMovieModel.MRecord m) -> parseInt(multiYearParse(m.Year())));
                    break;
                }
            case "rating":
                if (ascOrDesc.equals("desc")) {
                    comparator = Comparator.comparing(IMovieModel.MRecord::imdbRating).reversed();
                    break;
                } else if (ascOrDesc.equals("asc")) {
                    comparator = Comparator.comparing(IMovieModel.MRecord::imdbRating);
                    break;
                }
            case "runtime":
                if (ascOrDesc.equals("desc")) {
                    comparator = Comparator.comparing(IMovieModel.MRecord::Runtime).reversed();
                    break;
                } else if (ascOrDesc.equals("asc")) {
                    comparator = Comparator.comparing(IMovieModel.MRecord::Runtime);
                    break;
                }
            default:
                throw new IllegalArgumentException("Invalid parameters for sorting movies...");
        }
        return movieStream.sorted(comparator).toList();
    }

}
