import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final boolean full = false;
    private static final boolean open = true;

    private final int size;
    private int openedCount;
    private boolean[] grid;
    private final WeightedQuickUnionUF qf;
    private int virtualTop;
    private int virtualBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Size n must be >= 1");
        size = n;
        int len = size * size;

        grid = new boolean[len];
        qf = new WeightedQuickUnionUF(len + 2); // 2 extra for virtual top & bottom nodes

        virtualTop = len;
        virtualBottom = len + 1;

        // Connect top row to virtual top
        // and bottom row to virtual bottom
        for (int i = 0; i < size; i++) {
            qf.union(i, virtualTop);
            qf.union(i + len - size, virtualBottom);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        int cur = pos(row, col);

        if (grid[cur] == open)
            return;

        grid[cur] = open;
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
        return qf.connected(virtualTop, virtualBottom);
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
        return grid[pos] == open;
    }

    private boolean isFull(int pos) {
        return grid[pos] == full;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        p.open(1, 1);
        StdOut.println(p.percolates());
        p.open(2, 1);
        StdOut.println(p.percolates());
    }
}