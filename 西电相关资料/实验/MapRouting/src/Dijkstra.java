import edu.princeton.cs.algs4.*;

public class Dijkstra {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;            // edgeTo[v] = last edge on shortest s->v path
    private IndexMultiwayMinPQ<Double> pq;    // priority queue of vertices

    public Dijkstra(EdgeWeightedGraph G, int s, int d, double[][] vLoc) {
        for (Edge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];

        validateVertex(s);

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        StdDraw.setPenColor(StdDraw.GRAY);

        // relax vertices in order of distance from s
        pq = new IndexMultiwayMinPQ<Double>(G.V(), 3); // optimal 3: use multiway min priority queue
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            // draw
            if(v == d){
                return; // optimal 1: search until the destination
            }
            for (Edge e : G.adj(v))
                relax(e, v, vLoc, d);
        }

    }

    // relax edge e and update pq if changed
    private void relax(Edge e, int v, double[][] vLoc, int d) {
        int w = e.other(v);
        double x0 = vLoc[v][0] / 10000;
        double y0 = vLoc[v][1] / 10000;
        double x1 = vLoc[w][0] / 10000;
        double y1 = vLoc[w][1] / 10000;
        double weight = distTo[v] + e.weight() + MapRouting.dist(w,d) - MapRouting.dist(v,d); // optimal 2: Euler's distance
        // double weight = distTo[v] + e.weight();
        if (e.weight() > 0 && distTo[w] > weight) {
            StdDraw.line(x0, y0, x1, y1);
            distTo[w] = weight;
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<Edge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<Edge> path = new Stack<Edge>();
        int x = v;
        for (Edge e = edgeTo[v]; e != null; e = edgeTo[x]) {
            path.push(e);
            x = e.other(x);
        }
        return path;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
}