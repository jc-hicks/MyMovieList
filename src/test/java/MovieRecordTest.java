import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.model.IMovieRecord;
import net.model.MovieRecord;

public class MovieRecordTest {
    // Fields declared as the interface type for better flexibility
    private IMovieRecord testMovie1;
    private IMovieRecord testMovie2;
    private IMovieRecord testMovie3;
    private IMovieRecord testMovie4;
    
    @BeforeEach
    void setUp() {
        testMovie1 = new MovieRecord("Inception", 2010, "Christopher Nolan", "Leonardo DiCaprio, Joseph Gordon-Levitt",
         "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.",
          "https://example.com/inception.jpg", "8.8", "Action, Sci-Fi", "148 min", "USA");

        testMovie2 = new MovieRecord("The Matrix", 1999, "Lana Wachowski, Lilly Wachowski", "Keanu Reeves, Laurence Fishburne",
         "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
          "https://example.com/matrix.jpg", "8.7", "Action, Sci-Fi", "136 min", "USA");

        testMovie3 = new MovieRecord("The Godfather", 1972, "Francis Ford Coppola", "Marlon Brando, Al Pacino",
         "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
          "https://example.com/godfather.jpg", "9.2", "Crime, Drama", "175 min", "USA");

        testMovie4 = new MovieRecord("The Godfather", 1972, "Francis Ford Coppola", "Marlon Brando, Al Pacino",
         "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
          "https://example.com/godfather.jpg", "9.2", "Crime, Drama", "175 min", "USA");
    }

    @Test
    public void testGetTitle() {
        assertEquals("Inception", testMovie1.getTitle());
        assertEquals("The Matrix", testMovie2.getTitle());
        assertEquals("The Godfather", testMovie3.getTitle());
    }

    @Test
    public void testGetYear() {
        assertEquals(2010, testMovie1.getYear());
        assertEquals(1999, testMovie2.getYear());
        assertEquals(1972, testMovie3.getYear());
    }

    @Test
    public void testGetDirector() {
        assertEquals("Christopher Nolan", testMovie1.getDirector());
        assertEquals("Lana Wachowski, Lilly Wachowski", testMovie2.getDirector());
        assertEquals("Francis Ford Coppola", testMovie3.getDirector());
    }

    @Test
    public void testGetCast() {
        assertEquals("Leonardo DiCaprio, Joseph Gordon-Levitt", testMovie1.getCast());
        assertEquals("Keanu Reeves, Laurence Fishburne", testMovie2.getCast());
        assertEquals("Marlon Brando, Al Pacino", testMovie3.getCast());
    }

    @Test
    public void testGetDescription() {
        assertEquals("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.", testMovie1.getDescription());
        assertEquals("A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.", testMovie2.getDescription());
        assertEquals("An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.", testMovie3.getDescription());
    }

    @Test
    public void testGetPosterUrl() {
        assertEquals("https://example.com/inception.jpg", testMovie1.getPosterUrl());
        assertEquals("https://example.com/matrix.jpg", testMovie2.getPosterUrl());
        assertEquals("https://example.com/godfather.jpg", testMovie3.getPosterUrl());
    }

    @Test
    public void testGetRating() {
        assertEquals("8.8", testMovie1.getRating());
        assertEquals("8.7", testMovie2.getRating());
        assertEquals("9.2", testMovie3.getRating());
    }

    @Test
    public void testGetGenre() {
        assertEquals("Action, Sci-Fi", testMovie1.getGenre());
        assertEquals("Action, Sci-Fi", testMovie2.getGenre());
        assertEquals("Crime, Drama", testMovie3.getGenre());
    }

    @Test
    public void testGetRuntime() {
        assertEquals("148 min", testMovie1.getRuntime());
        assertEquals("136 min", testMovie2.getRuntime());
        assertEquals("175 min", testMovie3.getRuntime());
    }

    @Test
    public void testGetCountry() {
        assertEquals("USA", testMovie1.getCountry());
        assertEquals("USA", testMovie2.getCountry());
        assertEquals("USA", testMovie3.getCountry());
    }

    @Test
    public void testMovieToString() {
        String expectedOutput1 = "Title: Inception\nYear: 2010\nDirector: Christopher Nolan\nCast: Leonardo DiCaprio, Joseph Gordon-Levitt\nDescription: A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.\nPoster URL: https://example.com/inception.jpg\nRating: 8.8\nGenre: Action, Sci-Fi\nRuntime: 148 min\nCountry: USA";
        String expectedOutput2 = "Title: The Matrix\nYear: 1999\nDirector: Lana Wachowski, Lilly Wachowski\nCast: Keanu Reeves, Laurence Fishburne\nDescription: A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.\nPoster URL: https://example.com/matrix.jpg\nRating: 8.7\nGenre: Action, Sci-Fi\nRuntime: 136 min\nCountry: USA";
        String expectedOutput3 = "Title: The Godfather\nYear: 1972\nDirector: Francis Ford Coppola\nCast: Marlon Brando, Al Pacino\nDescription: An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.\nPoster URL: https://example.com/godfather.jpg\nRating: 9.2\nGenre: Crime, Drama\nRuntime: 175 min\nCountry: USA";

        assertEquals(expectedOutput1, testMovie1.movieToString());
        assertEquals(expectedOutput2, testMovie2.movieToString());
        assertEquals(expectedOutput3, testMovie3.movieToString());
    }
}