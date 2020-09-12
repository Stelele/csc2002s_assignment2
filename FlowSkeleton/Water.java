package FlowSkeleton;

public class Water {
    private int[][] depth;

    Water(int dimx,int dimy){
        depth = new int[dimy][dimx];

        /*for(int x = 0; x < dimx; x++){
            for(int y = 0; y < dimy; y++){
                depth[x][y] = 0;
            }
        }*/
    }

    public synchronized int getDepth(int x, int y){
        return depth[x][y];
    }

    public synchronized void setDepth(int x, int y, int newDepth){
        depth[x][y] = newDepth;
    }

    public synchronized void decrementDepth(int x, int y){
        depth[x][y]--;
    }

    public synchronized void incrementDepth(int x, int y){
        depth[x][y]++;
    }

    public synchronized void clearWater(){
        depth = new int[depth.length][depth[0].length];
    }
}
