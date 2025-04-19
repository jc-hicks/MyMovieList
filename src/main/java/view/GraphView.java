package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.List;

import model.IMovieModel;
import model.IMovieModel.MRecord;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;

import controller.Controller;
import controller.IMovieController;

public class GraphView  extends JFrame {

    public GraphView(IMovieController controller){

        setTitle("Bar Chart Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        List<MRecord> records = controller.getAllMovies();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String[][] data = new String[(records.size())][2];
        String[] headings = {"Title", "Index"};

        // Add data to a dataset for the chart
        for(int i = 0; i < records.size(); i++){
            double rating = 0;
            try{
                rating = Double.parseDouble(records.get(i).imdbRating());
            } catch (NumberFormatException e){
                System.out.println("Cannot parse ratings to double");
            }
            String title =  records.get(i).Title();
            String indexString = String.valueOf(i);
            dataset.addValue(rating, "Rating", indexString);

            data[i][0] = title;
            data[i][1] = indexString;
        }

        // Create Key panel
        JPanel keyPanel = new JPanel();
        keyPanel.setPreferredSize(new Dimension(400, 700));
        keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));

        // Create JTable for movie list and indices and add to Panel
        JTable movieTable = new JTable(data, headings);
        movieTable.setFillsViewportHeight(true);
        movieTable.setBackground(Color.DARK_GRAY);
        movieTable.setForeground(Color.WHITE);

        movieTable.setGridColor(Color.DARK_GRAY);

        // Enable automatic resizing
        movieTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Set preferred column widths
        movieTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        movieTable.getColumnModel().getColumn(1).setPreferredWidth(10);


        JScrollPane movieScroll = new JScrollPane(movieTable);
        movieScroll.setPreferredSize(new Dimension(400, 700));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                int rows = movieTable.getRowCount();
                int height = movieScroll.getViewport().getHeight();
                if (rows > 0 && height > 0) {
                    int rowHeight = height / rows;
                    movieTable.setRowHeight(rowHeight);
                }
            }
        });


        keyPanel.add(movieScroll);

        // Create Bar Graph
        JFreeChart barGraph = ChartFactory.createBarChart("Ratings Graph", "Movie", "Rating", dataset);

        // Add Components to frame

        // Wrap it in a ChartPanel
        ChartPanel chartPanel = new ChartPanel(barGraph);
        chartPanel.setPreferredSize(new Dimension(800, 700));

        // Add the chart to the frame
        setLayout(new BorderLayout());
        add(keyPanel, BorderLayout.WEST);
        add(chartPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main(String[] args){

        IMovieModel model = IMovieModel.getInstance();
        IMovieController controller = new Controller(model);
        new GraphView(controller);
    }


}
