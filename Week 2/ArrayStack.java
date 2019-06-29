public class ArrayStack {
    private String[] s;
    private int N = 0;

    public ArrayStack(int capacity) {
        s = new String[capacity];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public void push(String item) {
        s[N++] = item;
    }

    public String pop() {
//        return s[--N];
        String item = s[--N];
        s[N] = null; // to avoid "loitering", which is holding references to objects when they are no longer exist.
        return item;
    }
}
