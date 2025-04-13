package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.json.JsonMapper;

import model.IMovieModel.MRecord;
import net.NetUtils;

public class MovieModelTest {

  private MovieModel movieModel;
  private NetUtils netUtilsMock;
  private JsonMapper jsonMapperMock;

  @BeforeEach
  public void setUp() {
    // Prepare mock dependencies
    netUtilsMock = mock(NetUtils.class);
    jsonMapperMock = mock(JsonMapper.class);
        // Initialize the MovieModel with mock records
        movieModel = new MovieModel("data/testmovies.json");
    }

  @Test
  public void testGetRecord1() {
    // Test when the record is found in the list
    MRecord result = movieModel.getRecord("Inception");

    assertNotNull(result);  // Ensure the record is found
    assertEquals("Inception", result.Title()); // Check if the title matches
    assertEquals("2010", result.Year()); // Check if the year matches
  }

  @Test
  public void testGetRecord2() {
    MRecord result = movieModel.getRecord("Titanic");

    assertEquals("Titanic", result.Title());
    assertEquals("James Cameron", result.Director());
  }

  @Test
  public void testNonexistentRecord() {
    MRecord result = movieModel.getRecord("Stranger Things");
    System.out.println(result);
    assertEquals("Stranger Things", result.Title());
  }

  @Test
  public void testFilterDefault() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("nonExistant Type", "NON existent");
    List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
    List<String> expected = Arrays.asList("Inception", "Titanic", "The Matrix", "City of God", "Rango", "Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListYear() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("year", "= 2016");
    List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
    List<String> expected = List.of("Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListYearVoid() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("year", "Void");
    List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
    List<String> expected = new ArrayList<>();
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListYearGreater() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("year", "> 2000");
    List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "City of God", "Rango", "Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListYearLess() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("year", "< 2000");
    List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
    List<String> expected = List.of("Titanic", "The Matrix");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListYearGreaterOrEqual() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("year", ">= 2002");
    List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "City of God", "Rango", "Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListYearLessOrEqual() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("year", "<= 2002");
    List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
    List<String> expected = List.of("Titanic", "The Matrix", "City of God");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListGenre() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("genre", "sci-fi");
    List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "The Matrix");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListGenreVoid() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("genre", "Void");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = new ArrayList<>();
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListSingleDirector() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("director", "Gore Verbinski");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Rango");
    assertEquals(expected, actual);
  }


