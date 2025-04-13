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
        this.setSize(900, 700);
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
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton searchButtonOne = new JButton(" \uD83D\uDD0D Search");
        JPanel topPanel = new JPanel();

        // Table layout
        tableModel = new DefaultTableModel(new String[]{"Year", "Title", "Director","IMDBRating"}, 0);
        movieTable = new JTable(tableModel);
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem rateItem = new JMenuItem("Rate this movie");
        JScrollPane scrollPane = new JScrollPane(movieTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        watchlistModel = new DefaultListModel<>();
        watchListDisplay = new JList<>(watchlistModel);
        JScrollPane watchlistScrollPane = new JScrollPane(watchListDisplay);
        watchlistScrollPane.setBorder(BorderFactory.createTitledBorder("My Movie List"));
        watchlistScrollPane.setPreferredSize(new Dimension(400, 0));
        panel.add(watchlistScrollPane, BorderLayout.EAST);

        // top panel
        topPanel.add(new JLabel("Search"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        panel.add(topPanel, BorderLayout.NORTH);

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
        rateItem.addActionListener(e -> rateSelectedMovie());
        popupMenu.add(rateItem);
        movieTable.setComponentPopupMenu(popupMenu);
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                features.searchMovie(query);    // calls API or model
                refreshMovieTable();            // show new results
            }
        });

        this.add(panel);
        updateWatchlistPanel();
    }

    private void refreshMovieTable() {
        tableModel.setRowCount(0);

        List <IMovieModel.MRecord> updateMovies = features.getAllMovies();

        for (IMovieModel.MRecord mRecord : updateMovies) {
            tableModel.addRow(new Object[]{
                    mRecord.Year(),
                    mRecord.Title(),
                    mRecord.Director(),
                    mRecord.imdbRating()
            });
        }
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
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String imdbRating = (String) tableModel.getValueAt(selectedRow, 3);
            String year = (String) tableModel.getValueAt(selectedRow, 0);
            String display = title + " (" + year + ", * " + imdbRating + ")";
            System.out.println("Adding to watchlist: " + display);

            features.addToWatchList(title);
            features.saveWatchList();
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
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String imdbRating = (String) tableModel.getValueAt(selectedRow, 3);
            String display = title + " (" + imdbRating + ")";
            features.removeFromWatchList(title);
            features.saveWatchList();
            JOptionPane.showMessageDialog(this, title + " removed from your watchlist!");
            updateWatchlistPanel();
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
            String rating = features.getMyRating(record.Title());
            String imdb = record.imdbRating();
            String display;
            if (rating != null && !rating.isEmpty()) {
                display = String.format("%s (%s, IMDB: %s, My Rating: ⭐ %s)",
                        record.Title(), record.Year(), imdb, rating);
            } else {
                display = String.format("%x (%s, IMDB: %s)",
                        record.Title(), record.Year(), imdb);
            }
            watchlistModel.addElement(display);
        }
    }

    private void rateSelectedMovie() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) tableModel.getValueAt(selectedRow, 1);

            String rating = JOptionPane.showInputDialog(
                    this,
                    "Enter your rating for \"" + title + "\":",
                    "Rate Movie",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (rating != null && !rating.trim().isEmpty()) {
                features.setMyRating(title, rating); // controller/model handles this
                JOptionPane.showMessageDialog(this,
                        "You rated \"" + title + "\" with " + rating + "*");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a movie first.");
                // The controller needs to have this: void setMyRating(String title, String rating);
            }
        }
    }
}
