import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        private Item item;
        private Node next;
        private Node pre;

        public Node() {
            item = null;
            next = null;
            pre = null;
        }
    }

    public Deque() // construct an empty deque
    {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() // is the deque empty?
    {
        return size == 0;
    }

    public int size() // return the number of items on the deque
    {
        return size;
    }

    private void checkArgument(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    public void addFirst(Item item) // add the item to the front
    {
        checkArgument(item);

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.pre = null;

        if (isEmpty()) last = first;
        else oldFirst.pre = first;

        size++;
    }

    public void addLast(Item item) // add the item to the end
    {
        checkArgument(item);

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.pre = oldLast;

        if (isEmpty()) first = last;
        else oldLast.next = last;

        size++;
    }

    public Item removeFirst() // remove and return the item from the front
    {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        Item item = first.item;
        first.item = null;
        first = first.next;
        size--;

        if (isEmpty()) last = null;
        else first.pre = null;

        return item;
    }

    public Item removeLast() // remove and return the item from the end
    {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        Item item = last.item;
        last.item = null;
        last = last.pre;
        size--;

        if (isEmpty()) first = null;
        else last.next = null;

        return item;
    }


    public Iterator<Item> iterator() // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node cur = first;

        public boolean hasNext() {
            return cur != null;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();

            Item item = cur.item;
            cur = cur.next;
            return item;
        }
    }


    public static void main(String[] args) // unit testing (optional)
    {
        Deque<String> d = new Deque<>();
        System.out.println(d.isEmpty());

        String item = "abc";
//        d.addLast(item);

        System.out.println(d.isEmpty());

        Iterator<String> iterator = d.iterator();
        System.out.println(iterator.hasNext());
        System.out.println(iterator.next());

//        System.out.println(d.removeFirst());

        d.addFirst(item);
    }
}
