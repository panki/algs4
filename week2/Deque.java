import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.ListIterator;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        public Item item;
        public Node prev;
        public Node next;
    }

    private Node first;
    private Node last;
    private int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node node = new Node();
        node.item = item;

        if (size > 0) {
            node.next = first;
            first.prev = node;
        } else {
            last = node;
        }

        first = node;
        size++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (isEmpty()) {
            addFirst(item);
            return;
        }
        Node node = new Node();
        node.item = item;
        node.prev = last;

        last.next = node;
        last = node;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = first.item;
        Node next = first.next;
        first = next;
        size--;

        if (size > 0) {
            next.prev = null;
        } else {
            first = null;
            last = null;
        }

        return item;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = last.item;
        Node prev = last.prev;
        last = prev;
        size--;
        if (size > 0) {
            prev.next = null;
        } else {
            first = null;
            last = null;
        }

        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {

        Deque<Integer> q = new Deque<Integer>();

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size(), q.isEmpty());

        int count = 10;

        StdOut.printf("Inserting: %d elements\n", count * 2);

        for (int i = 1; i <= count; i++) {
            StdOut.printf("addFirst(%d)\n", i * 10);
            q.addFirst(i * 10);
            StdOut.printf("addLast(%d)\n", i * 100);
            q.addLast(i * 100);
        }

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size(), q.isEmpty());

        StdOut.println("Iterating:");
        for (int i : q) {
            StdOut.printf("Element: %d\n", i);
        }

        StdOut.printf("Removing: %d elements\n", count * 2);
        for (int i = 0; i < count; i++) {
            StdOut.printf("removeFirst = %d\n", q.removeFirst());
            StdOut.printf("removeLast = %d\n", q.removeLast());
        }

        StdOut.printf("Size: %d, IsEmpty: %s\n", q.size(), q.isEmpty());
    }
}