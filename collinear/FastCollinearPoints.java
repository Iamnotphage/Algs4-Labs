/*************************************************************************
 A faster, sorting-based solution. Remarkably,
 it is possible to solve the problem much faster than the brute-force
 solution described above.Given a point p, the following method determines
 whether p participates in a set of 4 or more collinear points.

 * Think of p as the origin.
 * For each other point q, determine the slope it makes with p.
 * Sort the points according to the slopes they make with p.
 * Check if any 3 (or more) adjacent points in the sorted order have equal
   slopes with respect to p. If so, these points, together with p, are collinear.
 *************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    // The order of growth of the running time of your program should be n2 log n in the worst case
    private class Node{
        private LineSegment value;
        private Node next;
    }

    //private Stack<LineSegment> StackLineSeg; // 最开始用的栈，但是有测试需要多次调用segments()检查是否结果一致
    private Node first; // 自定义链表了，草。Stack不会超时，但是只有88分，ArrayList和LinkedList都超时
    private int nums;
    public FastCollinearPoints(Point[] points){// finds all line segments containing 4 or more points
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
                    // 尼玛，测试平台说我没抛出异常，本地机器明明可以
                }
            }
        }

        first = null;
        nums = 0;

        // 让点集都按照y坐标排序
        // 这样才能使后续按斜率排序时，先遍历的点是y坐标较小的点
        // 因为上述代码对重复点的可能性抛出了异常，所以排序的稳定性不用考虑
        Arrays.sort(points);

        // 思路：遍历n个点，每次第i个点为原点，给i点和剩下n-1个点根据斜率排序
        // 然后排序之后，查重，找到连续4个及以上的点在同一直线上

        // N * (N + NlogN + ?)
        for(int i = 0; i < points.length; i++){
            // points[i] 就是 op
            // op launch
            Point[] tmpPoints = new Point[points.length - 1]; // tmpPoints需要根据斜率排序
            int tmpPointsLength = 0;

            // 给tmpPoints赋值
            for(int j = 0; j < points.length; j++){
                if(j == i){
                    continue;
                }
               tmpPoints[tmpPointsLength] = points[j];
               tmpPointsLength++;
            }

            // 根据斜率排序 都是针对op的 p[i]和op之间的斜率按照大小来排序
            // 必须稳定，因为前文Arrays.sort(points);已经排序好了点集
            // 再根据斜率来排序点集时，相同斜率的点不能顺序搞乱，这样可能导致 p->q->r->s->t 乱序
            // 查阅资料显示 使用比较器的排序用的是 MergeSort 所以这里应该能保证稳定性
            // 'This sort is guaranteed to be stable: equal elements will not be reordered as a result of the sort.'
            Arrays.sort(tmpPoints, points[i].slopeOrder());

//            for(int k=0;k<tmpPointsLength;k++){
//                StdOut.println("in loop"+i+" tmpPoints: "+tmpPoints[k]);
//            }

            // 查找4个及以上的点
            // 下面的操作等价于 ---> 给定有序数组，查找4个及以上相同值的元素
            // 但是如果遍历到 以中间的某个点为原点，比如一个十字的交点
            // 起点就不是原点，而是需要比较来得出起点

            Point startPoint = null; // 共线的线段中，起始的点
            Point endPoint = null; // 共线的线段中，结束的点
            int count = 0; // 同一线段上的点数 因为原点默认是线段上的

            for(int j=0;j<tmpPointsLength-1;j++){
                if(points[i].slopeTo(tmpPoints[j]) == points[i].slopeTo(tmpPoints[j+1])){
                    // j和j+1对op的斜率一样
                    // 处理j和j+1点，谁是起点，谁是终点

                    // 最开始，startPoint == null
                    if(startPoint == null){
                        if(points[i].compareTo(tmpPoints[j]) > 0){
                            // points[i] > tmpPoints[j] 表明 j点y坐标更小
                            endPoint = points[i];
                            startPoint = tmpPoints[j];
                        }else{
                            // 否则 i点y坐标更小 （不存在相同点）
                            endPoint = tmpPoints[j];
                            startPoint = points[i];
                        }
                    }

                    if(startPoint.compareTo(tmpPoints[j+1]) > 0){
                        // j+1 的y坐标更小，需要更新起始点
                        startPoint = tmpPoints[j+1];
                    }

                    if(endPoint.compareTo(tmpPoints[j+1]) < 0){
                        // j+1 的y坐标更大，需要更新终点
                        endPoint = tmpPoints[j+1];
                    }

                    count++;

                    // j和j+1是最后两个结点
                    if( j == tmpPointsLength - 2){
                        // 此时此刻，j和j+1点对op斜率是一样的
                        // 所以count只需要大于等于2
                        // 这里需要非常小心，如果是线段中间的某个点作为op
                        // 那么该线段的startPoint必然在其他循环中被计算过，
                        // 所以一定要保证startPoint和op是同一个点，才能去掉重复线段
                        if(count >= 2 && points[i].compareTo(startPoint)==0){
                            // 头插法
                            Node tmpNode = new Node();
                            tmpNode.value = new LineSegment(startPoint,endPoint);
                            tmpNode.next = first;
                            first = tmpNode;
                            nums++;
                        }

                        count = 0;
                        startPoint = null;
                        endPoint = null;
                    }

                }else{
                    // j和j+1对op的斜率不一样
                    if(count >= 2 && points[i].compareTo(startPoint)==0){
                        // 头插法
                        Node tmpNode = new Node();
                        tmpNode.value = new LineSegment(startPoint,endPoint);
                        tmpNode.next = first;
                        first = tmpNode;
                        nums++;
                    }

                    count = 0;
                    startPoint = null;
                    endPoint = null;
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

    public static void main(String[] args) {

        // read the n points from a file
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
