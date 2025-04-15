package view;

import controller.Controller;
import controller.IMovieController;
import model.MovieModel;

public class MyMovieListLauncher {

    /**
     * Main launcher for program
     */
    public static void main(String[] args) {

        MovieModel movieModel = new MovieModel();
        IMovieController controller = new Controller(movieModel);


//        controller.modelSetAPIKey("OMDB_KEY_REDACTED");

        MyMovieList view = new MyMovieList();
        view.setFeatures(controller);
//        view.setFeatures(new RealMovieFeatures());  remember: Use RealMovieFeatures to access IMDBs API
        view.setVisible(true);
    }
}
