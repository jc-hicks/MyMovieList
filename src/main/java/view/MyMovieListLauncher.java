package view;

import javax.swing.*;

public class MyMovieListLauncher {

    /**
     * Main launcher for
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyMovieList view = new MyMovieList();
            view.setVisible(true);
        });
    }
}
