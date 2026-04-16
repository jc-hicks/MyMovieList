package model;

import static java.lang.Integer.parseInt;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import model.IMovieModel.MRecord;

/**
 * The MovieListSortFilter class implements the ISortandFilter interface and
 * provides methods for filtering and sorting movie records based on various
 * criteria. It is used to manage the movie list and watchlist functionality in
 * the application.
 */
public class MovieListSortFilter implements  ISortandFilter {

    /**
     * A private method utilized to simplify tv show year ranges to default to pilot year.
     * @param inputYear
     * @return pilot year in string form.
     */
    private static String multiYearParse(String inputYear) {
        return inputYear.contains("\u2013") 
            ? inputYear.split("\u2013")[0] 
            : inputYear;
    }

    /**
     * Applies a numeric comparison filter based on the operator and threshold.
     * Supports operators: =, >, <, >=, <=
     * @param operator the comparison operator
     * @param value the threshold value
     * @param predicate the comparison to perform
     * @return true if the comparison is satisfied, false otherwise
     * @throws IllegalArgumentException if the operator is invalid
     */
    private static boolean applyNumericComparison(String operator, double value, double threshold) {
        return switch (operator) {
            case "=" -> value == threshold;
            case ">" -> value > threshold;
            case "<" -> value < threshold;
            case ">=" -> value >= threshold;
            case "<=" -> value <= threshold;
            default -> throw new IllegalArgumentException("Invalid filter operation: " + operator);
        };
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
        
        return switch (filterType.toLowerCase()) {
            case "title" -> records.stream()
                .filter(m -> !m.Title().equals("N/A") && m.Title().toLowerCase().contains(filterValue.toLowerCase()));
            
            case "year" -> filterByYear(records, filterValue);
            
            case "director" -> records.stream()
                .filter(m -> !m.Director().equals("N/A") && m.Director().toLowerCase().contains(filterValue.toLowerCase()));
            
            case "genre" -> records.stream()
                .filter(m -> !m.Genre().equals("N/A") && m.Genre().toLowerCase().contains(filterValue.toLowerCase()));
            
            case "actors" -> records.stream()
                .filter(m -> !m.Actors().equals("N/A") && m.Actors().toLowerCase().contains(filterValue.toLowerCase()));
            
            case "rating" -> filterByRating(records, filterValue);
            
            case "runtime" -> filterByRuntime(records, filterValue);
            
            case "country" -> records.stream()
                .filter(m -> !m.Country().equals("N/A") && m.Country().toLowerCase().contains(filterValue.toLowerCase()));
            
            default -> records.stream(); // Return all records if filter type is not recognized
        };
    }

    /**
     * Helper method to filter movies by year with comparison operators.
     * @param records the list of movie records
     * @param filterValue the filter value in format "[operator] [year]"
     * @return stream of filtered records
     */
    private Stream<IMovieModel.MRecord> filterByYear(List<MRecord> records, String filterValue) {
        List<String> parts = Arrays.asList(filterValue.split(" "));
        if (parts.size() < 2) {
            throw new IllegalArgumentException("Year filter requires operator and value. Format: [operator] [year]");
        }
        
        String operator = parts.get(0);
        int targetYear = parseInt(parts.get(1));
        
        return records.stream()
            .filter(m -> !m.Year().equals("N/A"))
            .filter(m -> {
                int movieYear = parseInt(multiYearParse(m.Year()));
                return applyNumericComparison(operator, movieYear, targetYear);
            });
    }

    /**
     * Helper method to filter movies by rating with comparison operators.
     * @param records the list of movie records
     * @param filterValue the filter value in format "[operator] [rating]"
     * @return stream of filtered records
     */
    private Stream<IMovieModel.MRecord> filterByRating(List<MRecord> records, String filterValue) {
        List<String> parts = Arrays.stream(filterValue.split(" ")).toList();
        if (parts.size() < 2) {
            throw new IllegalArgumentException("Rating filter requires operator and value. Format: [operator] [rating]");
        }
        
        String operator = parts.get(0);
        double targetRating = Double.parseDouble(parts.get(1));
        
        return records.stream()
            .filter(m -> !m.imdbRating().equals("N/A"))
            .filter(m -> {
                double movieRating = Double.parseDouble(m.imdbRating());
                return applyNumericComparison(operator, movieRating, targetRating);
            });
    }

    /**
     * Helper method to filter movies by runtime with comparison operators.
     * @param records the list of movie records
     * @param filterValue the filter value in format "[operator] [runtime]"
     * @return stream of filtered records
     */
    private Stream<IMovieModel.MRecord> filterByRuntime(List<MRecord> records, String filterValue) {
        List<String> parts = Arrays.stream(filterValue.split(" ")).toList();
        if (parts.size() < 2) {
            throw new IllegalArgumentException("Runtime filter requires operator and value. Format: [operator] [runtime]");
        }
        
        String operator = parts.get(0);
        double targetRuntime = Double.parseDouble(parts.get(1));
        
        return records.stream()
            .filter(m -> !m.Runtime().equals("N/A"))
            .filter(m -> {
                double movieRuntime = Double.parseDouble(m.Runtime().split(" ")[0]);
                return applyNumericComparison(operator, movieRuntime, targetRuntime);
            });
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
        if (movieStream == null || ascOrDesc == null || column == null) {
            throw new IllegalArgumentException("Invalid arguments for Sorting Movies");
        }
        
        boolean isDescending = ascOrDesc.equalsIgnoreCase("desc");
        Comparator<IMovieModel.MRecord> comparator = createComparator(column, isDescending);
        
        return movieStream.sorted(comparator).toList();
    }

    /**
     * Helper method to create the appropriate comparator based on the column.
     * @param column the column to sort by
     * @param isDescending whether to sort in descending order
     * @return the comparator to use for sorting
     * @throws IllegalArgumentException if the column is invalid
     */
    private Comparator<IMovieModel.MRecord> createComparator(String column, boolean isDescending) {
        Comparator<IMovieModel.MRecord> comparator = switch (column.toLowerCase()) {
            case "title" -> Comparator.comparing(IMovieModel.MRecord::Title, String.CASE_INSENSITIVE_ORDER);
            
            case "director" -> Comparator.comparing(IMovieModel.MRecord::Director, String.CASE_INSENSITIVE_ORDER);
            
            case "year" -> Comparator.comparing((IMovieModel.MRecord m) -> 
                parseInt(multiYearParse(m.Year()))
            );
            
            case "rating" -> Comparator.comparing((IMovieModel.MRecord m) -> 
                Double.parseDouble(m.imdbRating())
            );
            
            case "runtime" -> Comparator.comparing((IMovieModel.MRecord m) -> 
                Double.parseDouble(m.Runtime().split(" ")[0])
            );
            
            default -> throw new IllegalArgumentException("Invalid column for sorting: " + column);
        };
        
        return isDescending ? comparator.reversed() : comparator;
    }
}
