import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class FastCollinearPointsHashMap {
    private final LineSegment[] lineSegments;
    private final ArrayList<LineSegment> lineSegmentsList = new ArrayList<>();
    private final HashMap<Double, ArrayList<Point>> startingPointsWithSameSlope = new HashMap<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPointsHashMap(Point[] points) {
        if (isNullPoints(points)) {
            throw new IllegalArgumentException("Points array can't be null or contain null values");
        }

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);

        if (hasDuplicatedPoints(pointsCopy)) {
            throw new IllegalArgumentException("Points array can't contain duplicated points");
        }


        ArrayList<Point> segmentPoints = new ArrayList<>();

        for (Point point : points) {
            // Sort based on the slope each point made with point
            Arrays.sort(pointsCopy, point.slopeOrder());

            double lastPointSlope = 0;
            // Check if any 3 or more adjacent points have equal slopes with respect to point.
            for (Point p : pointsCopy) {
                if (p.compareTo(point) == 0) {
                    continue;
                }

                double currentSlope = point.slopeTo(p);
                // First loop
                if (segmentPoints.isEmpty()) {
                    segmentPoints.add(p);
                    lastPointSlope = currentSlope;

                    // If the slope point p made with respect to point is equal to the last one, add it
                } else if (currentSlope == lastPointSlope) {
                    segmentPoints.add(p);

                    // If it doesn't , check if we already have 3 or more points in the data structure
                } else if (currentSlope != lastPointSlope) {

                    addSegment(segmentPoints, point, lastPointSlope);

                    segmentPoints.clear();
                    segmentPoints.add(p);
                    lastPointSlope = currentSlope;
                }
            }

            addSegment(segmentPoints, point, lastPointSlope);
        }

        // Transform the list to array
        lineSegments = lineSegmentsList.toArray(new LineSegment[lineSegmentsList.size()]);
    }


    /**
     * Check if it is satisfied to add a new segment
     * Note: Using only slope or startingPoint to determine is not enough
     * Ex. Two different lines with the same slope
     * Ex. Two different lines starting at the same point
     */
    private void addSegment(ArrayList<Point> segmentPoints, Point point, double slope) {
        if (segmentPoints.size() < 3) {
            return;
        }

        // Add outer iteration point into segment
        segmentPoints.add(point);
        Collections.sort(segmentPoints);

        Point startingPoint = segmentPoints.get(0);
        Point endPoint = segmentPoints.get(segmentPoints.size() - 1);

        ArrayList<Point> startingPoints;

        // Check if the starting point is already in the list, it means this is a sub-segment
        if (startingPointsWithSameSlope.containsKey(slope)) {
            startingPoints = startingPointsWithSameSlope.get(slope);
            for (Point sp : startingPoints) {
                if (sp.compareTo(startingPoint) == 0) {
                    return;
                }
            }

            startingPoints.add(startingPoint);

        } else {
            startingPoints = new ArrayList<>();
            startingPoints.add(startingPoint);
            startingPointsWithSameSlope.put(slope, startingPoints);
        }

        lineSegmentsList.add(new LineSegment(startingPoint, endPoint));
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
        FastCollinearPointsHashMap collinear = new FastCollinearPointsHashMap(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
