package view;

public class MyMovieListLauncher {

    /**
     * Main launcher for program
     */
    public static void main(String[] args) {
        MyMovieList view = new MyMovieList();
        IMovieFeatures features = new RealMovieFeatures();
        view.setFeatures(features);
//        view.setFeatures(new RealMovieFeatures());  remember: Use RealMovieFeatures to access IMDBs API
        view.setVisible(true);
    }
}
