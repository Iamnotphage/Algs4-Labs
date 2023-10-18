import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

public class Solver {

    private boolean solvable;

    private Stack<Board> solution; // record the solution

    private Node finalNode; // final Node


    // 下面两行已经弃用，因为最终存储在Solver类中，会导致Solver类的大小超出要求
    // 转为Solver类构造函数中的临时变量
    //    private MinPQ<Node> boardPQ; // define the node to be a board
    //
    //    private MinPQ<Node> boardPQTwin; // twin of the boardPQ


    // Board类本身不能比较，需要构造结点使之能够比较
    // 比较distance + moves = priority的大小
    private class Node implements Comparable<Node>{
        private Board nodeBoard;
        private int move;

        private Node pre;

        private int priority;

        public Node(Board initial){
            this.move = 0;
            this.nodeBoard = initial;
            this.pre = null;
            this.priority = this.move + this.nodeBoard.manhattan();
        }

        // 添加邻居结点，需要筛选掉重复的
        public Node(Board neighbor, Node preSearchNode){
            this.move = preSearchNode.move + 1;
            this.nodeBoard = neighbor;
            this.pre = preSearchNode;
            this.priority = this.move + this.nodeBoard.manhattan();
        }

        // 重载这个函数才能够加入到优先队列里面比较
        @Override
        public int compareTo(Node that) {
            if(this.priority < that.priority){
                return -1;
            }else if(this.priority > that.priority){
                return 1;
            }
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        if(initial == null){
            throw new IllegalArgumentException();
        }

        // 放在这，最终Solver类的占用内存不会超出要求
        MinPQ<Node> boardPQ; // define the node to be a board
        MinPQ<Node> boardPQTwin; // twin of the boardPQ

        solvable = false;

        boardPQ = new MinPQ<>();
        boardPQTwin = new MinPQ<>(); // twin

        Node minNode = new Node(initial);
        Node minNodeTwin = new Node(initial.twin());
        boardPQ.insert(minNode);
        boardPQTwin.insert(minNodeTwin);

        while(true){

            minNode = boardPQ.delMin();
            minNodeTwin = boardPQTwin.delMin();


            for(Board neighbor : minNode.nodeBoard.neighbors()){
                if(minNode.pre == null || !neighbor.equals(minNode.pre.nodeBoard)){
                    boardPQ.insert(new Node(neighbor, minNode));
                }
            }

            for(Board neighbor : minNodeTwin.nodeBoard.neighbors()){
                if(minNodeTwin.pre == null || !neighbor.equals(minNodeTwin.pre.nodeBoard)){
                    boardPQTwin.insert(new Node(neighbor, minNodeTwin));
                }
            }


            if(minNode.nodeBoard.isGoal()){
                solvable = true;
                break;
            }

            if(minNodeTwin.nodeBoard.isGoal()){
                solvable = false;
                break;
            }
        }

        finalNode = minNode;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        if(!isSolvable()){
            return -1;
        }
        return finalNode.move;
    }


    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        if(!isSolvable()){
            return null;
        }

        Stack<Board> InverseSolution = new Stack<>();
        solution = new Stack<>();

        Node node = finalNode;
        while(node != null){
            InverseSolution.push(node.nodeBoard);
            node = node.pre;
        }

        while(!InverseSolution.isEmpty()){
            solution.push(InverseSolution.pop());
        }
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
