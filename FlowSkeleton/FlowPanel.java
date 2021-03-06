package FlowSkeleton;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ForkJoinPool;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * class responsible for refreshing the view and starting handling the moving water simulation
 */
public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	Water water; 
	JLabel timelapsed;

	private boolean play;
	private boolean exit;
	private boolean pauseClicked;

	static long startTime;
	long timeBeforePause;

	static final ForkJoinPool fjpool = new ForkJoinPool();
	
	/**
	 * 
	 * @param terrain underlying terrain model
	 * @param waterData underlying water model
	 * @param timelapsed timestep label shown on GUI
	 */
	FlowPanel(Terrain terrain, Water waterData, JLabel timelapsed) {
		land = terrain;
		water = waterData;
		exit = false;
		this.timelapsed = timelapsed;

		timeBeforePause = 0;
		pauseClicked = false;
	}

	// start timer
	/**
	 * Record time of when the play button was clicked
	 */
	private static void tick(){
		startTime = 0;
	}
	
	// stop timer, return time elapsed in seconds
	/**
	 * 
	 * @return starttime += 1
	 */
	private static long tock(){
		startTime += 1;
		return startTime; 
	}
		
	// responsible for painting the terrain and water
	// as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the landscape in greyscale as an image
		if (land.getImage() != null){
			g.drawImage(land.getImage(), 0, 0, null);
		}

		//draw blue boxes ontop reprsenting water
		WaterPainter.g = g;
		WaterPainter.land = land;
		WaterPainter.water = water;

		fjpool.invoke(new WaterPainter(0, land.dim()));
	}

	/**
	 * Pauses simulation when Pause button is clicked
	 */
	public void pauseSimulation(){
		play = false;
		pauseClicked = true;
	}

	/**
	 * Starts simulation when Play button is clicked
	 */
	public void playSimulation(){
		tick();
		play = true;
	}

	/**
	 * End simulation 
	 */
	public void exitSimulation(){
		exit = true;
	}

	/**
	 * Resets the simulation 
	 */
	public void resetSimulation(){
		tick();
		timeBeforePause = 0;

		timelapsed.setText("Steps: 0");
		water.clearWater();
		repaint();
	}
	
	/**
	 * Function that adds water at clicked terrain possition
	 * @param x clicked column location
	 * @param y clicked row location
	 */
	public void addWater(int x, int y){
		int width = land.getDimX();
		int height = land.getDimY();
		for(int i = x-5; i < x+5; i++){
			for(int j = y-5; j < y+5; j++){
				if(i < width && j < height && i>=0 && j>=0)
					water.incrementDepth(i, j);
			}
		}
		repaint();
	}

	/**
	 * Remove all water in Water model
	 */
	public void clearWater(){
		water.clearWater();
		repaint();
	}

	/**
	 * Method run by starting thread
	 */
	public void run() {	

		int linearPermListSize = land.dim();
		int numThreads = 4;
		int workingSize = linearPermListSize/numThreads;

		FlowController.mounTerrain = land;
		FlowController.water = water;

		repaint();
		try{
			while(!exit){
				if(play){
					FlowController firstQuarter = new FlowController( 0, workingSize);
					FlowController secondQuarter = new FlowController(workingSize, 2*workingSize);
					FlowController thirdQuarter = new FlowController(2*workingSize, 3*workingSize);
					FlowController fourthQuarter = new FlowController(3*workingSize, linearPermListSize);

					firstQuarter.start();
					secondQuarter.start();
					thirdQuarter.start();
					fourthQuarter.start();
					
					firstQuarter.join();
					secondQuarter.join();
					thirdQuarter.join();
					fourthQuarter.join();

					timelapsed.setText(String.format("Steps: %d", tock() + timeBeforePause));

					repaint();
				
				}

				if(pauseClicked){
					timeBeforePause += startTime;
					pauseClicked = false;
				}
					
			}
		} catch(InterruptedException e){
			System.err.println(e);
		}
	}
}