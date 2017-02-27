/******************************************************************************
 *  Compilation:  javac-algs4 PercolationStats.java
 *  Execution:    java-algs4 PercolationStats 200 100
 *  Dependencies: Percolation.java
 *
 *  This programs produce stats about percolation on tries nxn
 *
 ******************************************************************************/


import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private double[] attempts;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) { 
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        attempts = new double [trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            int steps = 0;
            while (!perc.percolates()) {
                int row = StdRandom.uniform(n)+1;
                int col = StdRandom.uniform(n)+1;
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                    steps++;
                }
            }
            attempts[i] = (double) steps/(n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(attempts);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(attempts);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean()-((1.96*stddev())/Math.sqrt(attempts.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean()+((1.96*stddev())/Math.sqrt(attempts.length));
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats PS = new PercolationStats(n, trials);
        StdOut.println("mean = " + PS.mean());
        StdOut.println("std dev = " + PS.stddev());
        StdOut.println("95% confidence interval = [" + PS.confidenceLo() + ", " + PS.confidenceHi() + "]");
    }
}
