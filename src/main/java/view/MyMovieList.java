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
    private JButton loadButton, addToWatchListButton, removeFromWatchListButton;
    private JButton sortButton, clearButton, filterButton, searchButton;
    private JComboBox<String> sortColumnCombo, sortOrderCombo, filterFieldCombo;
    private JTextField searchField, filterInput;
    private JList<String> watchListDisplay;
    private DefaultListModel<String> watchlistModel;
    private JPanel bottomPanel;

    public MyMovieList() {
        super("My Movie List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        initUI();
    }

    public void setFeatures(IMovieFeatures features) {
        this.features = features;
        if (features instanceof RealMovieFeatures real) {
            real.loadWatchlistOnStartup();
        }
        updateWatchlistPanel();
    }

    private void initUI() {
        UIManager.put("Label.foreground", Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));

        // === Combined Top Panel ===
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(40, 40, 40));

        // === Search Panel ===
        JPanel searchSubPanel = new JPanel();
        searchSubPanel.setBackground(new Color(40, 40, 40));
        searchField = new JTextField(20);
        searchField.setForeground(Color.WHITE);
        searchField.setBackground(new Color(60, 60, 60));
        searchField.setCaretColor(Color.WHITE);
        searchButton = new JButton("Search API");
        JLabel searchLabel = new JLabel("Search");
        searchSubPanel.add(searchLabel);
        searchSubPanel.add(searchField);
        searchSubPanel.add(searchButton);

        // === Sort Panel ===
        JPanel sortSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortSubPanel.setBackground(new Color(40, 40, 40));
        sortColumnCombo = new JComboBox<>(new String[]{"Year", "Title", "Director", "IMDBRating"});
        sortOrderCombo = new JComboBox<>(new String[]{"Ascending", "Descending"});
        sortButton = new JButton("Sort");
        JLabel sortLabel = new JLabel("Sort by:");
        sortSubPanel.add(sortLabel);
        sortSubPanel.add(sortColumnCombo);
        sortSubPanel.add(sortOrderCombo);
        sortSubPanel.add(sortButton);

        topPanel.add(searchSubPanel);
        topPanel.add(sortSubPanel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // === Movie Table ===
        tableModel = new DefaultTableModel(new String[]{"Year", "Title", "Director", "IMDBRating"}, 0);
        movieTable = new JTable(tableModel);
        movieTable.setForeground(Color.WHITE);
        movieTable.setBackground(new Color(40, 40, 40));
        movieTable.setSelectionBackground(new Color(70, 130, 180));
        movieTable.setGridColor(new Color(100, 100, 100));
        JScrollPane tableScrollPane = new JScrollPane(movieTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // === Watchlist Panel ===
        watchlistModel = new DefaultListModel<>();
        watchListDisplay = new JList<>(watchlistModel);
        watchListDisplay.setForeground(new Color(173, 216, 230));
        watchListDisplay.setBackground(Color.BLACK);
        watchListDisplay.setSelectionBackground(new Color(100, 149, 237));
        JScrollPane watchlistScrollPane = new JScrollPane(watchListDisplay);
        watchlistScrollPane.setBorder(BorderFactory.createTitledBorder("My Watchlist"));
        watchlistScrollPane.setPreferredSize(new Dimension(400, 0));
        mainPanel.add(watchlistScrollPane, BorderLayout.EAST);

        // === Filter Panel ===
        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(new Color(40, 40, 40));
        filterInput = new JTextField(15);
        filterInput.setForeground(Color.WHITE);
        filterInput.setBackground(new Color(60, 60, 60));
        filterButton = new JButton("Filter");
        filterFieldCombo = new JComboBox<>(new String[]{"Title", "Year", "Director", "Genre", "Actors", "Rating", "Runtime", "Country"});
        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(filterFieldCombo);
        filterPanel.add(filterInput);
        filterPanel.add(filterButton);

        // === Bottom Button Panel ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(40, 40, 40));
        loadButton = new JButton("Load All Movies");
        addToWatchListButton = new JButton("Add to Watchlist");
        removeFromWatchListButton = new JButton("Remove from Watchlist");
        clearButton = new JButton("Clear Movie Table");   // maybe should be moved to top??
        buttonPanel.add(loadButton);
        buttonPanel.add(addToWatchListButton);
        buttonPanel.add(removeFromWatchListButton);
        buttonPanel.add(clearButton);

        // === Combine Filter + Buttons ===
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(40, 40, 40));
        bottomPanel.add(filterPanel);
        bottomPanel.add(buttonPanel);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // === Button Styling ===
        Color buttonBg = new Color(70, 130, 180);
        Color buttonFg = Color.WHITE;
        JButton[] allButtons = {
                searchButton, sortButton, clearButton, loadButton, filterButton,
                addToWatchListButton, removeFromWatchListButton
        };
        for (JButton b : allButtons) {
            b.setBackground(buttonBg);
            b.setForeground(buttonFg);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        }

        // === Action Listeners ===
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            System.out.println("🧠 GUI triggered search for: " + query);
            if (!query.isEmpty()) {
                features.searchMovie(query);
                refreshMovieTable();
            }
        });

        filterButton.addActionListener(e -> {
            String field = (String) filterFieldCombo.getSelectedItem();
            String input = filterInput.getText().trim();
            if (!input.isEmpty()) {
                List<IMovieModel.MRecord> filtered = features.filterMovieList(field.toLowerCase(), input);
                tableModel.setRowCount(0);
                for (IMovieModel.MRecord record : filtered) {
                    tableModel.addRow(new Object[]{record.Year(), record.Title(), record.Director(), record.imdbRating()});
                }
            }
        });

        // === Mouse Right-Click Add Rating ===
        watchListDisplay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    int index = watchListDisplay.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        String selected = watchListDisplay.getModel().getElementAt(index);
                        String title = selected.split(" \\(")[0];
                        String newRating = JOptionPane.showInputDialog(
                                MyMovieList.this,
                                "Enter your personal rating for: " + title,
                                "Rate Movie",
                                JOptionPane.PLAIN_MESSAGE
                        );
                        if (newRating != null && !newRating.isBlank()) {
                            features.setMyRating(title, newRating.trim());
                            updateWatchlistPanel();
                        }
                    }
                }
            }
        });


        loadButton.addActionListener(e -> loadMovies());
        addToWatchListButton.addActionListener(e -> addSelectedMovieToWatchlist());
        removeFromWatchListButton.addActionListener(e -> removeSelectedMovieFromWatchlist());
        sortButton.addActionListener(e -> sortMovieList());
        clearButton.addActionListener(e -> clearTable());

        this.add(mainPanel);
        updateWatchlistPanel();
    }

    private void refreshMovieTable() {
        tableModel.setRowCount(0);
        List<IMovieModel.MRecord> updateMovies = features.getAllMovies();
        System.out.println("🔄 Refreshing table with " + updateMovies.size() + " records");
        for (IMovieModel.MRecord mRecord : updateMovies) {
            tableModel.addRow(new Object[]{mRecord.Year(), mRecord.Title(), mRecord.Director(), mRecord.imdbRating()});
        }
    }

    private void loadMovies() {
        if (features == null) return;
        List<IMovieModel.MRecord> movies = features.getAllMovies();
        tableModel.setRowCount(0);
        for (IMovieModel.MRecord movie : movies) {
            tableModel.addRow(new Object[]{movie.Year(), movie.Title(), movie.Director(), movie.imdbRating()});
        }
    }

    private void addSelectedMovieToWatchlist() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            features.addToWatchList(title);
            features.saveWatchList();
            JOptionPane.showMessageDialog(this, title + " added to your watchlist!");
            updateWatchlistPanel();
        }
    }

    private void removeSelectedMovieFromWatchlist() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            features.removeFromWatchList(title);
            features.saveWatchList();
            JOptionPane.showMessageDialog(this, title + " removed from your watchlist!");
            updateWatchlistPanel();
        }
    }

    private void sortMovieList() {
        if (features == null) return;
        String column = (String) sortColumnCombo.getSelectedItem();
        String order = (String) sortOrderCombo.getSelectedItem();
        String ascOrDesc = order.equals("Ascending") ? "asc" : "desc";
        List<IMovieModel.MRecord> sorted = features.sortMovieList(column, ascOrDesc);
        tableModel.setRowCount(0);
        for (IMovieModel.MRecord movie : sorted) {
            tableModel.addRow(new Object[]{movie.Year(), movie.Title(), movie.Director(), movie.imdbRating()});
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    private void updateWatchlistPanel() {
        if (features == null) return;
        List<IMovieModel.MRecord> watchlist = features.getWatchList();
        watchlistModel.clear();
        for (IMovieModel.MRecord record : watchlist) {
            String rating = features.getMyRating(record.Title());
            String imdb = record.imdbRating();
            String display = (rating != null && !rating.isEmpty()) ?
                    String.format("%s (%s, IMDB: %s, My Rating: ⭐ %s)", record.Title(), record.Year(), imdb, rating) :
                    String.format("%s (%s, IMDB: %s)", record.Title(), record.Year(), imdb);
            watchlistModel.addElement(display);
        }
    }
}
