import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final double[] meanArr;
    private final int numTrials;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Enter values greater than 0.");
        }

        int row;
        int col;
        numTrials = trials;
        meanArr = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                row = StdRandom.uniform(1, n+1);
                col = StdRandom.uniform(1, n+1);
                perc.open(row, col);
            }
            double theEnn = (n*n);
            meanArr[i] = (perc.numberOfOpenSites() / theEnn);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(meanArr);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (numTrials == 1) {
            return Double.NaN;
        }
        return StdStats.stddev(meanArr);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(numTrials)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(numTrials)));
    }
    // test client (see below)
    public static void main(String[] args) {
        int[] arrArgs = StdIn.readAllInts();
        PercolationStats percS = new PercolationStats(arrArgs[0], arrArgs[1]);

        StdOut.println("mean = " + percS.mean());
        StdOut.println("stddev = " + percS.stddev());
        StdOut.println("95% confidence interval = [" + percS.confidenceLo() + ", " + percS.confidenceHi() + "]");
    }
}
