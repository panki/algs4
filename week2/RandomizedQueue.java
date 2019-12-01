import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private Item[] list;

    public RandomizedQueue() {
        list = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    private void resize(int capacity) {
        assert capacity > n;
        Item[] tmp = (Item[]) new Object[capacity];
        System.arraycopy(list, 0, tmp, 0, n);
        list = tmp;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (n == list.length)
            resize(n << 1);
        list[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        if (n < list.length >> 2)
            resize(list.length >> 1);
        if (n > 1) {
            int i = StdRandom.uniform(n);
            swap(i, n - 1);
        }
        n--;
        Item item = list[n];
        list[n] = null;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        return list[StdRandom.uniform(n)];
    }

    private void swap(int i, int j) {
        Item tmp = list[i];
        list[i] = list[j];
        list[j] = tmp;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        Item[] items;
        int count;

        public ListIterator() {
            items = (Item[]) new Object[n];
            System.arraycopy(list, 0, items, 0, n);
            count = items.length;
        }

        public boolean hasNext() {
            return count > 0;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            count--;
            Item tmp;
            if (count > 0) {
                int i = StdRandom.uniform(count + 1);
                tmp = items[i];
                items[i] = items[count];
            } else {
                tmp = items[count];
            }
            items[count] = null;
            return tmp;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
        int count = 5;

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size(), q.isEmpty());
        StdOut.printf("Inserting: %d elements\n", count);

        for (int i = 0; i < count; i++) {
            q.enqueue(i);
        }

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size(), q.isEmpty());

        StdOut.println("Iterating:");
        for (int i : q) {
            StdOut.printf("Element: %d\n", i);
        }

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size(), q.isEmpty());

        StdOut.printf("Removing: %d elements\n", count);

        for (int i = 0; i < count; i++) {
            StdOut.printf("Element: %d\n", q.dequeue());
        }

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size(), q.isEmpty());
    }

}