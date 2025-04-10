package view;

import javax.swing.*;

public class MyMovieList extends JFrame {

    /**
     * Main GUI panel.
     */
    public MyMovieList() {
        super("My Movie List");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        this.add(panel);
    }

    private IMovieFeatures features;
    public void setFeatures(IMovieFeatures features) {
        this.features = features;
    }

}
