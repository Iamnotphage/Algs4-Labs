- [2.3.3](#233)

# 2.3.3
**Number of largest element exchanges for quicksort**

假设有数组a,其中元素 $a_k > a_{k-1} (k \in Z)$ 并且 $max > a_k$

对于交换元素，只有两种可能：(基于algs4的快速排序的代码)

* i和j指针交换
* pivot和j指针交换

考虑 N = 4 的情况:

可以构造 $a_2,max,a_1,a_3$

$a_2$作为pivot，接下来将要交换$max$和$a1$

得到：$a_2,a_1,max,a_3$

然后把pivot放在正确的位置:

$a_1,a_2,max,a_3$

接下来要对 $max,a_3$ 再进行划分

最终得到：$a_1,a_2,a_3,max$

可见，$max$交换了2次。

把N = 4作为 N = 4 + 2 = 6的子数列

必然有N = 6 数列划分后的结果：

After partition: $a_{-1},a_0,a_2,max,a_1,a_3$

容易得到 N = 6 数列在划分前的序列：

Before partition: $a_0,max,a_2,a_{-1},a_1,a_3$

也就是说，N = 6的数列，将要对max元素进行 1 + 2 = 3次交换，其中2次是N = 4子数列交换的次数。

考虑N = 6 + 2 = 8的数列，N = 6作为其右子数列，容易得到划分前：

Before partition: $a_{-2},max,a_0,a_{-3},a_2,a_{-1},a_1,a_3$

也就是说，将要对max元素进行 1 + 1 + 2 = 4 次交换

由数学归纳法可以发现，N 长度序列的数组，只要满足

$a_{k+1},max,a_{k+3},a_k, ......$

即可每次partition都交换一次max

考虑到奇数长度

最终得到结果：N/2取下整，也就是 floor(N/2)

