package gillespie;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.jfree.ui.RefineryUtilities;

public class Improved {
	double[] c = {1,0.025};// this is the rate constants
	
	double[] x = {1,40};// this is the molecule concentration number
	double[]start = {1,40};// this is a copy that is used to reset it once a simulation is done
	
	double[] gamma = {1.2,(1/1.2)};// this is the value that used to calculate weight
	
	double[] p = new double[c.length];// this is an array that is used to store the bi values
	
	Graph output = new Graph("output 2 this works","number of molecules vs time");// this makes a graph when a single sim is done
		
	double weight = 1;// this is the value used to calculate weight effected count number
	
	int m = c.length;//amount of rate constants
	
	int n =  x.length;//amount of molecule numbers
	int numberofruns = 10;// number of sim runs there are
	double count = 0;//count value that increases when a state is found
	
	float probablity;// Probably value that will be used to calculate the estimate
	
	int targetitem = 1;
	double targetvalue = 70;
	double targettime = 100;
	//these are the values that are used make a desired state
	boolean nomorereaction = false;
	//a boolean value used to check whether there are no more reactions
	
	double[] h = new double[m];
	// this represents hi
	double T = 100.0;
	//end of simulation length
	double t = 0.0;
	//time of current reaction
	double sum1 = 0;
	double sum2 = 0;
	// h0 and b0
	
	
	int[][] L = { {0,0},
				  {0,1},//left side of reaction
	                       };
	int[][] R = { {0,1}, 
			      {0,0},//right side
                           };
	int rows = L.length;
	int cols = L[0].length;
	//rows and col of the cols as seen above
	int V[][] = new int[rows][cols];
	//sets up vector that will contain the overall change vector
	long seed = 123l;
	RandomGenerator randomgen = new JDKRandomGenerator();
	//these are the values that are used make a desired state
	public void main() {
		randomgen.setSeed(seed);// set up random number
		
		count = 0;//count reset
		
		getV();//makes overall change vector
		
		output.addToDataSet(x,t);// adds current x value and t value to the graph
		
		for(int k= 1; k <= numberofruns;k++)
		{
			System.out.println("run number improve " + k);//keeps track of simulation
			t = 0;
			for(int q= 0; q < x.length;q++)
			{
				x[q] = start[q];//resets x value
				
			}
			weight = 1;//weight reset
			nomorereaction = false;//reset no more reaction
			System.out.println();
			
	
			System.out.println();

			while(t < T && nomorereaction == false)//if current time is less than max time and there are still reactions
			{

				if(x[targetitem] >= targetvalue && t <= targettime )//this has to change based on what you are looking for (<= instead of >=)
				{
					count = count + weight;// applies weighted effected value to the count
					//count += 1;
					break;//breaks out the while loop
				}
				run();//run the algorithm
				output.addToDataSet(x,t);//add value to graph
			}
			
		}

		probablity = (float) (count / numberofruns);// get probability from count/ number of runs
		
		System.out.println(t);
		System.out.println(T);
		System.out.println("probablity of count = " + count);//output count
		
		System.out.println("probablity of event = " + probablity);//output probability


	}
	

	public void run() {
		sum1 = 0;
		sum2 = 0;
		//reset h0 and b0
		for(int i = 0; i < m;i++) {
			calculate(i,x,c);//(2)//this where i evaluate aj and bj //update ai(x) and bi(x); recalculate a0(x) and    b0(x)
		}
		
		for(int j = 0; j < m;j++) {
			sum1 = sum1 + h[j];//calculate h0 as a sum of all the hi
			
		}
	
		for(int a = 0; a < m;a++) {
			sum2 = sum2 + p[a];//calculate b0 as a sum of all the bi
			
		}
		
		
		if(sum1 == 0)
		{
			nomorereaction = true;
			return;//if no reaction return
		}
		
		ExponentialDistribution ed = new ExponentialDistribution(1/sum1);//(4)
		double tchange = ed.sample();//make time change as a sample value from the ExponentialDistribution with the mean of 1/sum
		t = t + tchange;//time value changed
		int[] trial = new int[m];
		for(int wee = 0; wee < m;wee++)
		{
			trial[wee] = wee;//adds int value for each reaction
		}
		
		double[] d = new double[m];
		for(int z = 0; z < m;z++)
		{
			d[z] = p[z]/sum2; // makes probability.
		}
		EnumeratedIntegerDistribution eid = new EnumeratedIntegerDistribution(trial,d);// j←smallest integer satisfying   ∑ji=1bi(x)≥r2b0(x).
		
		int random = eid.sample();// gets random reaction number based on the a sample of the EnumeratedIntegerDistribution
		int next_r = random;
		
		weight = weight * (h[random]/p[random]) * (sum2/sum1);// calculate the weight 
	
		for (int i = 0; i < cols;i++)
		{
			x[i] = x[i] + V[next_r][i];//Apply reaction
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
		p[i] = gamma[i] * prod;//j(x)=γjaj(x)  (j=1,…,M),//apply weight
		return;

	}
	
	public void getV() {
	for(int i = 0;i < rows;i++) {
		for(int j = 0;j < cols;j++) {//sets up overall change vector
			V[i][j] = R[i][j] - L[i][j];
		}
	}
	
	for(int k = 0;k < rows;k++) {
		for(int tr = 0;tr < cols;tr++) {
			System.out.print("{"+V[k][tr]+"} ");//prints it out overall change vector
		}
		System.out.println();
	}
	}
	public void setNumberofruns(int change) {
		numberofruns = change;//sets numberof runs
	}
	

}

