import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            q.enqueue(StdIn.readString());
        }

        int n = Integer.parseInt(args[0]);
        while (n > 0) {
            StdOut.println(q.dequeue());
            n--;
        }
    }
}
