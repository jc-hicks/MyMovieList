package Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import model.MovieModel;
import controller.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.json.JsonMapper;

import model.IMovieModel.MRecord;
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
    movieModel = new MovieModel("data/testmovies.json");
    movieController = new Controller(movieModel);
  }

  @Test
  public void testGetAllMovies() {
    List<MRecord> movies = movieController.getAllMovies();

  }


}
