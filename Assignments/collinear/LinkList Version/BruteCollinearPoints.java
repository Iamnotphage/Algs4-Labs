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

        first = null;
        nums = 0;

        Arrays.sort(clonePoints); // 按照 compareTo()的规则排序: 先y后x

//        System.out.println("After Sort: ");
//        for(int i=0;i<points.length;i++){
//            System.out.println(points[i].toString());
//        }

        // really sucks loop
        for(int p=0;p < clonePoints.length-3;p++){
            for(int q=p+1;q < clonePoints.length-2;q++){
                for(int r=q+1;r < clonePoints.length-1;r++){
                    for(int s=r+1;s < clonePoints.length;s++){

                        double slope1 = clonePoints[p].slopeTo(clonePoints[q]);
                        double slope2 = clonePoints[q].slopeTo(clonePoints[r]);
                        double slope3 = clonePoints[r].slopeTo(clonePoints[s]);

                        if( slope1 == slope2 && slope2 == slope3 ){
                            nums++;

                            // 头插法
                            Node tmpNode = new Node();
                            tmpNode.value = new LineSegment(clonePoints[p],clonePoints[s]);
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
        Node p = first; // 测试样例有重复调用segments()必须保证每次调用返回值一样
        for(int i=0;i<nums;i++){
            LineSeg[i] = p.value;
            p = p.next;
        }
        return LineSeg;
    }
}
