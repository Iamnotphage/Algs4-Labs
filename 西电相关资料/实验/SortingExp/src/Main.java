import edu.princeton.cs.algs4.In;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // generate a random array.
        int[] arr = new int[100];
        int[] arrHundred = new int[100];

        In in = new In("100.txt");
        for(int i = 0; i < 100; i++){
            arrHundred[i] = in.readInt();
        }

        System.arraycopy(arrHundred,0, arr, 0, 100);
        // init done.


        Runtime r = Runtime.getRuntime();
        r.gc(); // garbage collect
        long startTime = System.nanoTime();
        long startMem = r.totalMemory();
        InsertionSort IS = new InsertionSort(arrHundred);
        long runMem = startMem - r.freeMemory();
        long runTime = System.nanoTime() - startTime;
        System.out.println("IS: " + runTime * 0.001 + " μs mem: " + runMem/1024 + " KB"); // 1 ns = 10^-3 μs (micro second)

        r.gc();

        System.arraycopy(arr,0, arrHundred, 0, 100);

        startTime = System.nanoTime();
        startMem = r.totalMemory();
        TopdownMergesort TDM = new TopdownMergesort(arrHundred);
        runMem = startMem - r.freeMemory();
        runTime = System.nanoTime() - startTime;
        System.out.println("TDM: " + runTime * 0.001 + " μs mem: " + runMem/1024 + " KB"); // 1 ns = 10^-3 μs (micro second)

        r.gc();

        System.arraycopy(arr,0, arrHundred, 0, 100);

        startTime = System.nanoTime();
        startMem = r.totalMemory();
        BottomupMergesort BUM = new BottomupMergesort(arrHundred);
        runMem = startMem - r.freeMemory();
        runTime = System.nanoTime() - startTime;
        System.out.println("BUM: " + runTime * 0.001 + " μs mem: " + runMem/1024 + " KB"); // 1 ns = 10^-3 μs (micro second)

        r.gc();

        System.arraycopy(arr,0, arrHundred, 0, 100);

        startTime = System.nanoTime();
        startMem = r.totalMemory();
        RandomQuickSort RQ = new RandomQuickSort(arrHundred);
        runMem = startMem - r.freeMemory();
        runTime = System.nanoTime() - startTime;
        System.out.println("RQ: " + runTime * 0.001 + " μs mem: " + runMem/1024 + " KB"); // 1 ns = 10^-3 μs (micro second)

        r.gc();

        System.arraycopy(arr,0, arrHundred, 0, 100);

        startTime = System.nanoTime();
        startMem = r.totalMemory();
        Dijkstra3WayQuickSort QD3P = new Dijkstra3WayQuickSort(arrHundred);
        runMem = startMem - r.freeMemory();
        runTime = System.nanoTime() - startTime;
        System.out.println("QD3P: " + runTime * 0.001 + " μs mem: " + runMem/1024 + " KB"); // 1 ns = 10^-3 μs (micro second)

    }
}