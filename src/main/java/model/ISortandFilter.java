package model;

import java.util.List;
import java.util.stream.Stream;

import model.IMovieModel.MRecord;

public interface ISortandFilter {
     /**
     * Method for sorting movies based on user passed column and ordering
     * criteria.
     *
     * @param movieStream
     * @param ascOrDesc
     * @param column
     * @return List of movie records sorted in the appropriate order.
     */
    List<MRecord> sortMovieList(Stream<MRecord> movieStream, String ascOrDesc, String column);

    /**
     * Method to filter through the current list of movies based on passed
     * criteria.
     *
     * @param filterType
     * @param filterValue
     * @return Stream of movie records
     */
    Stream<IMovieModel.MRecord> filterWatchList(String filterType, String filterValue, MovieList movieList, List<MRecord> records);
}
