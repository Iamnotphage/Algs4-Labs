# Algs4-Labs
My personal solutions for algs4 labs💀

- [Algs4-Labs](#algs4-labs)
- [Websites](#websites)
- [hello](#hello)
- [percolation](#percolation)
- [queues](#queues)
- [collinear](#collinear)

# Websites

官网和coursera上有很多资料，也有实验的在线评测平台。

课程官网：https://algs4.cs.princeton.edu/home/

Coursera官网：https://www.coursera.org/

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

* 85/100 
  
有点小问题，后续再整改

# queues

* 100/100

好像没啥大问题

# collinear

* 100/100

自己写了两个版本都100/100通过线上平台，大同小异；

一个自定义链表实现，一个Stack实现（这个简单一点, 更推荐）

个人遇到的问题：

* 构造器传入的points不能改变，否则部分样例不通过
* 检查points中元素是否重复或者null，如果调用slopeTo()必须保证两者都不是null，否则会错误抛出异常
* 使用ArrayList，LinkedList等java.util封装的数据结构会出现超时（但是自定义的链表和Stack不会）
* FastCollinearPoints类里面，去掉重复线段的处理要非常小心，详见代码
* 样例有多次调用同一个函数，请保证每次调用返回结果一样(eg.不能直接把栈弹光，后续再调用结果就不一样了)

