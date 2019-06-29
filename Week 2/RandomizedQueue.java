import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int size = 0;

    public RandomizedQueue() // construct an empty randomized queue
    {
        s = (Item[]) new Object[1]; // Java prohibits the creation of arrays of generic types.
    }

    public boolean isEmpty() // is the randomized queue empty?
    {
        return size == 0;
    }

    public int size() // return the number of items on the randomized queue
    {
        return size;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++)
            copy[i] = s[i];
        s = copy;
    }

    public void enqueue(Item item) // add the item
    {
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (size == s.length) resize(s.length * 2);
        s[size++] = item;
    }

    public Item dequeue() // remove and return a random item
    {
        if (size == 0) throw new java.util.NoSuchElementException();

        int n = StdRandom.uniform(size);
        Item item = s[n];
        s[n] = s[size - 1]; // put the last element to the empty entry to fill the gap
        s[--size] = null;

        // shrink the size if necessary
        if (size > 0 && size == s.length * 1 / 4) resize(s.length / 2);

        return item;
    }

    public Item sample() // return a random item (but do not remove it)
    {
        if (size == 0) throw new java.util.NoSuchElementException();

        return s[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() // return an independent iterator over items in random order
    {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int cur = 0;
        private final int[] order;

        public RandomizedQueueIterator() {
            order = new int[size];
            for (int j = 0; j < size; j++) {
                order[j] = j;
            }
            StdRandom.shuffle(order);
        }

        public boolean hasNext() {
            return cur < size;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();

            return s[order[cur++]];
        }

    }


    public static void main(String[] args) // unit testing (optional)
    {
        RandomizedQueue<String> r = new RandomizedQueue<>();
        System.out.println(r.isEmpty());

        String item = "abc";
        r.enqueue(item);

        System.out.println(r.isEmpty());

        Iterator<String> iterator = r.iterator();
        System.out.println(iterator.hasNext());
        System.out.println(iterator.next());

    }
}
