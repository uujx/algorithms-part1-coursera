public class ResizingArrayStack {
    private String[] s;
    private int N = 0;

    public ResizingArrayStack() {
        s = new String[1];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public void push(String item) {
        if (N == s.length) {
            resize(2 * s.length);
        }
        s[N++] = item;
    }

    public String pop() {
        String item = s[--N];
        s[N] = null;
        if (N > 0 && N == s.length * 1 / 4) {
            resize(s.length * 1 / 2);
        }
        return item;
    }

    private void resize(int capacity) {
        String[] copy = new String[capacity];
        for (int i = 0; i < s.length; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }

}
