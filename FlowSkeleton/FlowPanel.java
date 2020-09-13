package FlowSkeleton;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ForkJoinPool;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	Water water; 
	JLabel timelapsed;

	private boolean play;
	private boolean exit;
	private boolean pauseClicked;

	static long startTime;
	float timeBeforePause;

	static final ForkJoinPool fjpool = new ForkJoinPool();
	
	FlowPanel(Terrain terrain, Water waterData, JLabel timelapsed) {
		land = terrain;
		water = waterData;
		exit = false;
		this.timelapsed = timelapsed;

		timeBeforePause = 0f;
		pauseClicked = false;
	}

	// start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
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

	public void pauseSimulation(){
		play = false;
		pauseClicked = true;
	}

	public void playSimulation(){
		tick();
		play = true;
	}

	public void exitSimulation(){
		exit = true;
	}

	public void resetSimulation(){
		tick();
		timeBeforePause = 0f;

		timelapsed.setText("Time: 0.00s");
		water.clearWater();
		repaint();
	}
	
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

	public void clearWater(){
		water.clearWater();
		repaint();
	}

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

					timelapsed.setText(String.format("Time: %.2fs", tock() + timeBeforePause));

					repaint();
				
				}

				if(pauseClicked){
					timeBeforePause += tock();
					pauseClicked = false;
				}
					
			}
		} catch(InterruptedException e){
			System.err.println(e);
		}
	}
}