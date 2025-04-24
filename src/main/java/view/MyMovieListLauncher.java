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
        MyMovieList view = new MyMovieList();
        view.setController(controller);
        view.setVisible(true);
    }
}
