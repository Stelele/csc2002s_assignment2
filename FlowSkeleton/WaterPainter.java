package FlowSkeleton;

import java.util.concurrent.RecursiveAction;

import java.awt.Color;
import java.awt.Graphics;

/**
 * class responsimble for painting water possitions on GUI
 */
public class WaterPainter extends RecursiveAction {
    static Graphics g;
    static Terrain land;
    static Water water;
    static final int SEQUENTIAL_CUTOFF = 100;

    private int start;
    private int end;

    /**
     * 
     * @param start starting possition of Terrain perm list
     * @param end ending possition of Terrain perm list
     */
    WaterPainter(int start,int end){
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute(){
        if(end - start <= SEQUENTIAL_CUTOFF){
            int[] location = new int[2];
            g.setColor(Color.BLUE);

            for(int i = start; i < end; i++){
                land.getPermute(i, location);
    
                if(water.getDepth(location[0], location[1]) > 0){				
                    g.fillRect(location[0], location[1], 1, 1);
                }
            }
        } else{
            int mid = (start + end) / 2;

            WaterPainter left = new WaterPainter(start, mid);
            WaterPainter right = new WaterPainter(mid, end);

            left.fork();
            right.compute();

            left.join();
        }
    }
}
