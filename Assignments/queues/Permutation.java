import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        // input n strings, where n is not given
        String item;
        while (!StdIn.isEmpty()) {
            item = StdIn.readString();
            rq.enqueue(item);
        }

        // iterators have StdRandom.shuffle()
        for (String s : rq) {
            if (k == 0) {
                break;
            }
            StdOut.println(s);
            k--;
        }
    }
}
