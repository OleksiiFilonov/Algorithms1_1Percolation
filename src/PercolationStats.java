import static edu.princeton.cs.algs4.StdRandom.uniform;

import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

	private final int numberOfTrials;
	private final Percolation[] percolations;
	private final double[] fractionOfOpenSites;

	// perform trials independent experiments on an n-by-n grid
	public PercolationStats(final int gridSize, final int trials) {
		if (gridSize <= 0 || trials <= 0)
			throw new IllegalArgumentException("Either grid size: \"" + gridSize + "\" or number of trials \"" + trials
					+ "\" must be greater than zero");
		numberOfTrials = trials;
		percolations = new Percolation[numberOfTrials];
		fractionOfOpenSites = new double[numberOfTrials];
		for (int i = 0; i < numberOfTrials; i++) {
			Percolation percolation = new Percolation(gridSize);
			while (!percolation.percolates()) {
				percolation.open(1 + uniform(gridSize), 1 + uniform(gridSize));
			}
			percolations[i] = percolation;
			fractionOfOpenSites[i] = (double) percolation.numberOfOpenSites() / (gridSize * gridSize);
		}
	}

	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(fractionOfOpenSites);
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		if (numberOfTrials == 1) {
			return Double.NaN;
		} else {
			return StdStats.stddev(fractionOfOpenSites);
		}
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return mean() - 1.96 * stddev() / Math.sqrt(numberOfTrials);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mean() + 1.96 * stddev() / Math.sqrt(numberOfTrials);
	}

	// test client (described below)
	public static void main(String[] args) {
		if (args.length < 2) {
			throw new IllegalStateException("Grid size or number of trials are missed ");
		}
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		PercolationStats statistics = new PercolationStats(n, trials);
		System.out.println(String.format(
				"Size grid: %s, number of trials: %s \nmean: %s, standard dev: %s, confidentLow: %s, confidentHigh: %s",
				n, trials, statistics.mean(), statistics.stddev(), statistics.confidenceLo(),
				statistics.confidenceHi()));
	}
}
