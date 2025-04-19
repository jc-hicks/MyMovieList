package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.List;

import model.IMovieModel;
import model.IMovieModel.MRecord;
import javax.swing.JFrame;

import controller.Controller;
import controller.IMovieController;

public class GraphView  extends JFrame {

    public GraphView(IMovieController controller){
        JFrame frame = new JFrame("Ratings Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

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

        frame.setVisible(true);

    }

    public static void main(String[] args){

        IMovieModel model = IMovieModel.getInstance();
        IMovieController controller = new Controller(model);
        new GraphView(controller);
    }


}
