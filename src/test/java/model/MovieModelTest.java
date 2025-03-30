package model;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.ArrayList;
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
        MRecord record1 = new MRecord("Inception", "2010", "Christopher Nolan",
            "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
            "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.",
            "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg",
            "8.8", "Action, Adventure, Sci-Fi", "148 min", "United States, United Kingdom");

        MRecord record2 = new MRecord("Titanic", "1997", "James Cameron",
            "Leonardo DiCaprio, Kate Winslet, Billy Zane",
            "A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.",
            "https://m.media-amazon.com/images/M/MV5BYzYyN2FiZmUtYWYzMy00MzViLWJkZTMtOGY1ZjgzNWMwN2YxXkEyXkFqcGc@._V1_SX300.jpg",
            "7.9", "Drama, Romance", "194 min", "United States, Mexico");

        MRecord record3 = new MRecord("The Matrix", "1999", "Lana Wachowski, Lilly Wachowski",
            "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
            "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.",
            "https://m.media-amazon.com/images/M/MV5BN2NmN2VhMTQtMDNiOS00NDlhLTliMjgtODE2ZTY0ODQyNDRhXkEyXkFqcGc@._V1_SX300.jpg",
            "8.7", "Action, Sci-Fi", "136 min", "United States, Australia");

        MRecord record4 = new MRecord("City of God", "2002", "Fernando Meirelles, Kátia Lund",
            "Alexandre Rodrigues, Leandro Firmino, Matheus Nachtergaele",
            "In the slums of Rio, two kids' paths diverge as one struggles to become a photographer and the other a kingpin.",
            "https://m.media-amazon.com/images/M/MV5BYjY4NGI5OTUtY2ZlZS00Zjk4LTk5N2MtN2JmYWVjNGNmMGRlXkEyXkFqcGc@._V1_SX300.jpg",
            "8.6", "Crime, Drama", "130 min", "Brazil, France");

        MRecord record5 = new MRecord("Rango", "2011", "Gore Verbinski",
            "Johnny Depp, Isla Fisher, Timothy Olyphant",
            "Rango is an ordinary chameleon who accidentally winds up in the town of Dirt, a lawless outpost in the Wild West in desperate need of a new sheriff.",
            "https://m.media-amazon.com/images/M/MV5BMTc4NjEyODE1OV5BMl5BanBnXkFtZTcwMjYzNTkxNA@@._V1_SX300.jpg",
            "7.3", "Animation, Action, Adventure", "107 min", "United States, United Kingdom");

        List<IMovieModel.MRecord> records = Arrays.asList(record1, record2, record3, record4, record5);

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
        Stream<MRecord> recordStream = movieModel.filterWatchList("nonExistant Type","NON existent");
        List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
        List<String> expected = Arrays.asList("Inception", "Titanic");
        assertEquals(expected, actual);
    }

    @Test public void testFilterWatchListYear(){
        Stream<MRecord> recordStream = movieModel.filterWatchList("year","2010");
        List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
        List<String> expected = List.of("Inception");
        assertEquals(expected, actual);
    }

    @Test public void testFilterWatchListYearVoid(){
        Stream<MRecord> recordStream = movieModel.filterWatchList("year","Void");
        List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
        List<String> expected = new ArrayList<>();
        assertEquals(expected, actual);
    }

    @Test public void testFilterWatchListGenre(){
        Stream<MRecord> recordStream = movieModel.filterWatchList("genre","sci-fi");
        List<String> actual = recordStream.map(m -> m.Title()).collect(Collectors.toList());
        List<String> expected = List.of("Inception");
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterWatchListGenreVoid(){
        Stream<MRecord> recordStream = movieModel.filterWatchList("genre","Void");
        List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
        List<String> expected = new ArrayList<>();
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterWatchListSingleDirector(){
        Stream<MRecord> recordStream = movieModel.filterWatchList("director","Gore Verbinski");
        List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
        List<String> expected = List.of("Rango");
        assertEquals(expected, actual);
    }


    @Test
    public void testFilterWatchListDirectorMultiDirector(){
        Stream<MRecord> recordStream = movieModel.filterWatchList("director","Lana Wachowski");
        List<String> actual = recordStream.map(MRecord::Title).collect(Collectors.toList());
        List<String> expected = List.of("The Matrix");
        assertEquals(expected, actual);

    }
}
