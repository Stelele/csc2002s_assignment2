package FlowSkeleton;

import java.util.concurrent.ForkJoinPool;

public class FlowController extends Thread {
    static Terrain mounTerrain;
    static Water water;

    static final ForkJoinPool fjpool = new ForkJoinPool();

    int startPos;
    int endPos;

    FlowController(int startPos, int endPos){
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public void run(){
        FlowControllerUnit.mounTerrain = mounTerrain;
        FlowControllerUnit.water = water;

        fjpool.invoke(new FlowControllerUnit(startPos, endPos));
    }
}
