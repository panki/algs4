import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) {
            StdOut.println("Usage: java Permutation n < stream");
            StdOut.println("Where:");
            StdOut.println("  n      - number of items to print, should be >= 1");
            StdOut.println("  stream - input items");
            return;
        }

        int n = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            q.enqueue(StdIn.readString());
        }

        for (int i = 1; i <= n; i++) {
            StdOut.println(q.dequeue());
        }
    }
}