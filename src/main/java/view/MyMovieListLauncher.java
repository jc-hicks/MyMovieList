package view;

public class MyMovieListLauncher {

    /**
     * Main launcher for program
     */
    public static void main(String[] args) {
        MyMovieList view = new MyMovieList();
        view.setFeatures(new MockMovieFeatures());
        view.setVisible(true);
    }
}
