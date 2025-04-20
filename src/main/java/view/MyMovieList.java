package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import controller.Controller;
import controller.IMovieController;
import model.IMovieModel;

public class MyMovieList extends JFrame {
    private IMovieController controller;
    private JTable movieTable;
    private DefaultTableModel tableModel;
    private JButton loadButton, addToWatchListButton, removeFromWatchListButton, saveOutButton, viewGraph;
    private JButton sortButton, clearButton, filterButton, searchButton, submitApiKeyButton;
    private JComboBox<String> sortColumnCombo, sortOrderCombo, filterFieldCombo;
    private JTextField searchField, filterInput, apiKeyField;
    private JList<String> watchListDisplay;
    private DefaultListModel<String> watchlistModel;
    private JPanel bottomPanel;

    public MyMovieList() {
        super("My Movie List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        initUI();
    }

    public void setController(IMovieController controller) {
        this.controller = controller;
        if (controller instanceof Controller real) {
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

        // === Search Panel & API Key Provisioning ===
        JPanel searchSubPanel = new JPanel();
        searchSubPanel.setBackground(new Color(40, 40, 40));
        searchField = new JTextField(20);
        searchField.setForeground(Color.WHITE);
        searchField.setBackground(new Color(0, 0, 0));
        searchField.setCaretColor(Color.WHITE);
        searchButton = new JButton("Search Movie");
        JLabel searchLabel = new JLabel("Search");

        apiKeyField = new JTextField(10);
        apiKeyField.setForeground(Color.WHITE);
        apiKeyField.setBackground(new Color(0, 0, 0));
        apiKeyField.setCaretColor(Color.WHITE);
        submitApiKeyButton = new JButton("Submit API Key");
        JLabel apiKeyLabel = new JLabel("API Key");

        searchSubPanel.add(apiKeyField);
        searchSubPanel.add(submitApiKeyButton);
        searchSubPanel.add(apiKeyLabel);

        searchSubPanel.add(searchLabel);
        searchSubPanel.add(searchField);
        searchSubPanel.add(searchButton);


        // === Adding ComboBox for operations as it pertains to Filtering ===
        String[] options = {"",">", "<", ">=", "<=", "="};
        JComboBox<String> filterOperation = new JComboBox<>(options);
        filterOperation.setVisible(false);  // initially hidden


        // === Filter Panel ===
        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(new Color(40, 40, 40));
        filterInput = new JTextField(15);
        filterInput.setForeground(Color.WHITE);
        filterInput.setBackground(new Color(60, 60, 60));
        filterButton = new JButton("Filter");
        filterFieldCombo = new JComboBox<>(
                new String[] { "Title", "Year", "Director", "Genre", "Actors", "Rating", "Runtime", "Country" });
        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(filterFieldCombo);
        filterPanel.add(filterOperation);
        filterPanel.add(filterInput);
        filterPanel.add(filterButton);

        // === Sort Panel ===
        JPanel sortSubPanel = new JPanel();
        sortSubPanel.setLayout(new BoxLayout(sortSubPanel, BoxLayout.X_AXIS));
        sortSubPanel.setBackground(new Color(40, 40, 40));
        sortColumnCombo = new JComboBox<>(new String[] { "Year", "Title", "Director", "Rating" });
        sortOrderCombo = new JComboBox<>(new String[] { "Ascending", "Descending" });
        sortButton = new JButton("Sort");
        JLabel sortLabel = new JLabel("Sort by:");
        sortSubPanel.add(sortLabel);
        sortSubPanel.add(sortColumnCombo);
        sortSubPanel.add(sortOrderCombo);
        sortSubPanel.add(sortButton);
        sortSubPanel.add(Box.createHorizontalStrut(50));
        sortSubPanel.add(filterPanel);
        
        topPanel.add(searchSubPanel);
        topPanel.add(sortSubPanel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // === Movie Table ===
        tableModel = new DefaultTableModel(new String[] { "Year", "Title", "Director", "IMDBRating" }, 0);
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


        // === Bottom Button Panel ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(40, 40, 40));
        loadButton = new JButton("Load All Movies");
        addToWatchListButton = new JButton("Add to Watchlist");
        removeFromWatchListButton = new JButton("Remove from Watchlist");
        clearButton = new JButton("Clear Movie Table"); // maybe should be moved to top??
        saveOutButton = new JButton("Save WatchList");
        viewGraph = new JButton("Ratings Graph");
        buttonPanel.add(viewGraph);
        buttonPanel.add(loadButton);
        buttonPanel.add(addToWatchListButton);
        buttonPanel.add(removeFromWatchListButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveOutButton);

        // === Combine Filter + Buttons ===
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(40, 40, 40));
        bottomPanel.add(buttonPanel);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // === Button Styling ===
        Color buttonBg = new Color(70, 130, 180);
        Color buttonFg = Color.WHITE;
        JButton[] allButtons = {
                searchButton, sortButton, clearButton, loadButton, filterButton,
                addToWatchListButton, removeFromWatchListButton, saveOutButton, viewGraph
        };

        for (JButton b : allButtons) {
            // b.setBackground(buttonBg);
            // b.setForeground(buttonFg);
            b.setFocusPainted(false);
            // b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        }

        // === Action Listeners ===
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            System.out.println("🧠 GUI triggered search for: " + query);
            if (!query.isEmpty()) {
                controller.searchMovie(query);
                refreshMovieTable();
            }
        });

        submitApiKeyButton.addActionListener(e -> {
            String query = apiKeyField.getText().trim();
            if (!query.isEmpty()) {
                controller.modelSetAPIKey(query);
            }
            apiKeyField.setText("");
        });

        filterFieldCombo.addActionListener(e -> {
            String field = (String) filterFieldCombo.getSelectedItem();
            if (field.equals("Year") || field.equals("Rating") || field.equals("Runtime")){
                filterOperation.setVisible(true);
            }else{
                filterOperation.setVisible(false);
            }
        });

        filterButton.addActionListener(e -> {

            String field = (String) filterFieldCombo.getSelectedItem();
            String input = "";
            String operation = (String) filterOperation.getSelectedItem();

            if (operation.isEmpty()){
                input = filterInput.getText().trim();
            } else{
                input = operation + " " + filterInput.getText().trim();
            }

            if (!input.isEmpty()) {
                System.out.println("DEBUG: PASSING INPUT: " + input + "PASSING FIELD: " + field);

                List<IMovieModel.MRecord> filtered = controller.filterMovieList(field.toLowerCase(), input);
                tableModel.setRowCount(0);
                for (IMovieModel.MRecord record : filtered) {
                    tableModel.addRow(
                            new Object[] { record.Year(), record.Title(), record.Director(), record.imdbRating() });
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
                                JOptionPane.PLAIN_MESSAGE);
                        if (newRating != null && !newRating.isBlank()) {
                            controller.setMyRating(title, newRating.trim());
                            updateWatchlistPanel();
                        } else {
                            JOptionPane.showMessageDialog(MyMovieList.this, "Invalid rating. Please enter a rating 1-10.");
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
        saveOutButton.addActionListener(e -> saveOut());
        viewGraph.addActionListener(e -> showGraph());

        this.add(mainPanel);
        updateWatchlistPanel();
    }

    private void refreshMovieTable() {
        tableModel.setRowCount(0);
        List<IMovieModel.MRecord> updateMovies = controller.getAllMovies();
        System.out.println("🔄 Refreshing table with " + updateMovies.size() + " records");
        for (IMovieModel.MRecord mRecord : updateMovies) {
            tableModel
                    .addRow(new Object[] { mRecord.Year(), mRecord.Title(), mRecord.Director(), mRecord.imdbRating() });
        }
    }

    private void loadMovies() {
        if (controller == null)
            return;
        List<IMovieModel.MRecord> movies = controller.getAllMovies();
        tableModel.setRowCount(0);
        for (IMovieModel.MRecord movie : movies) {
            tableModel.addRow(new Object[] { movie.Year(), movie.Title(), movie.Director(), movie.imdbRating() });
        }
    }

    private void addSelectedMovieToWatchlist() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            controller.addToWatchList(title);
            controller.saveWatchList();
            JOptionPane.showMessageDialog(this, title + " added to your watchlist!");
            updateWatchlistPanel();
        }
    }

    private void removeSelectedMovieFromWatchlist() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            controller.removeFromWatchList(title);
            controller.saveWatchList();
            JOptionPane.showMessageDialog(this, title + " removed from your watchlist!");
            updateWatchlistPanel();
        }
    }

    private void sortMovieList() {
        if (controller == null)
            return;
        String column = (String) sortColumnCombo.getSelectedItem();
        String order = (String) sortOrderCombo.getSelectedItem();
        String ascOrDesc = order.equals("Ascending") ? "asc" : "desc";
        List<IMovieModel.MRecord> sorted = controller.sortMovieList(column, ascOrDesc);
        tableModel.setRowCount(0);
        for (IMovieModel.MRecord movie : sorted) {
            tableModel.addRow(new Object[] { movie.Year(), movie.Title(), movie.Director(), movie.imdbRating() });
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    private void updateWatchlistPanel() {
        if (controller == null)
            return;
        List<IMovieModel.MRecord> watchlist = controller.getWatchList();
        watchlistModel.clear();
        for (IMovieModel.MRecord record : watchlist) {
            String rating = controller.getMyRating(record.Title());
            String imdb = record.imdbRating();
            String display = (rating != null && !rating.isEmpty())
                    ? String.format("%s (%s, IMDB: %s, My Rating: ⭐ %s)", record.Title(), record.Year(), imdb, rating)
                    : String.format("%s (%s, IMDB: %s)", record.Title(), record.Year(), imdb);
            watchlistModel.addElement(display);
        }
    }

    private void saveOut() {
        if (controller == null)
            return;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Watchlist to File");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON Format", "json"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                final String finalPath = path + ".json";
                @Override
                protected Void doInBackground() throws Exception {
                    controller.saveWatchListToFilepath(finalPath);
                    return null;
                }

                @Override
                protected void done() {
                    JOptionPane.showMessageDialog(MyMovieList.this, "Watchlist saved to " + path);
                }
            };
            worker.execute();
        }
    }

    private void showGraph(){
        if (controller == null)
            return;
        new GraphView(controller);
    }
}
