import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // some stats
    private double xBar;
    private int T;
    private int N;
    private double S;
    private double[] X; // T次实验中，每次实验的open site的数量与总格点数之比
    private double confidenceLow;
    private double confidenceHigh;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if(n<=0 || trials <=0){
            throw new IllegalArgumentException(" n or trials argument error!");
        }
        N = n;
        T = trials;
        xBar = 0.0;
        S = 0.0;
        confidenceHigh = 0.0;
        confidenceLow = 0.0;

        X = new double[trials];

        for(int i=0;i<trials;i++){
            Percolation pl = new Percolation(n);
            while(!pl.percolates()){
                int randomRow = StdRandom.uniformInt(1, n+1); // [a,b)
                int randomCol = StdRandom.uniformInt(1, n+1);
                pl.open(randomRow, randomCol);
            }
            X[i] = (double)pl.numberOfOpenSites()/(n*n);
        }

        xBar = StdStats.mean(X);
        S = StdStats.stddev(X);
        confidenceLow = (xBar - 1.96*S/Math.sqrt(T));
        confidenceHigh = (xBar + 1.96*S/Math.sqrt(T));
    }

    // sample mean of percolation threshold
    public double mean(){
        return xBar;
    }


    // sample standard deviation of percolation threshold
    public double stddev(){
        return S;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return confidenceLow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return confidenceHigh;
    }

    // test client (see below)
    public static void main(String[] args){
        Integer n = Integer.valueOf(args[0]);
        Integer trials = Integer.valueOf(args[1]);

        PercolationStats plstats = new PercolationStats(n,trials);

        System.out.println("mean                    = " + plstats.mean());
        System.out.println("stddev                  = " + plstats.stddev());
        System.out.println("95% confidence interval = [" + plstats.confidenceLo() + "," + plstats.confidenceHi() + "]");

        return;
        // start:  2023-9-23 10:30
        // end:    2023-9-23 15:50
        // modify: 2023-10-11 19:33
    }

}
