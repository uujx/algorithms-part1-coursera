import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> treeSet;

    // construct an empty set of points
    public PointSET() {
        treeSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return treeSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();

        treeSet.add(p);
    }


    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();

        return treeSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : treeSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();

        Stack<Point2D> s = new Stack<>();
        for (Point2D p : treeSet) {
            if (rect.contains(p)) {
                s.push(p);
            }
        }

        return s;
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        if (treeSet.isEmpty()) return null;

        Point2D nearestPoint = treeSet.first();
        double minDistance = p.distanceSquaredTo(nearestPoint);

        for (Point2D cur : treeSet) {
            if (p.distanceSquaredTo(cur) < minDistance) {
                minDistance = p.distanceSquaredTo(cur);
                nearestPoint = cur;
            }
        }

        return nearestPoint;
    }


    public static void main(String[] args) {
        // unit testing of the methods (optional)
    }
}
