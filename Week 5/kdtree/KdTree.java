import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final boolean VERTICAL = true;
    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;
        private final boolean isVertival;
        private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, boolean orientation) {
            this.p = p;
            this.isVertival = orientation;
            this.lb = null;
            this.rt = null;
        }

        public void setRect(RectHV rect) {
            this.rect = rect;
        }
    }


    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }


    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();

        if (contains(p)) return;

        if (root == null) {
            root = new Node(p, VERTICAL);
            root.setRect(new RectHV(0, 0, 1, 1));
            size++;
            return;
        }

        root = insert(root, null, false, VERTICAL, p);
    }

    private Node insert(Node cur, Node prev, boolean isLeft, boolean orientation, Point2D p) {
        if (cur == null) {
            size++;
            cur = new Node(p, orientation);
            cur.setRect(generateRect(prev, isLeft));

            return cur;
        }

        double cmp;
        if (orientation == VERTICAL) cmp = p.x() - cur.p.x();
        else cmp = p.y() - cur.p.y();

        if (cmp < 0) cur.lb = insert(cur.lb, cur, true, !orientation, p);
        else cur.rt = insert(cur.rt, cur, false, !orientation, p);

        return cur;
    }

    private RectHV generateRect(Node prev, boolean isLeft) {
        RectHV rect;
        if (prev.isVertival) {
            if (isLeft) {
                rect = new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.p.x(), prev.rect.ymax());
            } else {
                rect = new RectHV(prev.p.x(), prev.rect.ymin(), prev.rect.xmax(), prev.rect.ymax());
            }
        } else {
            if (isLeft) {
                rect = new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.rect.xmax(), prev.p.y());
            } else {
                rect = new RectHV(prev.rect.xmin(), prev.p.y(), prev.rect.xmax(), prev.rect.ymax());
            }
        }

        return rect;
    }


    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();

        return contains(root, VERTICAL, p);
    }

    private boolean contains(Node cur, boolean orientation, Point2D p) {
        if (cur == null) return false;
        if (cur.p.compareTo(p) == 0) return true;

        double cmp;
        if (orientation == VERTICAL) cmp = p.x() - cur.p.x();
        else cmp = p.y() - cur.p.y();

        boolean res = false;
        if (cmp < 0) res = contains(cur.lb, !orientation, p);
        else res = contains(cur.rt, !orientation, p);

        return res;
    }


    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node cur) {
        if (cur == null) return;

        draw(cur.lb);

        // Draw the point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(cur.p.x(), cur.p.y());
        StdDraw.setPenRadius();

        // Draw the line
        if (cur.isVertival) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(cur.p.x(), cur.rect.ymin(), cur.p.x(), cur.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(cur.rect.xmin(), cur.p.y(), cur.rect.xmax(), cur.p.y());
        }
        StdDraw.setPenRadius();

        draw(cur.rt);
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Stack<Point2D> s = new Stack<>();
        if (isEmpty()) return s;

        range(s, root, rect);
        return s;
    }

    private void range(Stack<Point2D> s, Node cur, RectHV rect) {
        if (!rect.intersects(cur.rect)) return;

        if (rect.contains(cur.p)) s.push(cur.p);
        if (cur.lb != null) range(s, cur.lb, rect);
        if (cur.rt != null) range(s, cur.rt, rect);
    }


    // a nearest neighbor in the set to point p; null if the set is empty)
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        MinData mindata = new MinData(root.p, Double.POSITIVE_INFINITY);
        mindata = nearest(root, p, mindata);
        return mindata.nearestPoint;
    }

    private MinData nearest(Node cur, Point2D p, MinData minData) {
        if (cur == null) return minData;

        if (minData.champ < cur.rect.distanceSquaredTo(p)) return minData;

        double distance = cur.p.distanceSquaredTo(p);
        if (distance < minData.champ) {
            minData.champ = distance;
            minData.nearestPoint = cur.p;
        }

        // Set priority
        Node first = cur.lb;
        Node second = cur.rt;
        if (cur.rt != null && cur.rt.rect.contains(p)) {
            first = cur.rt;
            second = cur.lb;
        }

        minData = nearest(first, p, minData);
        minData = nearest(second, p, minData);

        return minData;
    }

    private class MinData {
        private Point2D nearestPoint;
        private double champ;

        public MinData(Point2D p, double champ) {
            this.nearestPoint = p;
            this.champ = champ;
        }
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)

        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
//            double x = StdDraw.mouseX();
//            double y = StdDraw.mouseY();
            double x = 0.746094;
            double y = 0.708984;

            Point2D query = new Point2D(x, y);

//            StdOut.printf("%8.6f %8.6f\n", x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            query.draw();
            kdtree.draw();

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenRadius(0.02);
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.nearest(query).draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
    }
}
