# kdtree
![logo_5](img/logo_5.png)
* 100/100

![100/100](img/kdtree100.png)

PointSET.java比较简单，难点主要是KdTree.java中的nearest()

Kd-Tree还是非常有趣的。

有几个坑需要注意：

* 不要import xxxx.*
* KdTree中的插入函数，如果有相同点，则不能插入。并且左边是小于，右边是大于等于。
* 仔细阅读RectHV和Point2D的API
* 关于nearest() 感觉官方给的specification有点问题：
  * "if the closest point discovered so far is closer than the distance between the query point and the rectangle corresponding to a node, there is no need to explore that node (or its subtrees). That is, search a node only only if it might contain a point that is closer than the best one found so far. The effectiveness of the pruning rule depends on quickly finding a nearby point. To do this, organize the recursive method so that when there are two possible subtrees to go down, you always choose the subtree that is on the same side of the splitting line as the query point as the first subtree to explore—the closest point found while exploring the first subtree may enable pruning of the second subtree.  "
  * 原文说closer than, 也就是 minPoint.distanceTo(queryPoint) < node.rect.distanceTo(queryPoint)时，不需要遍历当前点及其子树，也就是>=时，才需要遍历，但是这样只能98/100，经过测试，改成>后，居然100/100通过了
  * 我发起的讨论：[About nearest() | Something wrong with specification?](https://www.coursera.org/learn/algorithms-part1/programming/wuF0a/kd-trees/discussions/threads/p_QSYn3kEe6QMRKEnYEYdQ)