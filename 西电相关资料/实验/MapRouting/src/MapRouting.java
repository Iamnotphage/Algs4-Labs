import edu.princeton.cs.algs4.*;

import java.awt.*;

public class MapRouting {
    private static EdgeWeightedGraph graph;
    private static double[][] vLoc;

    public static void getGraph(In in) {
        int V = in.readInt();
        int E = in.readInt();
        graph = new EdgeWeightedGraph(V);
        vLoc = new double[V][2];
        int v;
        for (int i = 0; i < V; i++) {
            v = in.readInt();
            vLoc[v][0] = in.readDouble();  // x
            vLoc[v][1] = in.readDouble();  // y
        }
        int v1, v2;
        for (int i = 0; i < E; i++) {
            v1 = in.readInt();
            v2 = in.readInt();
            graph.addEdge(new Edge(v1, v2, dist(v1, v2)));
        }
    }

    public static double dist(int v1, int v2) {
        double x1 = vLoc[v1][0], y1 = vLoc[v1][1];
        double x2 = vLoc[v2][0], y2 = vLoc[v2][1];
        return Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
    }

    public static void draw(int src, int dest) {
        double srcX = vLoc[src][0] / 10000;
        double srcY = vLoc[src][1] / 10000;
        double destX = vLoc[dest][0] / 10000;
        double destY = vLoc[dest][1] / 10000;
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(srcX, srcY, 0.005);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(destX, destY, 0.005);
    }

    public static void main(String[] args) {
        In in = new In("./usa.txt");
        StdOut.println("Source: ");
        int src = StdIn.readInt();
        StdOut.println("Destination: ");
        int dest = StdIn.readInt();

        getGraph(in);
        draw(src, dest);

        long startTime = System.currentTimeMillis();
        Dijkstra sp = new Dijkstra(graph, src, dest, vLoc);
        long endTime = System.currentTimeMillis();
        StdOut.println("running time :" + (double)(endTime - startTime) / 1000 + "s");

        if (sp.hasPathTo(dest)) {
            StdOut.printf("%d to %d\n",src, dest);
            double total = 0;
            StdDraw.setPenColor(Color.GREEN);
            for (Edge e : sp.pathTo(dest)) {
                total += e.weight();
                int v = e.either();
                int w = e.other(v);
                double x0 = vLoc[v][0] / 10000;
                double y0 = vLoc[v][1] / 10000;
                double x1 = vLoc[w][0] / 10000;
                double y1 = vLoc[w][1] / 10000;
                StdDraw.line(x0 , y0, x1, y1);
            }
            StdOut.println("distance: " + total);
        } else {
            StdOut.println("there's no path from " + src + " to " + dest);
        }
    }
}