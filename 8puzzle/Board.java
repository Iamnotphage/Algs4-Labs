
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private int n; // board n-by-n
    private int[][] board; // board 0~n^2-1

    // 预先存储 空格 在棋盘上的位置
    private int blankX;

    private int blankY;

    private final int hammingDistance; // Caching the Hamming and Manhattan priorities

    private final int manhattanDistance;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // 0 represents the blank square
        if (tiles.length < 2 || tiles.length >= 128) {
            throw new IllegalArgumentException();
        }

        n = tiles.length;
        // moves = 0;
        // preSearchNode = null;


        // copy tiles to board and store the BLANK position
        board = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                }
                board[i][j] = tiles[i][j];
            }
        }

        // Caching the Hamming and Manhattan priorities

        // 计算海明距离
        // 遍历给定棋盘格，与目标相异则距离加一
        int distance = 0;
        int goalRef = 1; // 参考值

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) {
                    // 最右下角的格子不计算
                    continue;
                }
                if (board[i][j] != goalRef++) {
                    distance++;
                }
            }
        }

        hammingDistance = distance;

        // 计算曼哈顿距离
        // 遍历给定棋盘格，用目标的坐标减去待计算的
        // 比如8在(0,0) 但目标是(2,1) 所以距离是3

        distance = 0;
        // 计算目标棋盘格的坐标
        // 第x行，第y列目标的值应该是 nx+y+1 (x,y ∈ [0,n-1])
        // 设给定值为V, 则 V = nx+y+1
        // (V-1)/n = x + y/n 由于 y小于n，在整数除法直接截断
        // 所以 x = (V-1)/n, y = V - nx - 1

//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                if (board[i][j] == 0) {
//                    continue;
//                }
//                if (board[i][j] != n * i + j + 1) {
//                    // 给定值不等于目标值
//                    int x = (board[i][j] - 1) / n;
//                    int y = board[i][j] - n * x - 1;
//                    int dx = Math.abs(x - i);
//                    int dy = Math.abs(y - j);
//                    distance = distance + dx + dy;
//                }
//            }
//        }

        int rightRow, rightCol, tileDistance;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++) {
                if ( i == blankX && j == blankY)
                    continue;
                rightRow = (board[i][j] - 1) / n;
                rightCol = (board[i][j] - 1) % n;
                tileDistance = Math.abs(rightRow - i) + Math.abs(rightCol - j);
                distance += tileDistance;
            }
        }

        manhattanDistance = distance;

    }

    // string representation of this board
    public String toString(){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(n + "\n");
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++)
                strBuilder.append("  " + board[row][col]);
            strBuilder.append("\n");
        }
        String string = strBuilder.toString();
        return string;
    }

    // board dimension n
    public int dimension(){
        return n;
    }

    // number of tiles out of place
    public int hamming(){
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal(){
        int goalRef = 1;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                // 最后一个格子重要判断
                if(i == n-1 && j == n-1 && board[i][j] == 0){
                    continue;
                }
                if(board[i][j] != goalRef++){
                    return false;
                }
            }
        }
        return true;
    }


    // does this board equal y?
    public boolean equals(Object y){
        if(y == this){
            return true;
        }
        if(y == null){
            return false;
        }

        if (this.getClass() != y.getClass())
            return false;

        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(this.board[i][j] != that.board[i][j] ){
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    // 这里的neighbors是冗余的，可能出现前两步出现过的棋盘，在Solver类中解决
    public Iterable<Board> neighbors(){
        ArrayList<Board> neighborBoards = new ArrayList<>();

        // 空格和上交换
        Board neighbor = exchange(blankX-1, blankY);
        if(neighbor!=null){
            neighborBoards.add(neighbor);
        }

        // 空格和右交换
        neighbor = exchange(blankX, blankY + 1);
        if(neighbor!=null){
            neighborBoards.add(neighbor);
        }

        // 空格和下交换
        neighbor = exchange(blankX + 1, blankY);
        if(neighbor!=null){
            neighborBoards.add(neighbor);
        }

        // 空格和左交换
        neighbor = exchange(blankX, blankY - 1);
        if(neighbor!=null){
            neighborBoards.add(neighbor);
        }

        return neighborBoards;
    }

    // help me to exchange the blank with (row,col) tiles in this Board
    private Board exchange(int row, int col){
        // row, col ∈ [0,n-1]
        if(row < 0 || row > n-1 || col < 0 || col > n-1){
            return null;
        }

        // 改变this.board[row][col]
        // 然后传给临时变量tmp board再把this.board[row][col]变回来
        // 这样就实现原地改变，而不需要再浪费N^2的时间来复制this.board

        // swap the blank and the (row, col)
        int t = this.board[row][col];
        this.board[row][col] = 0; // this.board[row][col] = BLANK;
        this.board[blankX][blankY] = t;

        Board tmpboard = new Board(this.board); // 构造器本身 O(N^2)

        this.board[blankX][blankY] = 0;// this.board[blankX][blankY] = BLANK;
        this.board[row][col] = t;

        return tmpboard;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        //  (the blank square is not a tile).
        Board twin = null;
        int row = 0, col = 0, exRow = 1, exCol = 1;
        if (row == blankX && col == blankY){

            int t = this.board[row][col+1];
            this.board[row][col+1] = this.board[exRow][exCol];
            this.board[exRow][exCol] = t;

            twin = new Board(this.board); // 构造器本身 O(N^2)

            this.board[exRow][exCol] = this.board[row][col+1];
            this.board[row][col+1] = t;

            return twin;
        }

        if (exRow == blankX && exCol == blankY){
            int t = this.board[row][col];
            this.board[row][col] = this.board[exRow-1][exCol];
            this.board[exRow-1][exCol] = t;

            twin = new Board(this.board); // 构造器本身 O(N^2)

            this.board[exRow-1][exCol] = this.board[row][col];
            this.board[row][col] = t;

            return twin;
        }

        int t = this.board[row][col];
        this.board[row][col] = this.board[exRow][exCol];
        this.board[exRow][exCol] = t;

        twin = new Board(this.board); // 构造器本身 O(N^2)

        this.board[exRow][exCol] = this.board[row][col];
        this.board[row][col] = t;

        return twin;

    }

    // unit testing (not graded)
    public static void main(String[] args){
        // create initial board from file
        int[][] tiles = new int[3][3];
        tiles[0][0] = 1; tiles[0][1] = 2; tiles[0][2] = 3;
        tiles[1][0] = 4; tiles[1][1] = 5; tiles[1][2] = 6;
        tiles[2][0] = 7; tiles[2][1] = 0; tiles[2][2] = 8;
        Board board = new Board(tiles);
        System.out.println(board.toString());
        System.out.println("\n");

        for (Board bd: board.neighbors()) {
            System.out.println(bd);
        }
        System.out.println(board.isGoal());


        int[][] tiles2 = new int[3][3];
        tiles2[0][0] = 1; tiles2[0][1] = 2; tiles2[0][2] = 3;
        tiles2[1][0] = 4; tiles2[1][1] = 5; tiles2[1][2] = 6;
        tiles2[2][0] = 7; tiles2[2][1] = 0; tiles2[2][2] = 1;

        Board board2 = new Board(tiles2);

        StdOut.println(board2.equals(board));

    }
}
