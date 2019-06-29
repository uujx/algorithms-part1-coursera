public class LinkedListStack {
    private Node first = null;

    private class Node {
        String item;
        Node next;
    }

    public void push(String item) {
        Node oldNode = first;
        first = new Node();
        first.item = item;
        first.next = oldNode;
    }

    public Node pop() {
        String item = first.item;
        first = first.next;
        return item;
    }

    public boolean isEmpty() {
        return first == null;
    }
    
}
