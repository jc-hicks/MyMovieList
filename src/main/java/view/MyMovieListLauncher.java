package view;

import controller.Controller;
import model.MovieModel;

public class MyMovieListLauncher {

    /**
     * Main launcher for program
     */
    public static void main(String[] args) {
        MyMovieList view = new MyMovieList();
        MovieModel movieModel = new MovieModel();
        IMovieFeatures controller = new Controller(movieModel);
        view.setFeatures(controller);
//        view.setFeatures(new RealMovieFeatures());  remember: Use RealMovieFeatures to access IMDBs API
        view.setVisible(true);
    }
}
