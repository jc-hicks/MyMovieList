package model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        // Create some mock data for the records
        MRecord record1 = new MRecord("Inception", "2010", "Christopher Nolan", "Leonardo DiCaprio", "A mind-bending thriller", "poster1", "8.8", "Sci-Fi", "148 minutes", "USA");
        MRecord record2 = new MRecord("Titanic", "1997", "James Cameron", "Leonardo DiCaprio", "A romantic tragedy", "poster2", "7.8", "Drama", "195 minutes", "USA");

        List<IMovieModel.MRecord> records = Arrays.asList(record1, record2);

        // Initialize the MovieModel with mock records
        movieModel = new MovieModel();
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

    @Test
    public void testRecordsFromJson() {
        // Path to the test JSON file
        String testJsonPath = "src/test/java/model/data/testmovies.json";

        // Load the movie model from the JSON file
        IMovieModel modelFromJson = IMovieModel.getInstance(testJsonPath);

        // Get records from the loaded model
        List<IMovieModel.MRecord> records = modelFromJson.getRecords();

        // Verify number of records is correct.
        assertNotNull(records);
        assertFalse(records.isEmpty());
        assertEquals(3, records.size());

        // Check all titles loaded from JSON file.
        assertTrue(records.stream().anyMatch(r -> r.Title().equals("Stranger Things")));
        assertTrue(records.stream().anyMatch(r -> r.Title().equals("Inception")));
        assertTrue(records.stream().anyMatch(r -> r.Title().equals("The Shawshank Redemption")));

        // Get the specific record for "Inception" to verify its details.
        MRecord inception = records.stream()
                .filter(r -> r.Title().equals("Inception"))
                .findFirst()
                .orElse(null);

        // Verify that the "Inception" record has the expected details.
        assertNotNull(inception);
        assertEquals("2010", inception.Year());
        assertEquals("Christopher Nolan", inception.Director());
        assertEquals("8.8", inception.imdbRating());
    }

}
