package model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class IMovieModelTest {

    private IMovieModel movieModel;

    @BeforeEach
    void setUp() {
        // Mock implementation of IMovieModel for testing
        movieModel = new IMovieModel() {
            private List<MRecord> records = new ArrayList<>();

            @Override
            public List<MRecord> getRecords() {
                return records;
            }

            @Override
            public MRecord getRecord(String title) {
                return records.stream()
                        .filter(record -> record.Title().equals(title))
                        .findFirst()
                        .orElse(null);
            }
        };

        // Adding mock data
        movieModel.getRecords().add(new IMovieModel.MRecord(
                "Inception", "2010", "Christopher Nolan", 
                "Leonardo DiCaprio, Joseph Gordon-Levitt", 
                "A thief who steals corporate secrets through dream-sharing technology.", 
                "poster_url", "8.8", "Action, Sci-Fi", "148 min", "USA"));
    }

    @Test
    void testGetRecords() {
        List<IMovieModel.MRecord> records = movieModel.getRecords();
        assertNotNull(records, "Records list should not be null");
        assertEquals(1, records.size(), "Records list should contain one record");
    }

    @Test
    void testGetRecord() {
        IMovieModel.MRecord record = movieModel.getRecord("Inception");
        assertNotNull(record, "Record should not be null");
        assertEquals("Inception", record.Title(), "Title should match");
        assertEquals("2010", record.Year(), "Year should match");
    }

    @Test
    void testGetRecordNotFound() {
        IMovieModel.MRecord record = movieModel.getRecord("Nonexistent Movie");
        assertNull(record, "Record should be null for nonexistent movie");
    }
}