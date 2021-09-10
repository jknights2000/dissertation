package gillespie;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.jfree.ui.RefineryUtilities;
public class Gillespie{

		

		double[] c = {1,0.025};// this is the rate constants
		double[] x = {1,40};// this is the molecule concentration number
		double[]start = {1,40};// this is a copy that is used to reset it once a simulation is done
		
		int m = c.length;//amount of rate constants
		int n =  x.length;//amount of molecule numbers
		boolean nomorereaction = false;
		//a boolean value used to check whether there are no more reactions
		double[] h = new double[m];// this represents hi
		Graph output = new Graph("output","number of molecules vs time");// this makes a graph when a single sim is done
		double T = 100.0;//end of simulation length
		double t = 0.0;//time of current reaction
		
		int[][] L = { {0,0},
					  {0,1}//left side of reaction
		                       };
		int[][] R = {  
				      {0,1},
				      {0,0}//right side
	                           };
		int rows = L.length;
		int cols = L[0].length;
		//rows and col of the cols as seen above
		int V[][] = new int[rows][cols];
		//sets up vector that will contain the overall change vector
		int numberofruns = 10;// number of sim runs there are
		double count = 0;//count value that increases when a state is found
		
		long seed = 123l;
		RandomGenerator randomgen = new JDKRandomGenerator();
		//these are the values that are used make a desired state
		
		float probablity;// Probably value that will be used to calculate the estimate
		int targetitem = 1;
		double targetvalue = 70;
		double targettime = 100;
		//these are the values that are used make a desired state
		
		public void main() {
			randomgen.setSeed(seed);// set up random number
			
			getV();//make overall change vector
			
			output.addToDataSet(x,t);// adds current x value and t value to the graph
			
			count = 0;
			
			for(int k= 1; k <= numberofruns;k++)
			{
				for(int q= 0; q < x.length;q++)
				{
					x[q] = start[q];//resets molecule numbers
					
				}
				System.out.println("run no" + k);//keeps track of reaction number
				t = 0;// reset time
				nomorereaction = false;//no reaction reset
				
			while(t < T && nomorereaction == false)//(9)
			{

				if(x[targetitem] >= targetvalue && t <= targettime )//if target found then
				{
					count = count + 1;//add one to count
					break;
				}
				run();//run algorithm
				output.addToDataSet(x,t);// adds current x value and t value to the graph
			}
			}
			probablity = (float) (count / numberofruns);// get probability from count/ number of runs
			
			System.out.println(t);
			System.out.println(T);
			System.out.println("probablity of count = " + count);//output count
			
			System.out.println("probablity of event = " + probablity);//output probability

		}
		
		public void getV() {
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++) {//sets up overall change vector
				V[i][j] = R[i][j] - L[i][j];
			}
		}
		
		for(int k = 0;k < rows;k++) {
			for(int tr = 0;tr < cols;tr++) {
				System.out.print("{"+V[k][tr]+"} ");//output overall change vector
			}
			System.out.println();
		}
		}
		
		public void run() {
			double sum = 0;
			//reset h0
			for(int i = 0; i < m;i++) {
				calculate(i,x,c);//calculate all the hi
			}
			for(int j = 0; j < m;j++) {
				sum = sum + h[j];//calculate h0
			}
			if(sum == 0)//if no reaction
			{
				nomorereaction = true;
				return;//exit algorithm
			}
			ExponentialDistribution ed = new ExponentialDistribution(1/sum);//(4)
			double tchange = ed.sample();//make time change as a sample value from the ExponentialDistribution with the mean of 1/sum
			t = t + tchange;//time value changed
			int[] trial = new int[m];
			for(int w = 0; w < m;w++)
			{
				trial[w] = w;//adds int value for each reaction
			}
			
			double[] d = new double[m];
			for(int z = 0; z < m;z++)
			{
				d[z] = h[z]/sum; // makes probability.
			}
			//d[i] = h[i]/sum (i = 1,...,m)
			EnumeratedIntegerDistribution eid = new EnumeratedIntegerDistribution(trial,d);//(6)
			int random = eid.sample();
			int next_r = random;
			// enumertatedintergerditribution, picks either 0, 1, 2 based on probability
			for (int i = 0; i < cols;i++)
			{
				x[i] = x[i] + V[next_r][i];//apply reaction
			}
			
		}
		public void calculate(int i, double[] x, double[] c)
		{
			double prod = 1;
			
			for(int nx = 0; nx < n;nx++) {
				double xs = x[nx];//gets molecule value
				//try-catch if catch set to zero
				try
				{
					prod = prod * CombinatoricsUtils.binomialCoefficient((int)xs,L[i][nx]);//calculate prod  using binomial coefficient of the current molecule value and reaction
				}
				catch(MathIllegalArgumentException mae)
				{
					prod = 0.0;//if no reaction prod is 0
					break;
				}
				
			}
			prod = prod * c[i];// prod value calculated using rate constant
			h[i] = prod;// apply to hi
			return;

		}
		public void setNumberofruns(int change) {
			numberofruns = change;//sets number of runs
		}

	}



