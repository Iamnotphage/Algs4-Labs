import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * shortest ancestral path:
 * All methods (and the constructor) should take
 * time at most proportional to E + V in the worst case
 * */
public class SAP {
    private final Digraph sapDiGraph;

    private final int V;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        // take time at most proportional to E + V in the worst case
        if(G == null){
            throw new IllegalArgumentException();
        }
        this.sapDiGraph = new Digraph(G);

        V = G.V();

//        TopologicalSort tls = new TopologicalSort(sapDiGraph);
//
//        int start = tls.reversePost.peek(); // start Node has no indegree

    }

    private boolean checkBoundary(Integer x){
        if(x == null){
            return false; // Any iterable argument contains a null item
        }
        return x >= 0 && x < this.sapDiGraph.V();// Any vertex argument is outside its prescribed range
    }


    /**Shortest ancestral path
     * @param vSet,wSet : from v to w (Iterable)
     * */
    private int[] sap(Iterable<Integer> vSet, Iterable<Integer> wSet){
        int[] ans = new int[2]; // ans[0]: length; ans[1]: ancestor

        // default: no such path: -1
        Arrays.fill(ans, -1);

        // distance
        HashMap<Integer, Integer> distanceV = new HashMap<>();
        HashMap<Integer, Integer> distanceW = new HashMap<>();

        //
        Queue<Integer> q = new Queue<>();
        boolean[] marked = new boolean[V];

        Arrays.fill(marked, false);

        for(int v : vSet){
            q.enqueue(v); // 从vSet里选取所有点
            marked[v] = true;
            distanceV.put(v, 0);
        }

        // bfs
        while(!q.isEmpty()){
            int x = q.dequeue();
            Iterable<Integer> bag = sapDiGraph.adj(x);

            // 加入x的邻接点
            for(int v : bag){
                if(!marked[v]){
                    q.enqueue(v);
                    marked[v] = true;
                    // 更新距离
                    int d = distanceV.get(x);
                    // v是否在map里，如果在返回其距离，否则就默认加1
                    int dd = distanceV.getOrDefault(v, d + 1);

                    distanceV.put(v, dd);
                }
            }
        }

        Arrays.fill(marked, false);

        for(int w : wSet){
            q.enqueue(w); // 从vSet里选取所有点
            marked[w] = true;
            distanceW.put(w, 0);
        }

        // bfs
        while(!q.isEmpty()){
            int x = q.dequeue();

            // 检查 x是否是 公共祖先
            if(distanceV.containsKey(x)){
                // 更新minDistance
                int minDistance = distanceV.get(x) + distanceW.get(x);

                if(ans[0] == -1 || minDistance < ans[0]){
                    ans[0] = minDistance;
                    ans[1] = x;
                }
            }

            Iterable<Integer> bag = sapDiGraph.adj(x);

            // 加入x的邻接点
            for(int v : bag){
                if(!marked[v]){
                    q.enqueue(v);
                    marked[v] = true;
                    // 更新距离
                    int d = distanceW.get(x);
                    // v是否在map里，如果在返回其距离，否则就默认加1
                    int dd = distanceW.getOrDefault(v, d + 1);

                    distanceW.put(v, dd);
                }
            }
        }
        return ans;
    }



    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        if(!checkBoundary(v) || !checkBoundary(w)){
            throw new IllegalArgumentException();
        }
        ArrayList<Integer> vSet = new ArrayList<>();
        vSet.add(v);

        ArrayList<Integer> wSet = new ArrayList<>();
        wSet.add(w);

        return sap(vSet, wSet)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        if(!checkBoundary(v) || !checkBoundary(w)){
            throw new IllegalArgumentException();
        }
        ArrayList<Integer> vSet = new ArrayList<>();
        vSet.add(v);

        ArrayList<Integer> wSet = new ArrayList<>();
        wSet.add(w);

        return sap(vSet, wSet)[1];

    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if(v == null || w == null){
            throw new IllegalArgumentException();
        }

        for(Integer x : v){
            if(!checkBoundary(x)){ // Any iterable argument contains a null item
                throw new IllegalArgumentException();
            }
        }

        for(int x : w){
            if(!checkBoundary(x)){ // Any iterable argument contains a null item
                throw new IllegalArgumentException();
            }
        }

        // length
        return sap(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        if(v == null || w == null){
            throw new IllegalArgumentException();
        }

        for(Integer x : v){
            if(!checkBoundary(x)){ // Any iterable argument contains a null item
                throw new IllegalArgumentException();
            }
        }

        for(int x : w){
            if(!checkBoundary(x)){ // Any iterable argument contains a null item
                throw new IllegalArgumentException();
            }
        }

        return sap(v, w)[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}