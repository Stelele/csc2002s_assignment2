package FlowSkeleton;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	Water water; 
	boolean play;
	boolean reset;
	boolean exit;
	boolean resumePossible;
	
	FlowPanel(Terrain terrain, Water waterData) {
		land = terrain;
		water = waterData;
		play = false;
		resumePossible = false;

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

		g.setColor(Color.BLUE);
		int[] location = new int[2];

		int sizes = width * height; 
	
		for(int i = 0; i < sizes; i++){
			land.locate(i, location);

			if(water.getDepth(location[0], location[1]) > 0){				
				g.fillRect(location[0], location[1], 1, 1);
			}
		}
	}

	public void pauseSimulation(){
		play = false;
	}

	public void playSimulation(){
		play = true;
	}

	public void exitSimulation(){
		exit = true;
	}
	
	public void addWater(int x, int y){
		int width = getWidth();
		int height = getHeight();
		for(int i = x-5; i < x+5; i++){
			for(int j = y-5; j < y+5; j++){
				if(i < width && j < height)
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
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting
		int linearPermListSize = land.dim();
		int numThreads = 4;
		int workingSize = linearPermListSize/numThreads;

		FlowController.mounTerrain = land;
		FlowController.water = water;

		FlowController firstQuarter = new FlowController(0, workingSize);
		FlowController secondQuarter = new FlowController(workingSize, 2*workingSize);
		FlowController thirdQuarter = new FlowController(2*workingSize, 3*workingSize);
		FlowController fourthQuarter = new FlowController(3*workingSize, linearPermListSize);

		boolean firstRun = true;

		repaint();
		try{
			while(true){
				if(play){

					firstQuarter.start();
					secondQuarter.start();
					thirdQuarter.start();
					fourthQuarter.start();

					firstQuarter.join();
					secondQuarter.join();
					thirdQuarter.join();
					fourthQuarter.join();

					repaint();

				} else if(!play && !resumePossible && !firstRun){
					
					firstQuarter.wait();
					secondQuarter.wait();
					thirdQuarter.wait();
					fourthQuarter.wait();

					resumePossible = true;
					
				}
				
				if(exit){
					return;
				}
			}
		} catch(InterruptedException e){
			System.err.println(e);
		}
	}
}