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
    private JComboBox<String> sortColumnCombo;
    private JComboBox<String> sortOrderCombo;
    private JButton sortButton;


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

    /**
     *
     * @param features
     */
    public void setFeatures(IMovieFeatures features) {
        this.features = features;
    }

    /**
     *
     */
    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Table layout
        tableModel = new DefaultTableModel(new String[]{"Title", "Year", "Director", "Genre", "imdbRating"}, 0);
        movieTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(movieTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Sort panel layout
        sortColumnCombo = new JComboBox<>(new String[]{"Title", "Year", "ImdbRating"});
        sortOrderCombo = new JComboBox<>(new String[]{"Ascending", "Descending"});
        sortButton = new JButton("Sort");

        // Buttons on the panel
        JPanel buttonPanel = new JPanel();
        loadButton = new JButton("Load Movies");
        addToWatchListButton = new JButton("Add to My Watchlist");
        removeFromWatchListButton = new JButton("Remove from My Watchlist");

        buttonPanel.add(loadButton);
        buttonPanel.add(addToWatchListButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(removeFromWatchListButton);

        sortPanel.add(new JLabel("Sort by: "));
        sortPanel.add(sortColumnCombo);
        sortPanel.add(sortOrderCombo);
        sortPanel.add(sortButton);

        panel.add(sortPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Button functionality
        loadButton.addActionListener(e -> loadMovies());
        addToWatchListButton.addActionListener(e -> addSelectedMovieToWatchlist());
        removeFromWatchListButton.addActionListener(e -> removeSelectedMovieFromWatchlist());
        sortButton.addActionListener(e -> sortMovieList());

        this.add(panel);
    }

    /**
     *
     */
    private void loadMovies() {
        if (features == null) {
            System.out.println("Features not set");
            return;
        }

        List<IMovieModel.MRecord> movies = features.getAllMovies();
        tableModel.setRowCount(0);  // Clear existing rows

        for (IMovieModel.MRecord movie : movies) {
            tableModel.addRow(new Object[]{
                    movie.Year(),
                    movie.Director(),
                    movie.Genre(),
                    movie.imdbRating()
            });
        }
    }

    /**
     *
     */
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

    /**
     *
     */
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

    private void sortMovieList() {
        if (features == null) return;

        String column = (String) sortColumnCombo.getSelectedItem();
        String order = (String) sortOrderCombo.getSelectedItem();
        String ascOrDesc = order.equals("Ascending") ? "asc" : "desc";

        // Use features in the interface to perform sort
        List<IMovieModel.MRecord> sorted = features.sortMovieList(column, ascOrDesc);

        tableModel.setRowCount(0);

        for (IMovieModel.MRecord movie : sorted) {
            tableModel.addRow(new Object[]{
                    movie.Title(),
                    movie.Year(),
                    movie.Director(),
                    movie.Genre(),
                    movie.imdbRating()
            });
        }
    }
}
