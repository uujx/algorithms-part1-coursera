import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] threshold;
    private final int times;

    public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
    {
        times = trials;
        threshold = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                p.open(row, col);
            }
            threshold[i] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    public double mean()                          // sample mean of percolation threshold
    {
        return StdStats.mean(threshold);
    }

    public double stddev()                        // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(threshold);
    }


    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return mean() - ((1.96 * stddev()) / Math.sqrt(times));
    }

    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return mean() + ((1.96 * stddev()) / Math.sqrt(times));
    }

    public static void main(String[] args)        // test client (described below)
    {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
    }

}
