import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

/**
public class Point2D implements Comparable<Point2D> {
   public Point2D(double x, double y)              // construct the point (x, y)
   public  double x()                              // x-coordinate
   public  double y()                              // y-coordinate
   public  double distanceTo(Point2D that)         // Euclidean distance between two points
   public  double distanceSquaredTo(Point2D that)  // square of Euclidean distance between two points
   public     int compareTo(Point2D that)          // for use in an ordered symbol table
   public boolean equals(Object that)              // does this point equal that object?
   public    void draw()                           // draw to standard draw
   public  String toString()                       // string representation
}

public class RectHV {
   public    RectHV(double xmin, double ymin,      // construct the rectangle [xmin, xmax] x [ymin, ymax]
                    double xmax, double ymax)      // throw an IllegalArgumentException if (xmin > xmax) or (ymin > ymax)
   public  double xmin()                           // minimum x-coordinate of rectangle
   public  double ymin()                           // minimum y-coordinate of rectangle
   public  double xmax()                           // maximum x-coordinate of rectangle
   public  double ymax()                           // maximum y-coordinate of rectangle
   public boolean contains(Point2D p)              // does this rectangle contain the point p (either inside or on boundary)?
   public boolean intersects(RectHV that)          // does this rectangle intersect that rectangle (at one or more points)?
   public  double distanceTo(Point2D p)            // Euclidean distance from point p to closest point in rectangle
   public  double distanceSquaredTo(Point2D p)     // square of Euclidean distance from point p to closest point in rectangle
   public boolean equals(Object that)              // does this rectangle equal that object?
   public    void draw()                           // draw to standard draw
   public  String toString()                       // string representation
}
*/


// Brute-Force implementation.
public class PointSET {
    private SET<Point2D> pointSet; // set of points

    public PointSET(){// construct an empty set of points
        pointSet = new SET<>();
    }
    public boolean isEmpty(){// is the set empty?
        return pointSet.isEmpty();
    }
    public int size(){// number of points in the set
        return pointSet.size();
    }
    public void insert(Point2D p){// add the point to the set (if it is not already in the set)
        if(p == null){
            throw new IllegalArgumentException();
        }
        pointSet.add(p);
    }
    public boolean contains(Point2D p){// does the set contain point p?
        if(p == null){
            throw new IllegalArgumentException();
        }
        return pointSet.contains(p);
    }
    public void draw(){// draw all points to standard draw
        for(Point2D p : pointSet){
            p.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect){// all points that are inside the rectangle (or on the boundary)
        if(rect == null){
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> inRectPoints = new ArrayList<>();
        for(Point2D p : pointSet){
            if(rect.contains(p)){
                inRectPoints.add(p);
            }
        }
        return inRectPoints;
    }
    public Point2D nearest(Point2D p){// a nearest neighbor in the set to point p; null if the set is empty
        if(p == null){
            throw new IllegalArgumentException();
        }
        if(pointSet.isEmpty()){
            return null;
        }
        Point2D res = null;
        double minDistance = 1.5; // due to unit square, the max distance is sqrt(2)
        double tmpDistance = 0.0; // just for saving time.
        for(Point2D tmpP : pointSet){
            tmpDistance = p.distanceSquaredTo(tmpP);
            if(tmpDistance < minDistance){
                minDistance = tmpDistance;
                res = tmpP;
            }
        }
        return res;
    }

    public static void main(String[] args){// unit testing of the methods (optional)

    }
}
