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

    }

    public static void main(String[] args){

        IMovieModel model = IMovieModel.getInstance();
        IMovieController controller = new Controller(model);
        new GraphView(controller);
    }


}
