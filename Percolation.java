import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
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

        if (grid[cur])
            return;

        grid[cur] = true;
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
        int cur = pos(row, col);
        return isOpen(cur) && qf.connected(cur, virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedCount;
    }

    // does the system percolate?
    public boolean percolates() {
        if (size == 1)
            return isOpen(1, 1);
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
        if (row < 1 || col < 1 || row > size || col > size)
            throw new IllegalArgumentException(String.format("row and col must be between 1 and %d", size));
    }

    private boolean isOpen(int pos) {
        return grid[pos];
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(1);
        StdOut.println(p.numberOfOpenSites());
        StdOut.println(p.isOpen(1, 1));
        StdOut.println(p.isFull(1, 1));
        StdOut.println(p.percolates());
    }
}