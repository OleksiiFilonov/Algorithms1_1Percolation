import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private final int gridDimension;
	private final boolean openned[];
	private int numberOfOpenSites;

	private final int virtualTopIndex = 0;
	private final int virtualBottomIndex;

	private final WeightedQuickUnionUF unionFind;

	// create n-by-n grid, with all sites blocked
	public Percolation(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("The input grid size \"" + n + "\" must be >=0");
		gridDimension = n;
		int arraySize = gridDimension * gridDimension + 2;
		unionFind = new WeightedQuickUnionUF(arraySize);
		openned = new boolean[arraySize];
		numberOfOpenSites = 0;
		virtualBottomIndex = gridDimension * gridDimension + 1;
		for (int i = 0; i < arraySize; i++) {
			openned[i] = false;
		}
		// initialize the connection of Top and Bottom Virtual vertexes
		// connect the top and bottom sites in the grid.
		for (int i = convertTo1D(1, 1); i <= gridDimension; i++) {
			unionFind.union(virtualTopIndex, i);
		}
		int theRightBottomIndex = convertTo1D(gridDimension, gridDimension);
		for (int i = convertTo1D(gridDimension, 1); i <= theRightBottomIndex; i++) {
			unionFind.union(virtualBottomIndex, i);
		}
	}

	// open site (row, col) if it is not open already
	// row and col between 1<=x<=n
	public void open(int row, int col) {
		validateInput(row, col);
		if (!isOpen(row, col)) {
			numberOfOpenSites++;
			int opennedIndex = convertTo1D(row, col);
			openned[opennedIndex] = true;
			// connect to the top
			if (row > 1 && isOpen(row - 1, col)) {
				connectOpenedSites(row - 1, col, opennedIndex);
			}
			// connect to the right
			if (col < gridDimension && isOpen(row, col + 1)) {
				connectOpenedSites(row, col + 1, opennedIndex);
			}
			// connect to the bottom
			if (row < gridDimension && isOpen(row + 1, col)) {
				connectOpenedSites(row + 1, col, opennedIndex);
			}
			// connect to the left
			if (col > 1 && isOpen(row, col - 1)) {
				connectOpenedSites(row, col - 1, opennedIndex);
			}
			/*
			 * // if site at the top -> connect with virtual top if(row == 1) {
			 * unionFind.union(virtualTopIndex, opennedIndex); // if site on the
			 * last line -> connect to virtual bottom } else if (row ==
			 * gridDimension) { unionFind.union(virtualBottomIndex,
			 * opennedIndex); }
			 */
		}
	}

	private void connectOpenedSites(int row, int col, int opennedIndex) {
		int siteIndexToConnect = convertTo1D(row, col);
		unionFind.union(opennedIndex, siteIndexToConnect);
	}

	private void validateInput(int row, int col) {
		validateIndex(row);
		validateIndex(col);
	}

	// is site (row, col) open?
	public boolean isOpen(int row, int col) {
		validateInput(row, col);
		return openned[convertTo1D(row, col)];
	}

	private int convertTo1D(int row, int col) {
		return (row - 1) * gridDimension + col;
	}

	// is site (row, col) full?
	public boolean isFull(int row, int col) {
		validateInput(row, col);
		int siteIndex = convertTo1D(row, col);
		return unionFind.connected(virtualTopIndex, siteIndex) && openned[siteIndex];
	}

	// number of open sites
	public int numberOfOpenSites() {
		return numberOfOpenSites;
	}

	// does the system percolate?
	public boolean percolates() {
		return unionFind.connected(virtualTopIndex, virtualBottomIndex);
	}

	private void validateIndex(int index) {
		if (index <= 0 || index > gridDimension)
			throw new IndexOutOfBoundsException("row index " + index + " out of bounds");
	}
}
