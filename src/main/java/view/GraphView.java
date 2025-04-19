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

import controller.Controller;
import controller.IMovieController;

public class GraphView  extends JFrame {

    public GraphView(IMovieController controller){

        setTitle("Bar Chart Example");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        List<MRecord> records = controller.getAllMovies();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add data to a dataset for the chart
        for(MRecord record : records){
            double rating = 0;
            try{
                rating = Double.parseDouble(record.imdbRating());
            } catch (NumberFormatException e){
                System.out.println("Cannot parse ratings to double");
            }
            dataset.addValue(rating, "Rating", record.Title());
        }

        // Create Graph
        JFreeChart barGraph = ChartFactory.createBarChart("Ratings Graph", "Movie", "Rating", dataset);

        // Add graph to frame

        // Wrap it in a ChartPanel
        ChartPanel chartPanel = new ChartPanel(barGraph);
        chartPanel.setPreferredSize(new java.awt.Dimension(200, 100));

        // Add the chart to the frame
        setContentPane(chartPanel);
        setVisible(true);

    }

    public static void main(String[] args){

        IMovieModel model = IMovieModel.getInstance();
        IMovieController controller = new Controller(model);
        new GraphView(controller);
    }


}
