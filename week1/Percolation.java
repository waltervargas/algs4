/******************************************************************************
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution:    java-algs4 PercolationVisualizer input.txt
 *  Dependencies: algs4.jar
 *
 *  This programs create the percolation API
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int side;
    private int sites; // the number of sites
    private int[] states; // 0 closed 1 open
    private WeightedQuickUnionUF grid;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        side = n;
        sites = side*side;
        states = new int[sites+2];

        // all sites blocked at the beginning
        for (int i = 0; i < sites; i++) {
            states[i] = 0;
        }

        // creates the grid using the structure WeightedQuickUnionUF
        grid = new WeightedQuickUnionUF(sites+2);
        // virtual top
        states[sites] = 1;
        // virtual bottom
        states[sites+1] = 1;
    }

    // validates if the row and col are out of range. [NxN]
    private void errorOnRange(int row, int col) {
        if (row <= 0 || col <= 0 || row > side || col > side) {
            throw new IndexOutOfBoundsException("Row or Col out of Range");
        }
    }

    private void union(int p, int q) {
        if (!grid.connected(p, q)) {
            grid.union(p, q);
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        errorOnRange(row, col);
        int top = side*side;
        int bottom = side*side+1;
        int cell = getIndex(row, col);

        int below = getIndex(row+1, col);
        int right = getIndex(row, col+1);

        if (isOpen(row, col)) {
            return;
        }

        states[cell] = 1;

        // union with above cell if row if different of 1
        // union with virtual top if row is 1
        if (row != 1 && isOpen(row-1, col)) {
            int above = getIndex(row-1, col);
            union(above, cell);
        }
        else if (row == 1) {
            union(cell, top);
        }

        // union with below cell if row is different of side
        // union with virtual bottom if row is equal to side
        if (row != side && isOpen(row+1, col)) {
            union(below, cell);
        }
        else if (row == side) {
            union(cell, bottom);
        }

        // left
        if (col != 1 && isOpen(row, col-1)) {
            int left = getIndex(row, col-1);
            union(left, cell);
        }

        // right
        if (col != side && isOpen(row, col+1)) {
            union(right, cell);
        }
    }

    public boolean isOpen(int row, int col) {
        errorOnRange(row, col);
        return states[getIndex(row, col)] == 1;
    }

    public boolean isFull(int row, int col) {
        errorOnRange(row, col);
        int cell = getIndex(row, col);
        int top = side*side;

        return grid.connected(top, cell);
    }

    // does the system percolate?
    public boolean percolates() {
        int top = side*side;
        int bottom = side*side+1;

        return grid.connected(top, bottom);
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < sites; i++) {
            if (states[i] == 1) {
                count = count + 1;
            }
        }
        return count;
    }

    // returns the index for the cell
    private int getIndex(int row, int col) {
        return (side*(row-1))+(col-1);
    }
}
