import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private int[][] grid;
    private int count = 0;
    private final int nums;
    private final int top;
    private final int bottom;
    private final WeightedQuickUnionUF uf;

    // Create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        // 1. Validate indice
        validateIndices(n);

        // 2. Initialize the percolation grid
        nums = n;
        int sizeOfGrid = n * n;
        top = sizeOfGrid;
        bottom = sizeOfGrid + 1;
        grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = 0;
            }
        }

        // 3. Initialize the UnionFind object
        uf = new WeightedQuickUnionUF(sizeOfGrid + 2);
//        for (int i = 0; i < n; i++) {
//            uf.union(i, top);  // Make the frist row connected to the first element.
//        }
//        for (int i = sizeOfGrid - 1; i > sizeOfGrid - n - 1; i--) {
//            uf.union(i, bottom); // Make the last row connected to the last element.
//        }
    }


    // Open site (row, col) if it is not open already
    public void open(int row, int col) {
        // 1. Validate indices
        validateIndices(row, col);

        // 2. Open the site
        if (grid[row - 1][col - 1] != 1) {
            grid[row - 1][col - 1] = 1;
            count++;
        }

        // 3. Link the site with its neighbors
        link(row, col);
    }


    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIndices(row, col);

        return grid[row - 1][col - 1] == 1;
    }


    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIndices(row, col);

        // A site must be opened and connected to the top if it is Full
        if (!isOpen(row, col)) {
            return false;
        } else if (!uf.connected(top, (row - 1) * nums + (col - 1))) {
            return false;
        }

        return true;
    }


    // Number of open sites
    public int numberOfOpenSites() {
        return count;
    }


    // Does the system percolate?
    public boolean percolates() {
        return uf.connected(top, bottom);
    }


    // Validator for n
    private void validateIndices(int n) {
        if (n < 1) {
            throw new java.lang.IllegalArgumentException("The given n is invalid");
        }
    }


    // Validator for row and col
    private void validateIndices(int row, int col) {
//        System.out.printf("%d, %d \n", row, col);

        if (row < 1 || row > nums) {
            throw new java.lang.IllegalArgumentException("The given row is invalid");
        }
        if (col < 1 || col > nums) {
            throw new java.lang.IllegalArgumentException("The given col is invalid");
        }
    }


    // Link the site with its neighbors
    private void link(int row, int col) {
        // What if the row is 0, there is no up neighbor. Need to consider this condition.
        // Need to calculate the index of (rwo, col) in uf.
        int position = (row - 1) * nums + col - 1;

        // First row linked to the top
        if (row == 1) {
            uf.union(top, position);
        }

        // Last row linked to the bottom
        if (row == nums) {
            uf.union(bottom, position);
        }

        // up
        if (row != 1) {
            if (isOpen(row - 1, col)) {
                int up = (row - 2) * nums + col - 1;
                uf.union(up, position);
            }
        }

        // down
        if (row != nums) {
            if (isOpen(row + 1, col)) {
                int down = row * nums + col - 1;
                uf.union(down, position);
            }
        }

        // left
        if (col != 1) {
            if (isOpen(row, col - 1)) {
                int left = (row - 1) * nums + col - 2;
                uf.union(left, position);
            }
        }

        // right
        if (col != nums) {
            if (isOpen(row, col + 1)) {
                int right = (row - 1) * nums + col;
                uf.union(right, position);
            }
        }
    }


    // Test client (optional)
    public static void main(String[] args) {
        int row, col;
        Percolation p = new Percolation(200);

        while (!p.percolates()) {
            row = StdRandom.uniform(1, 200);
            col = StdRandom.uniform(1, 200);
            p.open(row, col);
            System.out.printf("Opened one site at (%d, %d) \n", row, col);
        }

//        System.out.println(p.count);
    }
}


