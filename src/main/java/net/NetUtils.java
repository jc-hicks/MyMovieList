package net;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A series of classes to help with pulling data from https://www.omdbapi.com.
 * 
 * You can read more about the API at https://www.omdbapi.com.
 */
public class NetUtils {
    /**
     * Format required for the API request. There are many options, but keeping it simple for now.
     */
    private static final String API_URL_FORMAT = "https://www.omdbapi.com/?apikey=%s&t=%s";
    
    /**
     * The API key for the OMDb API. This may be overwritten with a users own key.
     */
    private static String API_KEY;

    /**
     * Prevent instantiation.
     */
    private NetUtils() {
        // Prevent instantiation
    }

    /**
     * Sets the API key for core functionality
     * @param apiKey
     */
    public static void setAPIKey(String apiKey) {
        API_KEY = apiKey;
    }

    /**
     * Returns the URL for retrieving movie by title.
     * 
     * @param title The movie title to look up.
     * @return The URL for the API request.
     */
    public static String getMovieUrl(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
            System.out.println("Encoded title: " + encodedTitle);
            return String.format(API_URL_FORMAT, API_KEY, encodedTitle);
        } catch (Exception e) {
            System.err.println("Error encoding title: " + e.getMessage());
            return String.format(API_URL_FORMAT, API_KEY, title);
        }
    }
    
    /**
     * Gets the contents of a URL as an InputStream.
     * 
     * @param urlStr the URL to get the contents of
     * @return the contents of the URL as an InputStream, or the null InputStream if the connection
     *         fails
     * 
     */
    public static InputStream getUrlContents(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");  // RUBEN: changed to JSON from XML
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                            + "(KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            int status = con.getResponseCode();
            if (status == 200) {
                return con.getInputStream();
            } else {
                System.err.println("Failed to connect to " + urlStr);
            }

        } catch (Exception ex) {
            System.err.println("Failed to connect to " + urlStr);
        }
        return InputStream.nullInputStream();
    }


    /**
     * Gets movie details from the OMDb API.
     * 
     * @param title The movie title to look up
     * @return The movie data as an InputStream
     */
    public static InputStream getMovieDetails(String title) {
        String urlStr = getMovieUrl(title);
        return getUrlContents(urlStr);
    }
    
    /**
     * Sample usage of the movie API.
     */
    public static void main(String[] args) {
        try {
            InputStream movieData = NetUtils.getMovieDetails("Stranger Things");
            System.out.println(new String(movieData.readAllBytes()));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

// https://www.geeksforgeeks.org/java-net-urlencoder-class-java/