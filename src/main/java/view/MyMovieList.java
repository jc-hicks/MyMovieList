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
    private JButton clearButton;
    private JList<String> watchListDisplay;
    private DefaultListModel<String> watchlistModel;



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
        tableModel = new DefaultTableModel(new String[]{"Year", "Title", "Director","IMDBRating"}, 0);
        movieTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(movieTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        watchlistModel = new DefaultListModel<>();
        watchListDisplay = new JList<>(watchlistModel);
        JScrollPane watchlistScrollPane = new JScrollPane(watchListDisplay);
        watchlistScrollPane.setBorder(BorderFactory.createTitledBorder("My Movie List"));
        watchlistScrollPane.setPreferredSize(new Dimension(200, 0));
        panel.add(watchlistScrollPane, BorderLayout.EAST);

        // Sort panel layout
        sortColumnCombo = new JComboBox<>(new String[]{"Year", "Title", "Director","IMDBRating"});
        sortOrderCombo = new JComboBox<>(new String[]{"Ascending", "Descending"});
        sortButton = new JButton("Sort");

        // Buttons on the panel
        JPanel buttonPanel = new JPanel();
        loadButton = new JButton("Load Movies");
        addToWatchListButton = new JButton("Add to My Watchlist");
        removeFromWatchListButton = new JButton("Remove from My Watchlist");
        clearButton = new JButton("Clear Table");

        buttonPanel.add(loadButton);
        buttonPanel.add(addToWatchListButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(removeFromWatchListButton);
        buttonPanel.add(clearButton);

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
        clearButton.addActionListener(e -> tableModel.setRowCount(0));
        clearButton.addActionListener(e -> clearTable());

        this.add(panel);
        updateWatchlistPanel();
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
                    movie.Title(),
                    movie.Director(),
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
            updateWatchlistPanel();
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
                    movie.Year(),
                    movie.Title(),
                    movie.Director(),
                    movie.imdbRating()
            });
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);  // Clears all table rows
    }


    private void updateWatchlistPanel() {
        if (features == null) return;

        List<IMovieModel.MRecord> watchlist = features.getWatchList();
        watchlistModel.clear();

        for (IMovieModel.MRecord record : watchlist) {
            watchlistModel.addElement(record.Title());
        }
    }
}