  @Test
  public void testFilterWatchListDirectorMultiDirector() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("director", "Lana Wachowski");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("The Matrix");
    assertEquals(expected, actual);

  }

  @Test
  public void testFilterWatchListActorSingle() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("actors", "Leonardo DiCaprio");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "Titanic");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListActorInvalid() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("actors", "John Smithington");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = new ArrayList<>();
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListEqualRating() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("rating", "= 8.6");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("City of God");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListEqualRatingVoid() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("rating", "= 11");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = new ArrayList<>();
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListGreaterThanRating() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("rating", "> 8.6");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "The Matrix", "Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListGreaterThanRatingVoid() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("rating", "> 11");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = new ArrayList<>();
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListLessThanRating() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("rating", "< 8.5");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Titanic", "Rango");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListGreaterThanOrEqualRating() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("rating", ">= 8.7");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "The Matrix", "Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListLessThanOrEqualRating() {
    // Assuming filterWatchList method takes a "rating" field and the operation "<="
    Stream<MRecord> recordStream = movieModel.filterWatchList("rating", "<= 8.6");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Titanic", "City of God", "Rango");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListRatingNoFilter() {
    assertThrows(IllegalArgumentException.class, () -> movieModel.filterWatchList("rating", "10"));
  }

  @Test
  public void testFilterWatchListEqualRuntimeVoid() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("runtime", "= 3000");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = new ArrayList<>();
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListEqualRuntime() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("runtime", "= 107");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Rango");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListGreaterThanRuntime() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("runtime", "> 110");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "Titanic", "The Matrix", "City of God");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListLessThanRuntime() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("runtime", "< 130");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Rango", "Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListGreaterThanOrEqualRuntime() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("runtime", ">= 130");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "Titanic", "The Matrix", "City of God");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListLessThanOrEqualRuntime() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("runtime", "<= 190");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "The Matrix", "City of God", "Rango","Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListCountry() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("country", "United States");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = List.of("Inception", "Titanic", "The Matrix", "Rango", "Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWatchListCountryVoid() {
    Stream<MRecord> recordStream = movieModel.filterWatchList("country", "Void");
    List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
    List<String> expected = new ArrayList<>();
    assertEquals(expected, actual);

  }

  @Test
  public void testSortWatchListTitleDesc() {
    Stream<MRecord> recordStream = movieModel.getRecords().stream();
    List<MRecord> sortedRecords = movieModel.sortMovieList(recordStream, "desc", "title");
    List<String> actual = sortedRecords.stream().map(MRecord::Title).toList();
    List<String> expected = List.of("City of God", "Inception", "Rango", "Stranger Things", "The Matrix", "Titanic");
    assertEquals(expected, actual);
  }

  @Test
  public void testSortWatchListTitleAsc() {
    Stream<MRecord> recordStream = movieModel.getRecords().stream();
    List<MRecord> sortedRecords = movieModel.sortMovieList(recordStream, "asc", "title");
    List<String> actual = sortedRecords.stream().map(MRecord::Title).toList();
    List<String> expected = List.of("Titanic", "The Matrix", "Stranger Things", "Rango", "Inception", "City of God");
    assertEquals(expected, actual);
  }

  @Test
  public void testSortWatchListNull() {
    assertThrows(IllegalArgumentException.class, () -> movieModel.sortMovieList(null, "asc", "title"));
  }

  @Test
  public void testSortWatchListYearDesc() {
    Stream<MRecord> recordStream = movieModel.getRecords().stream();
    List<MRecord> sortedRecords = movieModel.sortMovieList(recordStream, "desc", "year");
    List<String> actual = sortedRecords.stream().map(MRecord::Title).toList();
    List<String> expected = List.of("Stranger Things","Rango", "Inception", "City of God", "The Matrix", "Titanic");
    assertEquals(expected, actual);
  }

  @Test
  public void testSortWatchListYearAsc() {
    Stream<MRecord> recordStream = movieModel.getRecords().stream();
    List<MRecord> sortedRecords = movieModel.sortMovieList(recordStream, "asc", "year");
    List<String> actual = sortedRecords.stream().map(MRecord::Title).toList();
    List<String> expected = List.of("Titanic", "The Matrix", "City of God", "Inception", "Rango", "Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testSortWatchListRatingDesc() {
    Stream<MRecord> recordStream = movieModel.getRecords().stream();
    List<MRecord> sortedRecords = movieModel.sortMovieList(recordStream, "desc", "rating");
    List<String> actual = sortedRecords.stream().map(MRecord::Title).toList();
    List<String> expected = List.of("Inception", "The Matrix", "Stranger Things", "City of God", "Titanic", "Rango");
    assertEquals(expected, actual);
  }

  @Test
  public void testSortWatchListRatingAsc() {
    Stream<MRecord> recordStream = movieModel.getRecords().stream();
    List<MRecord> sortedRecords = movieModel.sortMovieList(recordStream, "asc", "rating");
    List<String> actual = sortedRecords.stream().map(MRecord::Title).toList();
    List<String> expected = List.of("Rango", "Titanic", "City of God", "The Matrix", "Stranger Things", "Inception");
    assertEquals(expected, actual);
  }

  @Test
  public void testSortWatchListRuntimeDesc() {
    Stream<MRecord> recordStream = movieModel.getRecords().stream();
    List<MRecord> sortedRecords = movieModel.sortMovieList(recordStream, "desc", "runtime");
    List<String> actual = sortedRecords.stream().map(MRecord::Title).toList();
    List<String> expected = List.of("Stranger Things","Titanic", "Inception", "The Matrix", "City of God", "Rango");
    assertEquals(expected, actual);
  }

  @Test
  public void testSortWatchListRuntimeAsc() {
    Stream<MRecord> recordStream = movieModel.getRecords().stream();
    List<MRecord> sortedRecords = movieModel.sortMovieList(recordStream, "asc", "runtime");
    List<String> actual = sortedRecords.stream().map(MRecord::Title).toList();
    List<String> expected = List.of("Rango", "City of God", "The Matrix", "Inception", "Titanic","Stranger Things");
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMovieDistributions(){
    List<Double> actual = movieModel.getMovieDistributions();
    List<Double> expected = List.of(7.3, 7.9, 8.6, 8.7, 8.7, 8.8);
    assertEquals(expected, actual);
  }

}
