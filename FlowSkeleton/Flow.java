package FlowSkeleton;

import javax.swing.*;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;

public class Flow {
	static int frameX;
	static int frameY;
	static FlowPanel fp;
	
	public static void setupGUI(int frameX,int frameY,Terrain landdata, Water waterData) {
		
		Dimension fsize = new Dimension(800, 800);
    	JFrame frame = new JFrame("Waterflow"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout());
    	
      	JPanel g = new JPanel();
		g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
		
		//Adding Timer Label
		JLabel timeElapsed = new JLabel("Steps: 0");
   
		landdata.genPermute();

		fp = new FlowPanel(landdata, waterData, timeElapsed);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		g.add(fp);
	    
		// to do: add a MouseListener, buttons and ActionListeners on those buttons
	   	
		JPanel b = new JPanel();
		b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		
		//Reset Button and Pressed Event
		JButton resetB = new JButton("Reset");
		resetB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fp.resetSimulation();
				System.out.println("reset clicked");
			}
		});

		//Pause Button and pressed Event
		JButton pauseB = new JButton("Pause");
		pauseB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fp.pauseSimulation();
				System.out.println("Pause clicked");
			}
		});

		//Play Button and Pressed Event
		JButton playB = new JButton("Play");
		playB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fp.playSimulation();
				System.out.println("Play clicked");
			}
		});

		//end Button and Pressed Event
		JButton endB = new JButton("End");
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				fp.exitSimulation();
				frame.dispose();
			}
		});
		
		//Mouse Clicked Event Listener
		g.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				fp.addWater(e.getX(), e.getY());
				System.out.println(String.format("Mouse pressed col:%d, row:%d", e.getX(), e.getY()));
			}
		});

		b.add(resetB);
		b.add(Box.createHorizontalGlue());
		b.add(pauseB);
		b.add(Box.createHorizontalGlue());
		b.add(playB);
		b.add(Box.createHorizontalGlue());
		b.add(endB);
		b.add(Box.createHorizontalGlue());
		b.add(timeElapsed);

		g.add(b);
    	
		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
      	frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.setVisible(true);
        Thread fpt = new Thread(fp);
		fpt.start();
	}
	
		
	public static void main(String[] args) {
		Terrain landdata = new Terrain();

		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
			System.exit(0);
		}
				
		// landscape information from file supplied as argument
		landdata.readData(args[0]);
		
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();

		Water waterdata = new Water(frameX, frameY);

		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata, waterdata));
		
		// to do: initialise and start simulation
		

	}
}
