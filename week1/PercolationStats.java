import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    private static final double CONFIDENCE_95_KOEFF = 1.96;
    private final double stddev;
    private final double mean;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1)
            throw new IllegalArgumentException("Size n and number of trials both must be >= 1");

        double[] results = new double[trials];
        for (int pass = 0; pass < trials; pass++) {
            Percolation p = new Percolation(n);
            do {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                p.open(row, col);
            } while (!p.percolates());
            results[pass] = (double) p.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        double confidenceDelta = CONFIDENCE_95_KOEFF * stddev / Math.sqrt(trials);
        confidenceLo = mean - confidenceDelta;
        confidenceHi = mean + confidenceDelta;

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            StdOut.println("Usage: java PercolationStats n trials");
            StdOut.println("Where:");
            StdOut.println("  n      - grid size, should be >= 1");
            StdOut.println("  trials - number of experiments, should be >= 1");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.printf("%-23s = %f\n", "mean", stats.mean());
        StdOut.printf("%-23s = %f\n", "stddev", stats.stddev());
        StdOut.printf("%-23s = [%f, %f]\n", "95% confidence interval", stats.confidenceLo(), stats.confidenceHi());
    }

}