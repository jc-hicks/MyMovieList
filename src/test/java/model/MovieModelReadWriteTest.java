package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import model.IMovieModel.MRecord;

public class MovieModelReadWriteTest {
    @TempDir
    Path tempDir;
  
    //Has to be set up privately as I could not access tempFilePath when I defined it in @BeforeEach so couldn't instantiate DNModels
    private Path tempFilePath;
  
    @BeforeEach
    //This setup is useful before each test to set up a dummy xml file as it is annoying to do it every time
    void setUp() throws IOException {
  
      //resolves string into a path directory within tempDir
      tempFilePath = tempDir.resolve("test.xml");
  
      //Dummy data because empty file caused issues with creation of DNModel
      String dummyData = """
            [ {
                "Title" : "Stranger Things",
                "Year" : "2016–2025",
                "Director" : "N/A",
                "Actors" : "Millie Bobby Brown, Finn Wolfhard, Winona Ryder",
                "Plot" : "In 1980s Indiana, a group of young friends witness supernatural forces and secret government exploits. As they search for answers, the children unravel a series of extraordinary mysteries.",
                "Poster" : "https://m.media-amazon.com/images/M/MV5BMjE2N2MyMzEtNmU5NS00OTI0LTlkNTMtMWM1YWYyNmU4NmY0XkEyXkFqcGc@._V1_SX300.jpg",
                "imdbRating" : "8.7",
                "Genre" : "Drama, Fantasy, Horror",
                "Runtime" : "51 min",
                "Country" : "United States"
                }, {
                "Title" : "Inception",
                "Year" : "2010",
                "Director" : "Christopher Nolan",
                "Actors" : "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
                "Plot" : "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
                "Poster" : "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg",
                "imdbRating" : "8.8",
                "Genre" : "Action, Adventure, Sci-Fi",
                "Runtime" : "148 min",
                "Country" : "United States, United Kingdom"
                }, {
                "Title" : "The Shawshank Redemption",
                "Year" : "1994",
                "Director" : "Frank Darabont",
                "Actors" : "Tim Robbins, Morgan Freeman, Bob Gunton",
                "Plot" : "Over the course of several years, two convicts form a friendship, seeking consolation and, eventually, redemption through basic compassion.",
                "Poster" : "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGc@._V1_SX300.jpg",
                "imdbRating" : "9.3",
                "Genre" : "Drama",
                "Runtime" : "142 min",
                "Country" : "United States"
                }]
          """;
      //Puts the dummy data into the tempFilePath file
      Files.writeString(tempFilePath,dummyData);
    }

    @Test
    public void TestReadRecordsFromXML() {
        // Create an instance of MovieModel with the temporary file path
        MovieModel movieModel = new MovieModel(tempFilePath.toString());
  
        // Read records from the XML file
        List<MRecord> records = movieModel.getRecords();
  
        // Check if the number of records is as expected
        assertEquals(3, records.size(), "Number of records should be 3");
  
        // Check if the first record's title is as expected
        assertEquals("Stranger Things", records.get(0).Title(), "First record's title should be 'Stranger Things'");
    }

    @Test
    public void TestWriteRecordsToXML(){
        // Create an instance of MovieModel with the temporary file path
        MovieModel movieModel = new MovieModel(tempFilePath.toString());
  
        // Read records from the XML file
        List<MRecord> records = movieModel.getRecords();
  
        // Check if the number of records is as expected
        assertEquals(3, records.size(), "Number of records should be 3");
  
        // Add a new record to the model
        MRecord newRecord = new MRecord("New Movie", "2023", "Director", "Actors", "Plot", "Poster", "9.0", "Genre", "120 min", "Country");
        movieModel.addRecord(newRecord);
  
        // Read records again after adding a new one
        List<MRecord> updatedRecords = movieModel.getRecords();
  
        // Check if the number of records is updated correctly
        assertEquals(4, updatedRecords.size(), "Number of records should be 4 after adding a new one");
  
        // Check if the last record's title is as expected
        assertEquals("New Movie", updatedRecords.get(3).Title(), "Last record's title should be 'New Movie'");
    }

    @Test
    public void testGetNonExistantRecord() {
        // Create an instance of MovieModel with the temporary file path
        MovieModel movieModel = new MovieModel(tempFilePath.toString());
  
        // Attempt to get a record that doesn't exist
        MRecord result = movieModel.getRecord("uiqawehfiuqwbwnaeilufnqlaiwuewfnaqwilwuefniulwaqefiulqawehfiu");
  
        // Check if the result is null or empty
        assertEquals(null, result, "Result should be null for a nonexistent movie");

    }

    @Test
    public void testGetRecord() {
        // Create an instance of MovieModel with the temporary file path
        MovieModel movieModel = new MovieModel(tempFilePath.toString());
  
        // Attempt to get a record that exists
        MRecord result = movieModel.getRecord("Inception");
  
        // Check if the result is not null and has the expected title
        assertEquals("Inception", result.Title(), "Result should be 'Inception' for an existing movie");
}

    @Test
    public void testGetRecordWithNullTitle() {
        // Create an instance of MovieModel with the temporary file path
        MovieModel movieModel = new MovieModel(tempFilePath.toString());
  
        // Attempt to get a record with a null title
        MRecord result = movieModel.getRecord(null);
  
        // Check if the result is null or empty
        assertEquals(null, result, "Result should be null for a nonexistent movie");
    }

    @Test
    public void testGetFromWrongFile() {
        // Create an instance of MovieModel with a non-existent file path
        MovieModel movieModel = new MovieModel("non_existent_file.xml");
  
        // Attempt to get a record that doesn't exist
        MRecord result = movieModel.getRecord("Inception");
  
        // Check if the result is null or empty
        assertEquals(null, result, "Result should be null for a nonexistent movie");
    }

    @Test
    public void testGetRecordWithEmptyTitle() {
        // Create an instance of MovieModel with the temporary file path
        MovieModel movieModel = new MovieModel(tempFilePath.toString());
  
        // Attempt to get a record with an empty title
        MRecord result = movieModel.getRecord("");
  
        // Check if the result is null or empty
        assertEquals(null, result, "Result should be null for a nonexistent movie");
    }
}