import java.util.Arrays;

public class BruteCollinearPoints {
    private class Node{
        private LineSegment value;
        private Node next;
    }

    //private Stack<LineSegment> StackLineSeg; // 最开始用的栈，但是有测试需要多次调用segments()检查是否结果一致
    private Node first; // 自定义链表了，草。Stack不会超时，但是只有88分，ArrayList和LinkedList都超时
    private int nums; // number of segments
    public BruteCollinearPoints(Point[] points){// finds all line segments containing 4 points

        // check argument
        if(points == null){
            throw new IllegalArgumentException("Argument Illegal: points are null!");
        }
        for(int i=0;i<points.length;i++){
            if(points[i] == null){
                throw new IllegalArgumentException("Argument Illegal: a point is null!");
            }
            for(int j=i+1;j<points.length;j++){
                if(points[i].compareTo(points[j]) == 0){
                    throw new IllegalArgumentException("Argument Illegal: repeated points!");
                }
            }
        }

        first = null;
        nums = 0;

        Arrays.sort(points); // 按照 compareTo()的规则排序: 先y后x

//        System.out.println("After Sort: ");
//        for(int i=0;i<points.length;i++){
//            System.out.println(points[i].toString());
//        }

        // really sucks loop
        for(int p=0;p < points.length-3;p++){
            for(int q=p+1;q < points.length-2;q++){
                for(int r=q+1;r < points.length-1;r++){
                    for(int s=r+1;s < points.length;s++){

                        double slope1 = points[p].slopeTo(points[q]);
                        double slope2 = points[q].slopeTo(points[r]);
                        double slope3 = points[r].slopeTo(points[s]);

                        if( slope1 == slope2 && slope2 == slope3 ){
                            nums++;

                            // 头插法
                            Node tmpNode = new Node();
                            tmpNode.value = new LineSegment(points[p],points[s]);
                            tmpNode.next = first;
                            first = tmpNode;
                        }
                    }
                }
            }
        }
    }
    public int numberOfSegments(){// the number of line segments
        return nums;
    }
    public LineSegment[] segments(){// the line segments
        LineSegment[] LineSeg = new LineSegment[nums];
        for(int i=0;i<nums;i++){
            LineSeg[i] = first.value;
            first = first.next;
        }
        return LineSeg;
    }


    // just for test;
//    public static void main(String[] args) {
//
//        // read the n points from a file
//        In in = new In(args[0]);
//        int n = in.readInt();
//        Point[] points = new Point[n];
//        for (int i = 0; i < n; i++) {
//            int x = in.readInt();
//            int y = in.readInt();
//            points[i] = new Point(x, y);
//        }
//
//        // draw the points
//        StdDraw.enableDoubleBuffering();
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//        StdDraw.show();
//
//        // print and draw the line segments
//        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
//        for (LineSegment segment : collinear.segments()) {
//            StdOut.println(segment);
//            segment.draw();
//        }
//        StdDraw.show();
//    }
}
