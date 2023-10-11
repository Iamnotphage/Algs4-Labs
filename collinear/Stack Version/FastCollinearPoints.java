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
import java.util.Iterator;
import java.util.Stack;

public class FastCollinearPoints {
    // The order of growth of the running time of your program should be n2 log n in the worst case
    private Stack<LineSegment> StackLineSeg = new Stack<>(); // 最开始用的栈，但是有测试需要多次调用segments()检查是否结果一致,栈弹出之后就没了，要么再来一个辅助栈存内容，要么直接链表
    private int nums;
    public FastCollinearPoints(Point[] points){// finds all line segments containing 4 or more points
        // check argument
        if(points == null){
            throw new IllegalArgumentException("Argument Illegal: points are null!");
        }

        Point[] clonePoints = new Point[points.length]; // 本来可以原地操作的，但是测试样例有检查points是否改变，那就只能辅助数组了

        for(int i=0;i<points.length;i++){
            if(points[i] == null){
                throw new IllegalArgumentException("Argument Illegal: a point is null!");
            }
            for(int j=i+1;j<points.length;j++){
                if(points[j] == null){
                    // 这里非常小心，如果输入中有null，i点检查了，但是j没有检查，
                    // 然后直接调用下面的compareTo的话会导致抛出错误的异常(NullPointer)，从而某些样例不能通过
                    throw new IllegalArgumentException("Argument Illegal: a point is null!");
                }
                if(points[i].compareTo(points[j]) == 0){
                    throw new IllegalArgumentException("Argument Illegal: duplicate points!");
                }
            }
            clonePoints[i] = points[i];
        }

        nums = 0;

        // 让点集都按照y坐标排序
        // 这样才能使后续按斜率排序时，先遍历的点是y坐标较小的点
        // 因为上述代码对重复点的可能性抛出了异常，所以排序的稳定性不用考虑
        Arrays.sort(clonePoints);

        // 思路：遍历n个点，每次第i个点为原点，给i点和剩下n-1个点根据斜率排序
        // 然后排序之后，查重，找到连续4个及以上的点在同一直线上

        // N * (N + NlogN + ?)
        for(int i = 0; i < clonePoints.length; i++){
            // points[i] 就是 op
            // op launch
            Point[] tmpPoints = new Point[clonePoints.length - 1]; // tmpPoints需要根据斜率排序
            int tmpPointsLength = 0;

            // 给tmpPoints赋值
            for(int j = 0; j < clonePoints.length; j++){
                if(j == i){
                    continue;
                }
               tmpPoints[tmpPointsLength] = clonePoints[j];
               tmpPointsLength++;
            }

            // 根据斜率排序 都是针对op的 p[i]和op之间的斜率按照大小来排序
            // 必须稳定，因为前文Arrays.sort(points);已经排序好了点集
            // 再根据斜率来排序点集时，相同斜率的点不能顺序搞乱，这样可能导致 p->q->r->s->t 乱序
            // 查阅资料显示 使用比较器的排序用的是 MergeSort 所以这里应该能保证稳定性
            // 'This sort is guaranteed to be stable: equal elements will not be reordered as a result of the sort.'
            Arrays.sort(tmpPoints, clonePoints[i].slopeOrder());

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
                if(clonePoints[i].slopeTo(tmpPoints[j]) == clonePoints[i].slopeTo(tmpPoints[j+1])){
                    // j和j+1对op的斜率一样
                    // 处理j和j+1点，谁是起点，谁是终点

                    // 最开始，startPoint == null
                    if(startPoint == null){
                        if(clonePoints[i].compareTo(tmpPoints[j]) > 0){
                            // points[i] > tmpPoints[j] 表明 j点y坐标更小
                            endPoint = clonePoints[i];
                            startPoint = tmpPoints[j];
                        }else{
                            // 否则 i点y坐标更小 （不存在相同点）
                            endPoint = tmpPoints[j];
                            startPoint = clonePoints[i];
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
                        if(count >= 2 && clonePoints[i].compareTo(startPoint)==0){
                            StackLineSeg.push(new LineSegment(startPoint,endPoint));
                            nums++;
                        }

                        count = 0;
                        startPoint = null;
                        endPoint = null;
                    }

                }else{
                    // j和j+1对op的斜率不一样
                    if(count >= 2 && clonePoints[i].compareTo(startPoint)==0){
                        StackLineSeg.push(new LineSegment(startPoint,endPoint));
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
        Iterator<LineSegment> v = StackLineSeg.iterator(); // 测试样例有重复调用segments()必须保证每次调用返回值一样
        int i = 0;
        while(v.hasNext()){
            LineSeg[i++] = v.next();
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
