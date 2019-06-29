import edu.princeton.cs.algs4.Stack;

public class Board {
    private int[][] board;

    // construct a board from an n-by-n array of blocks.
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        board = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                board[i][j] = blocks[i][j];
            }
        }
    }


    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) continue;
                if (board[i][j] != i * dimension() + j + 1) count++;
            }
        }

        return count;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                int cur = board[i][j];
                if (cur == 0) continue;
                cur -= 1;
                if (cur != i * dimension() + j) {
                    int rowShouldBe = cur / dimension();
                    int colShouldBe = cur % dimension();
                    sum += Math.abs(rowShouldBe - i) + Math.abs(colShouldBe - j);
                }
            }
        }

        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) continue;
                if (board[i][j] != i * dimension() + j + 1) return false;
            }
        }

        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int fromX = 0;
        int fromY = 0;
        int toX = 1;
        int toY = 0;

        if (board[fromX][fromY] == 0) {
            fromY++;
        }

        if (board[toX][toY] == 0) {
            toY++;
        }

        Board twinBoard = new Board(board);
        twinBoard.exch(fromX, fromY, toX, toY);
        return twinBoard;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;

        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.board[i][j] != that.board[i][j]) return false;
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> s = new Stack<>();

        int blankRow = 0, blankCol = 0;
        label:
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break label;
                }
            }
        }

        int exchRow, exchCol;

        // Left element
        exchRow = blankRow;
        exchCol = blankCol - 1;
        if (exchCol >= 0) {
            Board neighbor = new Board(board);
            neighbor.exch(blankRow, blankCol, exchRow, exchCol);
            s.push(neighbor);
        }

        // Right element
        exchRow = blankRow;
        exchCol = blankCol + 1;
        if (exchCol <= dimension() - 1) {
            Board neighbor = new Board(board);
            neighbor.exch(blankRow, blankCol, exchRow, exchCol);
            s.push(neighbor);
        }

        // Up element
        exchRow = blankRow - 1;
        exchCol = blankCol;
        if (exchRow >= 0) {
            Board neighbor = new Board(board);
            neighbor.exch(blankRow, blankCol, exchRow, exchCol);
            s.push(neighbor);
        }

        // Down element
        exchRow = blankRow + 1;
        exchCol = blankCol;
        if (exchRow <= dimension() - 1) {
            Board neighbor = new Board(board);
            neighbor.exch(blankRow, blankCol, exchRow, exchCol);
            s.push(neighbor);
        }

        return s;
    }

    private void exch(int i, int j, int p, int q) {
        int t = this.board[i][j];
        this.board[i][j] = this.board[p][q];
        this.board[p][q] = t;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }

        return s.toString();
    }


    public static void main(String[] args) {
        // unit tests (not graded)
    }
}
