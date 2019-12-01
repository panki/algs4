import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

class DequeIterator<Item> implements Iterator<Item> {
    private Node<Item> current;

    public DequeIterator(Node<Item> current) {
        this.current = current;
    }

    public boolean hasNext() {
        return this.current != null;
    }

    public Item next() {
        if (this.current == null)
            throw new NoSuchElementException();
        Item item = current.item;
        current = current.next;
        return item;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}

class Node<Item> {
    public Item item;
    public Node<Item> prev;
    public Node<Item> next;
}

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node<Item> node = new Node<Item>();
        node.item = item;

        if (this.size > 0) {
            node.next = this.first;
            this.first.prev = node;
        } else {
            this.last = node;
        }

        this.first = node;
        this.size++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (isEmpty()) {
            this.addFirst(item);
            return;
        }
        Node<Item> node = new Node<Item>();
        node.item = item;
        node.prev = this.last;

        this.last.next = node;
        this.last = node;
        this.size++;
    }

    public Item removeFirst() throws NoSuchElementException {
        if (this.isEmpty())
            throw new NoSuchElementException();
        Item item = this.first.item;
        Node<Item> next = this.first.next;
        if (next != null)
            next.prev = null;
        this.first = next;
        this.size--;
        return item;
    }

    public Item removeLast() throws NoSuchElementException {
        if (this.isEmpty())
            throw new NoSuchElementException();
        Item item = this.last.item;
        Node<Item> prev = this.last.prev;
        if (prev != null)
            prev.next = null;
        this.last = prev;
        this.size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator<Item>(this.first);
    }

    public static void main(String[] args) {
        Deque<Integer> q = new Deque<Integer>();

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size, q.isEmpty());

        int count = 6;

        StdOut.printf("Inserting: %d elements\n", count * 2);

        for (int i = 1; i <= count; i++) {
            StdOut.printf("addFirst(%d)\n", i * 10);
            q.addFirst(i * 10);
            StdOut.printf("addLast(%d)\n", i * 100);
            q.addLast(i * 100);
        }

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size, q.isEmpty());

        StdOut.println("Iterating:");
        for (int i : q) {
            StdOut.printf("Element: %d\n", i);
        }

        StdOut.printf("Removing: %d elements\n", count * 2);
        for (int i = 0; i < count; i++) {
            StdOut.printf("removeFirst = %d\n", q.removeFirst());
            StdOut.printf("removeLast = %d\n", q.removeLast());
        }

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size, q.isEmpty());
    }
}