package gillespie;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.ValueAxis;
public class Estimategraph extends ApplicationFrame {


		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	   public Estimategraph( String applicationTitle , String chartTitle) {
	      super(applicationTitle);
	      JFreeChart lineChart = ChartFactory.createLineChart(
	         chartTitle,
	         "Simulation runs","Probablity",
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
	 void addToDataSet(double[] estimate, int i) {
		 for(int j = 0; j < estimate.length;j++) {
			 String ident;
			 if (j == 0){
				 ident = "base";
			 }else
			 {
				 ident = "weighted";
			 }//figuers out which estimate is being added
			 String runs = "10^" + i;
			 dataset.addValue(estimate[j], ident, runs);//adds esitmate to graph
		 }
	 }
	   

	}

