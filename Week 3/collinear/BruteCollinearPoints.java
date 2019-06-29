import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;


public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (isNullPoints(points)) {
            throw new IllegalArgumentException("Points array can't be null or contain null values");
        }

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);

        if (hasDuplicatedPoints(pointsCopy)) {
            throw new IllegalArgumentException("Points array can't contain duplicated points");
        }

        final int pointsLength = pointsCopy.length;
        ArrayList<LineSegment> lineSegmentList = new ArrayList<>();

        for (int p = 0; p < pointsLength - 3; p++) {
            for (int q = p + 1; q < pointsLength - 2; q++) {
                for (int r = q + 1; r < pointsLength - 1; r++) {
                    if (pointsCopy[p].slopeTo(pointsCopy[q]) != pointsCopy[p].slopeTo(pointsCopy[r])) {
                        continue;
                    }

                    for (int s = r + 1; s < pointsLength; s++) {
                        if (pointsCopy[p].slopeTo(pointsCopy[q]) == pointsCopy[p].slopeTo(pointsCopy[s])) {
                            lineSegmentList.add(new LineSegment(pointsCopy[p], pointsCopy[s]));
                        }
                    }
                }
            }
        }

        lineSegments = lineSegmentList.toArray(new LineSegment[lineSegmentList.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(this.lineSegments, this.lineSegments.length);
    }


    // Check if points is null or has null point
    private boolean isNullPoints(Point[] points) {
        if (points == null) {
            return true;
        }

        for (Point point : points) {
            if (point == null) {
                return true;
            }
        }

        return false;
    }

    // Check if points has duplicated points
    private boolean hasDuplicatedPoints(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                return true;
            }
        }

        return false;
    }


    public static void main(String[] args) {
        // read the n points from a file In in = new In(args[0]);
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
