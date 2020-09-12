package FlowSkeleton;

public class FlowController extends Thread {
    static Terrain mounTerrain;
    static Water water;

    int startPos;
    int endPos;

    FlowController(int startPos, int endPos){
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public void moveWater(int[] location){
        float minDepth = (water.getDepth(location[0], location[1])/100f) + mounTerrain.height[location[0]][location[1]];
        int[] minDepthLocation = location;

        for(int i = location[0]-1; i <= location[0]+1; i++){
            for(int j = location[1]-1; j <= location[1]+1; j++){
                if(i > 0 && j > 0 && i < mounTerrain.getDimX() && j < mounTerrain.getDimY()){
                    float waterDepth = (water.getDepth(i, j)/100f) + mounTerrain.height[i][j];

                    if(minDepth > waterDepth){
                        minDepth = waterDepth;
                        minDepthLocation[0] = i;
                        minDepthLocation[1] = j;
                    }
                }
            }
        }
        
        water.decrementDepth(location[0], location[1]);

        if(minDepthLocation[0] == 0 || minDepthLocation[1] == 0 || minDepthLocation[0] ==  mounTerrain.getDimX()-1 
            || minDepthLocation[1] == mounTerrain.getDimY()-1)
            water.setDepth(minDepthLocation[0], minDepthLocation[1], 0);
        else
            water.incrementDepth(minDepthLocation[0], minDepthLocation[1]);
    }

    public void run(){
        int[] location = new int[2];

        for(int i = startPos; i < endPos; i++){
            mounTerrain.getPermute(i, location);

            //System.out.println(String.format("index: %d is (%d,%d)",i,location[0],location[1]));
            moveWater(location);
        }
    }
}
