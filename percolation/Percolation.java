import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;                    // N-by-N grid;
    private int num ;                  // number of the open sites
    private int [][] grid;
    private WeightedQuickUnionUF uf;  //uf

    // creates n-by-n grid, with all sites initially blocked
    // total n*n+2 sites
    public Percolation(int n){
        if(n<=0){
            throw new IllegalArgumentException(" Cow or Col argument error!");
        }
        N = n;
        num = 0;
        grid = new int[n][n]; // 0:blocked; 1:opened; 2:fulled
        uf = new WeightedQuickUnionUF(n*n+2);
        // 0 is the top virtual site;
        // 1-n*n are the gird sites;
        // n*n+1 is the bottom virtual site
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                grid[i][j] = 0;
            }
        }
    }

    // check boundary row and col must be [1,N]
    private void checkBoundary(int row, int col){
        if( row < 1 || row > N || col < 1 || col >N){
            throw new IllegalArgumentException(" Cow or Col argument error!");
        }
        return;
    }

    // get Index of uf.parent[]
    private int getIndex(int row,int col){
        checkBoundary(row, col);
        return (row - 1) * N + col;
    }

    // connect the open site to its neighbors
    private void connectNeighbor(int row, int col, int Index){
        // 首行节点 open之后 与虚拟节点0 union;
        // 末行节点 open之后 与虚拟节点n*n+1 union;
        // 其余节点检查上下左右邻居，注意左右边界
        // 每次检查是否percolate只需要检查 两个虚拟节点的连通性

        // 向上连接
        if(row - 1 <= 0){ //首行
            uf.union(0, Index);
        }else{
            if(grid[row-2][col-1]!=0){
                uf.union(Index, Index - N);
            }
        }

        // 向右连接
        if(col + 1 <= N){// 界内
            if(grid[row-1][col]!=0){
                uf.union(Index, Index+1);
            }
        }

        // 向下连接
        if(row + 1 > N){ // 末行
            uf.union(N*N+1, Index);
        }else{
            if(grid[row][col-1]!=0){
                uf.union(Index, Index + N);
            }
        }

        // 向左连接
        if(col - 1 >= 1){ // 界内
            if(grid[row-1][col-2]!=0){
                uf.union(Index, Index-1);
            }
        }

    }
    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        int Index = getIndex(row,col);
        if(isOpen(row,col)){
            return;
        }

        grid[row-1][col-1] = 1; // open
        num++; // open的节点数加一

        connectNeighbor(row, col, Index);
        return;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        checkBoundary(row, col);
        // check its neighbor
        if(grid[row-1][col-1] != 0){
            return true;
        }else{
            return false;
        }
    }


    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        // 与虚拟结点0 连通的结点都是full
        // 因为可以假想水(电)从0结点开始灌(导通)
        int Index = getIndex(row, col);
        return uf.find(Index)==uf.find(0);
    }


    // returns the number of open sites
    public int numberOfOpenSites(){return num;}

    // does the system percolate?
    public boolean percolates(){
        return uf.find(0) == uf.find( N*N +1);
    }

    // test client (optional)
    //public static void main(String[] args)
}
