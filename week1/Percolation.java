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
    private final int size, len;

    /**
     * {@code int} variable representing the grid's opened sites count
     */
    private int openedCount;

    /**
     * {@code byte} array representing the state of the grid
     */
    private final byte[] state;
    private static final byte sOpen = 1;
    private static final byte sFull = 2;

    /**
     * {@code boolean} variable representing if the grid perculates or not
     */
    private boolean percolates;

    /**
     * Variable representing {@code WeightedQuickUnionUF} class which implements
     * weighted quick union-find algorithm, uses 2 virtual sites top and bottom to
     * calculate perculation
     */
    private final WeightedQuickUnionUF qf;

    /**
     * {@code int} variable representing the reserved virtual top site index. It is
     * used for simplified union with top/bottom row of the grid
     */
    private final int virtualTop;

    /**
     * Array used as stack to traverse the grid in markAsFull
     */
    private final int[] stack;

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
        len = size * size;

        state = new byte[len];
        qf = new WeightedQuickUnionUF(len + 1); // 1 extra for virtual top

        virtualTop = len;

        // For current implementation of markAsFull it's enough
        stack = new int[len > 1 ? len / 2 : 1];
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

        if (isOpen(cur) || isFull(cur))
            return;

        state[cur] = sOpen;
        openedCount++;

        int left = posLeft(cur);
        int right = posRight(cur);
        int top = posTop(cur);
        int bottom = posBottom(cur);

        if (row == 1)
            qf.union(cur, virtualTop);

        if (col > 1 && isOpenOrFull(left))
            qf.union(cur, left);

        if (col < size && isOpenOrFull(right))
            qf.union(cur, right);

        if (row > 1 && isOpenOrFull(top))
            qf.union(cur, top);

        if (row < size && isOpenOrFull(bottom))
            qf.union(cur, bottom);

        if (row == 1 || qf.connected(cur, virtualTop))
            markAsFull(row, col);
    }

    private void markAsFull(int row, int col) {
        int p = pos(row, col);
        if (!isOpen(p) || isFull(p))
            return;

        int i = 0;
        stack[i] = p;

        do {
            p = stack[i--];

            setFull(p);

            for (int j = 0; j < 4; j++) {
                int q = -1;

                switch (j) {
                case 0:
                    if (p > 0 && p % size != 0)
                        q = posLeft(p);
                    break;
                case 1:
                    if (p >= size)
                        q = posTop(p);
                    break;
                case 2:
                    if (p < len && (p + 1) % size != 0)
                        q = posRight(p);
                    break;
                case 3:
                    if (p < len - size)
                        q = posBottom(p);
                    break;
                default:
                    continue;
                }

                boolean push = false;

                if (q < 0 || q >= len || !isOpen(q) || isFull(q))
                    continue;

                setFull(q);

                // left
                if (q > 0 && q % size != 0 && state[q - 1] == sOpen)
                    push = true;

                // up
                if (q >= size && state[q - size] == sOpen)
                    push = true;

                // right
                if (q < len && (q + 1) % size != 0 && state[q + 1] == sOpen)
                    push = true;

                // down
                if (q < len - size && state[q + size] == sOpen)
                    push = true;

                if (push) {
                    stack[++i] = q;
                }
            }
        } while (i >= 0);
    }

    /**
     * Shows if the whole grid percolates
     *
     * @return {@code true} if the grid percolates, {@code false} if it doesn't
     */
    public boolean percolates() {
        return percolates;
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
        int cur = pos(row, col);
        return isOpen(cur) || isFull(cur);
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
        return isFull(cur);
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

    private boolean isOpenOrFull(int pos) {
        return state[pos] >= sOpen;
    }

    private boolean isOpen(int pos) {
        return state[pos] == sOpen;
    }

    private boolean isFull(int pos) {
        return state[pos] == sFull;
    }

    private void setFull(int pos) {
        state[pos] = sFull;
        if (pos >= len - size)
            percolates = true;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 20;
        Percolation p = new Percolation(n);
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                p.open(i, j);
            }
        }
        boolean f = true;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                f = f && p.isFull(i, j);
            }
        }
        System.out.println(f);
        System.out.println(p.percolates());
    }
}