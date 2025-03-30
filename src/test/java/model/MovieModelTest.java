package model;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.NetUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import model.IMovieModel.MRecord;

public class MovieModelTest {

    private MovieModel movieModel;
    private NetUtils netUtilsMock;
    private JsonMapper jsonMapperMock;

    @BeforeEach
    public void setUp() {
        // Prepare mock dependencies
        netUtilsMock = mock(NetUtils.class);
        jsonMapperMock = mock(JsonMapper.class);

        // Create some mock data for the records
        MRecord record1 = new MRecord("Inception", "2010", "Christopher Nolan", "Leonardo DiCaprio", "A mind-bending thriller", "poster1", "8.8", "Sci-Fi", "148 minutes", "USA");
        MRecord record2 = new MRecord("Titanic", "1997", "James Cameron", "Leonardo DiCaprio", "A romantic tragedy", "poster2", "7.8", "Drama", "195 minutes", "USA");

        List<IMovieModel.MRecord> records = Arrays.asList(record1, record2);

        // Initialize the MovieModel with mock records
        movieModel = new MovieModel(records);
    }

    @Test
    public void testGetRecord1() {
        // Test when the record is found in the list
        MRecord result = movieModel.getRecord("Inception");

        assertNotNull(result);  // Ensure the record is found
        assertEquals("Inception", result.Title()); // Check if the title matches
        assertEquals("2010", result.Year()); // Check if the year matches
    }

    @Test public void testGetRecord2() {
        MRecord result = movieModel.getRecord("Titanic");

        assertEquals("Titanic", result.Title());
        assertEquals("James Cameron", result.Director());
    }

    @Test public void testNonexistentRecord() {
        MRecord result = movieModel.getRecord("Stranger Things");
        System.out.println(result);
        assertEquals("Stranger Things", result.Title());
    }

    @Test public void testFilterDefault(){
        Stream<MRecord> recordStream = movieModel.filterWatchList("NON existent");
        List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
        List<String> expected = Arrays.asList("Inception", "Titanic");
        assertEquals(expected, actual);

    }
}
