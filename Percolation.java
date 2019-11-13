import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size;
    private int openedCount;

    private boolean[] grid;
    private final boolean FULL = false;
    private final boolean OPEN = true;

    public WeightedQuickUnionUF qf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Size n must be >= 1");
        size = n;
        grid = new boolean[size * size];
        qf = new WeightedQuickUnionUF(size * size);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        int cur = pos(row, col);

        if (grid[cur] == OPEN)
            return;

        grid[cur] = OPEN;
        openedCount++;

        int left = posLeft(cur);
        int right = posRight(cur);
        int top = posTop(cur);
        int bottom = posBottom(cur);

        if (col > 1 && isOpen(left))
            qf.union(cur, left);

        if (col < size && isOpen(right))
            qf.union(cur, right);

        if (row > 1 && isOpen(top))
            qf.union(cur, top);

        if (row < size && isOpen(bottom))
            qf.union(cur, bottom);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return isOpen(pos(row, col));
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return isFull(pos(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedCount;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int top = pos(1, 1); top <= pos(1, size); top++) {
            if (isFull(top))
                continue;
            for (int bottom = pos(size, 1); bottom <= pos(size, size); bottom++) {
                if (isOpen(bottom) && qf.connected(top, bottom))
                    return true;
            }
        }
        return false;
    }

    private int pos(int row, int col) {
        return (row - 1) * size + col - 1;
    }

    private int posLeft(int pos) {
        return pos - 1;
    }

    private int posTop(int pos) {
        return pos - size;
    }

    private int posRight(int pos) {
        return pos + 1;
    }

    private int posBottom(int pos) {
        return pos + size;
    }

    private void validate(int row, int col) {
        if (row < 1 || col < 1)
            throw new IllegalArgumentException("row and col must be >= 1");
    }

    private boolean isOpen(int pos) {
        return grid[pos] == OPEN;
    }

    private boolean isFull(int pos) {
        return grid[pos] == FULL;
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}