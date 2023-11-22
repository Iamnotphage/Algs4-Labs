

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

/****
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
***/

public class KdTree {

    // 2d-tree Node
    private class Node{
        public Point2D point; // point value
        public Node left; // left child
        public Node right; // right child

        // 每个结点里面有一个用于draw和debug的矩形，矩形的范围就是该结点的画线的范围。
        // 由此确定线段的边界条件
        public RectHV rect; // every node has a rect indicates its boundary

        public boolean isVertical;

        public Node(Point2D p, Node parent){
            // parent 参数用来限制矩形rect的范围
            point = p;
            left = null;
            right = null;

            if(parent == null){
                rect = new RectHV(0,0,1,1);
                isVertical = true;
            }else{
                isVertical = !parent.isVertical;
                // amazing
                if(isVertical){
                    // 垂直线段，根据父亲结点需要判断矩形上下限
                    // 父亲一定是水平线段，父亲的y值固定
                    // 如果当前结点是父亲结点的左结点，说明当前结点的y小于父亲的y
                    // 如果当前结点是父亲结点的右结点，说明当前结点的y大于父亲的y
                    // y小于父亲，则矩形的y的上限就是父亲结点的y
                    if(p.y() < parent.point.y()){
                        rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.point.y());
                    }else{
                        rect = new RectHV(parent.rect.xmin(), parent.point.y(), parent.rect.xmax(), parent.rect.ymax());
                    }
                }else{
                    // 水平线段，根据父亲结点需要判断矩形上下限
                    // 父亲一定是垂直线段，父亲的x值固定
                    // 如果当前结点是父亲结点的左结点，说明当前结点的x小于父亲的x
                    // 如果当前结点是父亲结点的右结点，说明当前结点的x大于父亲的x
                    // x小于父亲，则矩形的x的上限就是父亲结点的x
                    if(p.x() < parent.point.x()){
                        rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.point.x(), parent.rect.ymax());
                    }else{
                        rect = new RectHV(parent.point.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                    }
                }

            }
        }

    }

    private Node root;

    private int size;

    public KdTree(){// construct an empty set of points
        root = null;
        size = 0;
    }
    public boolean isEmpty(){// is the set empty?
        return size == 0;
    }
    public int size(){// number of points in the set
        return size;
    }

    public void insert(Point2D p){// add the point to the set (if it is not already in the set)
        if(p == null){
            throw new IllegalArgumentException();
        }


        if(root == null){
            root = new Node(p, null);
            size++;
            return;
        }

        Node current = root;

        while(true){

            if(p.equals(current.point)){
                // (if it is not already in the set)
                return;
            }

            if(current.isVertical){
                // x-coordinate
                if(p.x() < current.point.x()){
                    if(current.left == null){
                        current.left = new Node(p, current);
                        break;
                    }else{
                        current = current.left;
                    }
                }else{
                    if(current.right == null){
                        current.right = new Node(p, current);
                        break;
                    }else{
                        current = current.right;
                    }
                }
            }else{
                // y-coordinate
                if(p.y() < current.point.y()){
                    if(current.left == null){
                        current.left = new Node(p, current);
                        break;
                    }else{
                        current = current.left;
                    }
                }else{
                    if(current.right == null){
                        current.right = new Node(p, current);
                        break;
                    }else{
                        current = current.right;
                    }
                }
            }
        }
        size++;
    }

    // Search
    public boolean contains(Point2D p){// does the set contain point p?
        if(p == null){
            throw new IllegalArgumentException();
        }
        // search
        Node current = root;
        while(current != null){
            if(current.isVertical){
                if(current.point.equals(p)){
                    return true;
                }else if(p.x() < current.point.x()){
                    current = current.left;
                }else{
                    current = current.right;
                }
            }else{
                if(current.point.equals(p)){
                    return true;
                }else if(p.y() < current.point.y()){
                    current = current.left;
                }else{
                    current = current.right;
                }
            }
        }
        return false;
    }

    // DLR draw
    private void DLRDraw(Node node){
        if(node == null){
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw(); // draw a point

        StdDraw.setPenRadius();
        if(node.isVertical){
            // vertical line
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }else{
            // horizontal line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }

        DLRDraw(node.left);
        DLRDraw(node.right);
    }
    public void draw(){// draw all points to standard draw
        DLRDraw(root);
    }


    /*****
    Range search. To find all points contained in a given query rectangle,
    start at the root and recursively search for points in both subtrees using
    the following pruning rule: if the query rectangle does not intersect the
    rectangle corresponding to a node, there is no need to explore that node
    (or its subtrees). A subtree is searched only if it might contain a point
    contained in the query rectangle.
    *****/

    // 实际上是遍历所有结点，但是需要利用子节点的rect来判断是否和给定rect相交，不相交则不需要继续遍历
    // 先看当前结点是否包含于给定rect，再根据子结点来判断，所以是DLR顺序
    private void DLRRange(Node node, RectHV rect, ArrayList<Point2D> res){
        if(node == null){
            return;
        }
        // 查看当前结点是否包含于给定rect
        if(rect.contains(node.point)){
            res.add(node.point);
        }
        // 查看左右孩子的rect和给定rect是否相交
        if(node.left != null){
            if(node.left.rect.intersects(rect)){
                DLRRange(node.left, rect, res);
            }
        }
        if(node.right != null){
            if(node.right.rect.intersects(rect)){
                DLRRange(node.right, rect, res);
            }
        }
    }
    public Iterable<Point2D> range(RectHV rect){// all points that are inside the rectangle (or on the boundary)
        if(rect == null){
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> inRectPoints = new ArrayList<>();
        DLRRange(root, rect, inRectPoints);
        return inRectPoints;
    }
    /****
     To find a closest point to a given query point, start at the root and
     recursively search in both subtrees using the following pruning rule:
     if the closest point discovered so far is closer than the distance
     between the query point and the rectangle corresponding to a node,
     there is no need to explore that node (or its subtrees). That is,
     search a node only only if it might contain a point that is closer
     than the best one found so far. The effectiveness of the pruning
     rule depends on quickly finding a nearby point. To do this, organize
     the recursive method so that when there are two possible subtrees to
     go down, you always choose the subtree that is on the same side of
     the splitting line as the query point as the first subtree to explore—the
     closest point found while exploring the first subtree may enable
     pruning of the second subtree.
    * */

    /**
     首先要清楚矩形到查询点的距离的定义：就是查询点到矩形最近的距离，也就是整个矩形里（包括边界）选取一个点，使得与查询点
     距离最近。在2d树里面的点，一个点代表一个矩形范围，这个点必然在这个矩形内部。所以，如果当前最近的点到查询点的距离 比
     查询点到结点的矩形的距离还要小，那就不需要再继续了。
    **/
    private Point2D DLRNearest(Node node, Point2D queryPoint, Point2D minPoint){
        if(node == null) {
            return minPoint;
        }
        if(minPoint == null){
            // 说明是根节点，第一次传进来
            minPoint = node.point;
        }
        double minDistance = minPoint.distanceSquaredTo(queryPoint);
        double newDistance = node.point.distanceSquaredTo(queryPoint);

        // 如果当前最小点的距离 比 当前节点的矩形到查询点的距离 还小，就不需要查找

        if((minDistance > node.rect.distanceSquaredTo(queryPoint))){

            if(minDistance > newDistance){
                minPoint = node.point;
            }

            if( (node.isVertical && (queryPoint.x() < node.point.x())) ||
                    (!node.isVertical && (queryPoint.y() < node.point.y()))){
                // 查询点在当前结点左边
                minPoint = DLRNearest(node.left, queryPoint, minPoint);

                minPoint = DLRNearest(node.right, queryPoint, minPoint);

            }else{

                minPoint = DLRNearest(node.right, queryPoint, minPoint);

                minPoint = DLRNearest(node.left, queryPoint, minPoint);

            }
        }

        return minPoint;
    }

    public Point2D nearest(Point2D p){// a nearest neighbor in the set to point p; null if the set is empty
        if(p == null){
            throw new IllegalArgumentException();
        }
        Point2D minPoint = null;
        if(root != null){
            minPoint = DLRNearest(root, p, minPoint);
        }
        return minPoint;
    }

    public static void main(String[] args){// unit testing of the methods (optional)

        In in = new In("inputtest.txt");

        KdTree kdtree = new KdTree();

        while(!in.isEmpty()){
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x,y);
            kdtree.insert(p);
        }

        kdtree.draw();

        // Point2D ans = kdtree.nearest(new Point2D(0.47, 0.18));
        // input test : 0.4375, 0.75
        Point2D ans = kdtree.nearest(new Point2D(0.875, 0.625));

    }
}
