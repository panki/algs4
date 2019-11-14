import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * This class solves the "real world" Percolation problem using weighted quick
 * union-find algorithm
 *
 * @author Alexander Panko
 */
public class Percolation {
    /**
     * {@code int} variable representing the grid's side length
     */
    private final int size;

    /**
     * {@code int} variable representing the grid's opened sites count
     */
    private int openedCount;

    /**
     * {@code boolean} array representing the grid: {@code true} - site is already
     * opened {@code false} - site is closed yet
     */
    private boolean[] opened;

    /**
     * Variable representing {@code WeightedQuickUnionUF} class which implements
     * weighted quick union-find algorithm, uses 2 virtual sites top and bottom to
     * calculate perculation
     */
    private final WeightedQuickUnionUF qf;

    /**
     * Variable representing {@code WeightedQuickUnionUF} class which implements
     * weighted quick union-find algorithm, uses only 1 top virtual site to
     * calculate fullness
     */
    private final WeightedQuickUnionUF qfFull;

    /**
     * {@code int} variable representing the reserved virtual top site index. It is
     * used for simplified union with top/bottom row of the grid
     */
    private final int virtualTop, virtualBottom;
    private final int virtualTopFull;

    /**
     * Takes {@code int} variable and creates N*N grid with all sites initially
     * blocked
     *
     * @param n - grid's side length
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Size n must be >= 1");
        size = n;
        int len = size * size;

        opened = new boolean[len];
        qf = new WeightedQuickUnionUF(len + 2); // 2 extra for virtual top & bottom
        qfFull = new WeightedQuickUnionUF(len + 1); // 1 extra for virtual top

        virtualTop = len;
        virtualTopFull = len;
        virtualBottom = len + 1;
    }

    /**
     * Opens the site if it is not open already
     *
     * @param row - row index
     * @param col - col index
     */
    public void open(int row, int col) {
        validate(row, col);

        int cur = pos(row, col);

        if (opened[cur])
            return;

        opened[cur] = true;
        openedCount++;

        int left = posLeft(cur);
        int right = posRight(cur);
        int top = posTop(cur);
        int bottom = posBottom(cur);

        if (row == 1) {
            qf.union(cur, virtualTop);
            qfFull.union(cur, virtualTopFull);
        }

        if (row == size)
            qf.union(cur, virtualBottom);

        if (col > 1 && isOpen(left)) {
            qf.union(cur, left);
            qfFull.union(cur, left);
        }

        if (col < size && isOpen(right)) {
            qf.union(cur, right);
            qfFull.union(cur, right);
        }

        if (row > 1 && isOpen(top)) {
            qf.union(cur, top);
            qfFull.union(cur, top);
        }

        if (row < size && isOpen(bottom)) {
            qf.union(cur, bottom);
            qfFull.union(cur, bottom);
        }
    }

    /**
     * Shows if the whole grid percolates
     *
     * @return {@code true} if the grid percolates, {@code false} if it doesn't
     */
    public boolean percolates() {
        return qf.connected(virtualBottom, virtualTop);
    }

    /**
     * Shows if the site with given column and row is open
     *
     * @param row - row index
     * @param col - column index
     * @return {@code true} if site is open, {@code false} if it's not
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return isOpen(pos(row, col));
    }

    /**
     * Shows if the site with given column and row is full with liquid
     *
     * @param row - row index
     * @param col - column index
     * @return {@code true} if site is full, {@code false} if it's not
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        int cur = pos(row, col);
        return qfFull.connected(cur, virtualTopFull);
    }

    /**
     * Returns count of opened sites
     *
     * @return {@code int}
     */
    public int numberOfOpenSites() {
        return openedCount;
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
        return opened[pos];
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