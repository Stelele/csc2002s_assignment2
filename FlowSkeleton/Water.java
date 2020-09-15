package FlowSkeleton;

/**
 * Class that contains underlying water depth information
 */
public class Water {
    private int[][] depth;

    /**
     * created 2 dimension array of size [dimx][dimy]
     * @param dimx grid width 
     * @param dimy grid height
     */
    Water(int dimx,int dimy){
        depth = new int[dimy][dimx];
    }

    /**
     * Get depth a point
     * @param x column position
     * @param y row position
     * @return water depth at position x,y
     */
    public synchronized int getDepth(int x, int y){
        return depth[x][y];
    }

    /**
     * Sets the depth a point
     * @param x colum position
     * @param y row position
     * @param newDepth  new water depth at point
     */
    public synchronized void setDepth(int x, int y, int newDepth){
        depth[x][y] = newDepth;
    }

    /**
     * decrease depth at point by 1
     * @param x column position
     * @param y row position
     */
    public synchronized void decrementDepth(int x, int y){
        depth[x][y]--;
    }

    /**
     * increases depth at point by 1
     * @param x column position
     * @param y row position
     */
    public synchronized void incrementDepth(int x, int y){
        depth[x][y]++;
    }

    /**
     * Clears entire array and sets all depths to 0
     */
    public synchronized void clearWater(){
        depth = new int[depth.length][depth[0].length];
    }
}
