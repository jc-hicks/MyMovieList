package Controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.json.JsonMapper;

import controller.Controller;
import model.IMovieModel.MRecord;
import model.MovieModel;
import net.NetUtils;

public class ControllerTests {

  private MovieModel movieModel;
  private NetUtils netUtilsMock;
  private JsonMapper jsonMapperMock;
  private Controller movieController;

  @BeforeEach
  public void setUp() {
    // Prepare mock dependencies
    netUtilsMock = mock(NetUtils.class);
    jsonMapperMock = mock(JsonMapper.class);
    // Initialize the MovieModel with mock records
    movieModel = new MovieModel();
    movieController = new Controller(movieModel);
    movieController.loadWatchlistOnStartup();
  }

  @Test
  public void testGetAllMovies() {
    List<MRecord> movies = movieController.getAllMovies();

    assertEquals(23, movies.size());

    for(MRecord movie : movies){
      System.out.println(movie);
    }
  }

  @Test
  public void testLoadWatchListOnStartUp(){
    List<MRecord> watchlist = movieController.getWatchList();

    assertNotNull(watchlist);
    assertFalse(watchlist.isEmpty());

    for (MRecord record : watchlist) {
        System.out.println(record);
    }
  }

  @Test
  public void testAddToWatchList(){
    List<MRecord> watchList = movieController.getWatchList();
    int expected = watchList.size();

    movieController.addToWatchList("Stranger Things");
    assertEquals(expected + 1, watchList.size());
  }


  @Test
  public void testRemoveFromWatchList(){
    List<MRecord> watchList = movieController.getWatchList();
    System.out.println(watchList);
    int expected = watchList.size();

    movieController.removeFromWatchList("Rango");
    assertEquals(expected - 1, watchList.size());
  }

  @Test
  public void testSortMovieList(){
    List<MRecord> movieList = movieModel.getRecords();
    System.out.println(movieList);
    List<MRecord> sortedMovieList = movieController.sortMovieList("Title", "asc");

    System.out.println("");

    assertTrue(sortedMovieList.get(0).Title().equals("Twilight"));
    System.out.println(sortedMovieList);

    sortedMovieList = movieController.sortMovieList("year", "desc");
    assertTrue(sortedMovieList.get(0).Title().equals("Challengers"));
    System.out.println(sortedMovieList);

  }

  @Test
  public void testGetMyRating(){
    List<MRecord> watchList = movieController.getWatchList();
    String rating = movieController.getMyRating("Rango");

    assertTrue(rating.equals("7.3"));

  }







































}
