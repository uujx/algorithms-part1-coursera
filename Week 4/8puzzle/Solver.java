import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private boolean solvable;
    private final Stack<Board> solutionSeq = new Stack<>();


    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode predecessor;
        private final int priority;

        SearchNode(Board initial) {
            board = initial;
            moves = 0;
            predecessor = null;
            priority = board.manhattan();
        }

        SearchNode(Board neighbor, int moves, SearchNode predecessor) {
            board = neighbor;
            this.moves = moves;
            this.predecessor = predecessor;
            priority = neighbor.manhattan() + moves;
        }

        public int compareTo(SearchNode other) {
            return this.priority - other.priority;
        }
    }


    // find a solution to the initial board using the A* algorithm
    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }

        MinPQ<SearchNode> mainSearch = new MinPQ<>();
        MinPQ<SearchNode> parallelSearch = new MinPQ<>();

        mainSearch.insert(new SearchNode(initial));
        parallelSearch.insert(new SearchNode(initial.twin()));

        boolean mainSearchIsRunning = true;
        SearchNode cur;

        while (true) {
            if (mainSearchIsRunning) {
                if (!mainSearch.isEmpty()) {
                    cur = mainSearch.delMin();
                } else {
                    cur = parallelSearch.delMin();
                }
            } else {
                if (!parallelSearch.isEmpty()) {
                    cur = parallelSearch.delMin();
                } else {
                    cur = mainSearch.delMin();
                }
            }

            if (cur.board.isGoal()) {
                if (!mainSearchIsRunning) {
                    solvable = false;
                    return;
                } else {
                    solvable = true;
                    while (cur != null) {
                        solutionSeq.push(cur.board);
                        cur = cur.predecessor;
                    }
                    return;
                }
            }

            Iterable<Board> neighbors = cur.board.neighbors();
            for (Board neighbor : neighbors) {
                if (cur.predecessor == null || !cur.predecessor.board.equals(neighbor)) {
                    SearchNode next = new SearchNode(neighbor, cur.moves + 1, cur);
                    (mainSearchIsRunning ? mainSearch : parallelSearch).insert(next);
                }
            }

            mainSearchIsRunning = !mainSearchIsRunning;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? solutionSeq.size() - 1 : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? solutionSeq : null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            Solver solver = new Solver(initial);
            StdOut.println(filename + ": " + solver.moves());

            for (Board board : solver.solution()) {
                StdOut.println("manhattan: " + board.manhattan());
                StdOut.println(board.toString());
                StdOut.println(board.twin().toString() + "\n");
            }
        }
    }
}
