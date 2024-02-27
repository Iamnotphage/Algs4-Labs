import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;                    // N-by-N grid;
    private int num ;                  // number of the open sites
    private boolean [][] grid;
    private WeightedQuickUnionUF uf;  // uf for testing percolates
    private WeightedQuickUnionUF ufFull; // uf for testing isFull (只含top的虚拟结点不含bottom的虚拟结点)
    // backwash现象主要是误判isFull，我的实现里面，判断是否percolate只测试top和bottom虚拟节点是否连通
    // 这就导致有一些结点虽然是open但不是full，isFull()如果是看待测点是否和top连接，就会出现backwash
    // 原因就是因为bottom结点连接了下面几行的不是full的结点，所以特地再添加一个WeightedQuickUnionUF对象，不包含bottom虚拟节点

    // creates n-by-n grid, with all sites initially blocked
    // total n*n+2 sites
    public Percolation(int n){
        if(n<=0){
            throw new IllegalArgumentException(" Cow or Col argument error!");
        }
        N = n;
        num = 0;
        grid = new boolean[n][n]; // false:blocked; true:opened;
        uf = new WeightedQuickUnionUF(n*n+2);
        // 0 is the top virtual site;
        // 1-n*n are the gird sites;
        // n*n+1 is the bottom virtual site
        ufFull = new WeightedQuickUnionUF(n*n + 1); // 1 virtual top point and n*n real points

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                grid[i][j] = false;
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
            ufFull.union(0, Index);
        }else{
            if(grid[row-2][col-1]!=false){
                uf.union(Index, Index - N);
                ufFull.union(Index, Index - N);
            }
        }

        // 向右连接
        if(col + 1 <= N){// 界内
            if(grid[row-1][col]!=false){
                uf.union(Index, Index+1);
                ufFull.union(Index, Index +1);
            }
        }

        // 向下连接
        if(row + 1 > N){ // 末行
            uf.union(N*N+1, Index);
            // ufFull这里不union，因为没有底部虚拟结点
        }else{
            if(grid[row][col-1]!=false){
                uf.union(Index, Index + N);
                ufFull.union(Index, Index + N);
            }
        }

        // 向左连接
        if(col - 1 >= 1){ // 界内
            if(grid[row-1][col-2]!=false){
                uf.union(Index, Index-1);
                ufFull.union(Index, Index - 1);
            }
        }

    }
    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        int Index = getIndex(row,col);
        if(isOpen(row,col)){
            return;
        }

        grid[row-1][col-1] = true; // open
        num++; // open的节点数加一

        connectNeighbor(row, col, Index);
        return;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        checkBoundary(row, col);
        // check its neighbor
        if(grid[row-1][col-1] != false){
            return true;
        }else{
            return false;
        }
    }


    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        // 与虚拟结点0 连通的结点都是full 如果是两个虚拟结点会导致错误!
        // 对于用一个顶部虚拟结点来判断，下面注释的假想是没问题的
        // 因为可以假想水(电)从0结点开始灌(导通)
        int Index = getIndex(row, col);
        return ufFull.find(Index) == ufFull.find(0);
    }


    // returns the number of open sites
    public int numberOfOpenSites(){return num;}

    // does the system percolate?
    public boolean percolates(){
        // 判断percolates必须要用两个虚拟节点的uf
        return uf.find(0) == uf.find( N*N +1);
    }

    // test client (optional)
    //public static void main(String[] args)
}
