package view;

import model.IMovieModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyMovieList extends JFrame {

    private IMovieFeatures features;
    private JTable movieTable;
    private DefaultTableModel tableModel;
    private JButton loadButton;
    private JButton addToWatchListButton;
    private JButton removeFromWatchListButton;
    /**
     * Main GUI panel.
     */
    public MyMovieList() {
        super("My Movie List");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        initUI();
    }

    public void setFeatures(IMovieFeatures features) {
        this.features = features;
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table layout
        tableModel = new DefaultTableModel(new String[]{"Title", "Year", "Director"}, 0);
        movieTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(movieTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        loadButton = new JButton("Load Movies");
        addToWatchListButton = new JButton("Add to My Watchlist");
        removeFromWatchListButton = new JButton("Remove from My Watchlist");

        buttonPanel.add(loadButton);
        buttonPanel.add(addToWatchListButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(removeFromWatchListButton);

        // Button functionality
        loadButton.addActionListener(e -> loadMovies());
        addToWatchListButton.addActionListener(e -> addSelectedMovieToWatchlist());
        removeFromWatchListButton.addActionListener(e -> removeSelectedMovieFromWatchlist());

        this.add(panel);
    }

    private void loadMovies() {
        if (features == null) {
            System.out.println("Features not set");
            return;
        }

        List<IMovieModel.MRecord> movies = features.getAllMovies();
        tableModel.setRowCount(0);  // Clear existing rows

        for (IMovieModel.MRecord movie : movies) {
            tableModel.addRow(new Object[]{movie.Title(), movie.Year(), movie.Director()});
        }
    }

    private void addSelectedMovieToWatchlist() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) tableModel.getValueAt(selectedRow, 0);
            features.addToWatchList(title);
            JOptionPane.showMessageDialog(this, title + " added to your watchlist!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a movie to add first.");
        }
    }

    private void removeSelectedMovieFromWatchlist() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) tableModel.getValueAt(selectedRow, 0);
            features.removeFromWatchList(title);
            JOptionPane.showMessageDialog(this, title + " removed from your watchlist!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a movie to remove first.");
        }
    }
}
