# Algs4-Labs
My personal solutions for algs4 labs💀

- [Algs4-Labs](#algs4-labs)
- [Websites](#websites)
- [hello](#hello)
- [percolation](#percolation)
- [queues](#queues)
- [collinear](#collinear)
- [8puzzle](#8puzzle)
- [kdtree](#kdtree)
- [wordnet](#wordnet)

# Websites

官网和coursera上有很多资料，也有实验的在线评测平台。

课程官网：https://algs4.cs.princeton.edu/home/

Coursera官网：https://www.coursera.org/

部分我觉得有意思的课后练习题：[Click Here](/interestingAssignments/assignments.md)

# hello

主要配置环境，测试algs4.jar是否能正常编译运行

说实话，这个是最搞的。

Windows平台下，最简单的方法如下：(摸索了很久)

[点击这个网址](https://lift.cs.princeton.edu/java/windows/)

然后下载`lift-java-installer.exe`这个一键安装的内容包含：

* IntelliJ
* Git
* 还有一些配置文件


然后运行，它会覆盖classpath还有原先的JAVA path。如果你喜欢挑战自我可以试试手动配置

(安装JDK，添加JAVAPATH，下载algs4.jar，添加到classpath，再安装git，再把algs4.jar添加到git的path，然后用IDE导入algs4.jar包)。不推荐💀

上述内容是可选的，安装了Git或者IntelliJ可以不用勾选。

然后下载algs4.jar包

接下来在IDE里面导入包 (Bing一下，你就知道)

能够在Git Bash中使用

`javac-algs4`

`java-algs4`

这两个命令，就证明成功

# percolation
![logo_1](img/logo_1.png)

* 100/100 

![100/100](img/percolation100.png)
  
有点小问题，后续再整改 (2023-10-11 19:33更新)

核心就是Union Find的使用。

对于计算概率p，80分通过基本上没问题。

但是在线平台有一些细节需要注意：

* Backwash现象
* 多次调用PercolationStats的函数确保结果一致

Backwash现象主要围绕isFull()的判断，如果按照幻灯片里面的两个虚拟节点来判断是否percolate，确实很方便，但是如果判断isFull,就会出现Backwash（回流）的现象。

主要表现如下图：

![backwash](img/backwash.jpg)

所以最好是弄两个uf对象，一个包含两个虚拟结点，一个只包含一个top的虚拟结点。

# queues
![logo_2](img/logo_2.png)

* 100/100

![100/100](img/deque100.png)

核心就是操作链表。

好像没啥大问题。

# collinear
![logo_3](img/logo_3.png)
* 100/100

![100/100](img/collinear100.png)

核心是排序。

自己写了两个版本都100/100通过线上平台，大同小异；

一个自定义链表实现，一个Stack实现（这个简单一点, 更推荐）

个人遇到的问题：

* 构造器传入的points不能改变，否则部分样例不通过
* 检查points中元素是否重复或者null，如果调用slopeTo()必须保证两者都不是null，否则会错误抛出异常
* 使用ArrayList，LinkedList等java.util封装的数据结构会出现超时（但是自定义的链表和Stack不会）
* FastCollinearPoints类里面，去掉重复线段的处理要非常小心，详见代码
* 样例有多次调用同一个函数，请保证每次调用返回结果一样(eg.不能直接把栈弹光，后续再调用结果就不一样了)

# 8puzzle
![logo_4](img/logo_4.png)
* 100/100

![100/100](img/8puzzle100.png)

核心是优先队列，需要用到A*算法，但是给出的文档也有提及思路，所以似乎并没有那么硬核的使用。

关于算法的思路如下：

![8puzzle_1](img/8puzzle_1.jpg)

![8puzzle_2](img/8puzzle_2.jpg)

个人遇到的问题：

* Board类的isGoal()和equals()一定要极其认真写
* Board类的twin()一定要理解到位
* 写完Board类一定要进行测试
* Solver类中用到优先队列的插入时，一定要检查邻居是否重复，否则会堆溢出。
* Solver类的solution()从上图的草稿看，不够明显是如何获取的。但是一定要从最终的goal的棋盘回溯到最开始的棋盘，(goal.pre)类似这样。为什么？如果某个结点的邻居有两个棋盘的priority一样小，只是利用每次删除的minNode来加入到solution里，会导致solution的结点顺序错误，临近的两结点不是邻居。所以用pre来保证solution输出的序列一定是相邻的。（eg. 123 076 548）
* 关于内存，Solver类中的MinPQ最好是放在构造函数内部，这样执行完之后会自动清除，从而不会导致内存超出要求。

`To solve the puzzle from a given search node on the priority queue, the total number of moves we need to make (including those already made) is at least its priority, using either the Hamming or Manhattan priority function. Why? Consequently, when the goal board is dequeued, we have discovered not only a sequence of moves from the initial board to the goal board, but one that makes the fewest moves. (Challenge for the mathematically inclined: prove this fact.)`

`Proof: 假设优先队列的最小结点是第N个结点(distance, move, priority)，它的邻居只可能有两种情况（从距离上看）：比第N个结点距离大1 或 比第N个结点距离小1。而作为邻居结点，它们的move只会递增1。当删除第N个结点时，优先队列的最小结点将会是第N个结点邻居的距离较小的那个 (distance-1, move+1, priority) 对于任意N成立，所以最终distance=0时，move=priority，Q.E.D.`

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

# wordnet
![logo_6](img/logo_6.png)
* 100/100

![wordnet100](img/wordnet100.png)

Part II的难度直接飙升一个台阶。

**Tips**:

* synset和hypernym关系要捋清楚。
* WordNet的数据结构一定要构思清楚，id和synset是多对多的映射关系，一个id可能有多个noun，一个noun可能有多个id (数据结构详见下文)。
* 读取synset.txt和hypernyms.txt文件的方法：用algs4.In里面的in.readLine()逐行读取，用String类的split对字段分割
* 如何判断是否是DAG（即是否有环）：用ToplogicalSort或者DirectedCycle类的方法`hasCycle()`。
* TopologicalSort如何实现：有向图DFS，结点压栈得到reversePostOrder。反过来了就是Topological Sort的序列。
* 如何寻找公共祖先：对于两个结点v和w，各自用BFS, v先BFS，过程中记录遍历的结点x对v的距离；w再BFS，每次将队列首元素出列时，查看是否已经有对v距离的记录，如果有，更新minDistance。总之就是两次BFS的过程。
* 拓展：有向图的强连通分量求法：Kosaraju-Sharir算法


```Java
private final SAP sap;  
private final ArrayList<String> synList; // 同位词集添加到这里 索引就是id  
  
private final HashMap<String, List<Integer>> synsetToIdMap; // 一个id对应多个名词, 所以反过来一个名词对应部分id
```