/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
import java.util.ArrayList;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if(that == null){
            throw new NullPointerException();
            // 题目原文记得没有写，但是线上打分平台有测试样例
        }
        if(this.y == that.y && this.x != that.x){// horizontal
            return +0.0;
        }else if(this.x == that.x && this.y != that.y){// vertical
            return Double.POSITIVE_INFINITY;
        }else if( this.x == that.x && this.y == that.y){
            return Double.NEGATIVE_INFINITY;
        }else{
            return (this.y - that.y) / (double)(this.x - that.x);
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if( (this.y < that.y) ||
                ( (this.y == that.y) && (this.x < that.x) ) ){
            return -1;
        }else if( (this.y > that.y) ||
                ( (this.y == that.y) && (this.x > that.x) ) ){
            return 1;
        }else{
            return 0;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new SlopeOrder(this);
    }

    // 需要Bing一下 comparator 的相关讯息
    // 还有 Arrays.sort(arr, comparator) 的用法
    private class SlopeOrder implements Comparator<Point> {
        public Point p0;
        public SlopeOrder(Point invokePoint){
            this.p0 = invokePoint;
        }


        @Override
        public int compare(Point o1, Point o2) {
            if(o1 == null || o2 == null){
                throw new NullPointerException();
                // 题目原文记得没有写，但是线上打分平台有测试样例
            }

            double slope1 = p0.slopeTo(o1);
            double slope2 = p0.slopeTo(o2);
            if(slope1 < slope2){
                return -1;
            }else if(slope1 > slope2){
                return 1;
            }
            return 0;
        }
    }



    /**
     * Returns a string representation of this point.
     * This method is provided for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}