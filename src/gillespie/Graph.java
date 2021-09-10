package gillespie;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.ValueAxis;

public class Graph extends ApplicationFrame {
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
   public Graph( String applicationTitle , String chartTitle) {
      super(applicationTitle);
      JFreeChart lineChart = ChartFactory.createLineChart(
         chartTitle,
         "Time when reaction occur(random increase)","Concentration",
         createDataset(),
         PlotOrientation.VERTICAL,
         true,true,false);//sets up graph
      ChartPanel chartPanel = new ChartPanel( lineChart );//sets up panel with graph in it
      chartPanel.setPreferredSize( new java.awt.Dimension( 10000 , 10000 ) );//set size
      setContentPane( chartPanel );//sets up panel
   }

   private DefaultCategoryDataset createDataset() {
      return dataset;//return dataset, allows chart to be made
   }
 void addToDataSet(double[]input, double t) {
	 for(int i = 0; i < input.length;i++) {
		 String ident = "reagent" + i;
		 dataset.addValue(input[i], ident, String.valueOf((int)t));//makes lines what molecule it is
	 }
 }
   

}