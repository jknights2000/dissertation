package gillespie;
import java.lang.Math;

import org.jfree.ui.RefineryUtilities;
public class compare {
	static double estimate[] = new double[2];
	public static void main(String[] args) {
		Improved im = new Improved();
		Gillespie g = new Gillespie();
		Estimategraph eg = new Estimategraph("Output","estimate of the probablity of rare events");
		for(int i=1; i <= 6;i++)
		{
			double numberofruns = 10;
			double change = Math.pow(numberofruns, i);//sets number of runs to be 10 ^ i
			g.setNumberofruns((int)change);//set number of runs in base
			im.setNumberofruns((int)change);//set number of runs in improved
			g.main();//run base
			im.main();//run improved
			estimate[0] = g.probablity;
			estimate[1] = im.probablity;
			//get probabilities
			eg.addToDataSet(estimate, i);//add estimates to graph
		}
		eg.pack( );
	      RefineryUtilities.centerFrameOnScreen( eg );
	      eg.setVisible( true );//shows graph

	}
	}
