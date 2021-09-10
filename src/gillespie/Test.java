package gillespie;

import org.jfree.ui.RefineryUtilities;

public class Test {
	public static void main(String[] args) {
		Gillespie g = new Gillespie();
		Improved im = new Improved();
		Improved im2 = new Improved();
		
		g.setNumberofruns(1);
		g.main();
		g.output.pack( );
	    RefineryUtilities.centerFrameOnScreen( g.output );
	    g.output.setVisible( true );//run base once
	    
	    
	    for (int i = 0; i < im.gamma.length;i++)
	    {
	    	im.gamma[i] = 1;
	    }
	    
		im.setNumberofruns(1);
		im.main();
		im.output.pack( );
	    RefineryUtilities.centerFrameOnScreen( im.output );
	    im.output.setVisible( true );//run improved (gamma set to {1,1} which is the same as base) once
	    
		im2.setNumberofruns(1);
		im2.main();
		im2.output.pack( );
	    RefineryUtilities.centerFrameOnScreen( im2.output );
	    im2.output.setVisible( true );//run improved once
	    
	    
	}
}

