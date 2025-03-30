package model;

import com.fasterxml.jackson.databind.json.JsonMapper;
import net.NetUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class MovieModel implements IMovieModel {

    private final List<MRecord> records;

    public MovieModel(List<MRecord> records) {
        this.records = records;
    }

    @Override
    public MRecord getRecord(String title) {
        // Check the records first
        for (MRecord record : records) {
            if (record.Title().equals(title)) {
                return record;
            }
        }
        try {
            String url = NetUtils.getMovieUrl(title); // Get the URL
            try (InputStream inputStream = NetUtils.getUrlContents(url)) {
                JsonMapper mapper = new JsonMapper();
                return mapper.readValue(inputStream, MRecord.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MRecord> getRecords() {
        return List.copyOf(records);
    }

}
