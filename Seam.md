# seam
![logo_7](img/logo_7.png)

难度飙升。需要非常注意优化问题：

Tips:

- Don’t use an explicit `EdgeWeightedDigraph`. Instead, execute the topological sort algorithm directly on the pixels.
    
- When finding a seam, call `energy()` at most once per pixel. For example, you can save the energies in a local variable `energy[][]` and access the information directly from the 2D array (instead of recomputing from scratch).
    
- Avoid redundant calls to the `get()` method in `Picture`. For example, to access the red, green, and blue components of a pixel, call `get()` only once (and not three times).
    
- The order in which you traverse the pixels (row-major order vs. column-major order) can make a big difference.
    
- Creating `Color` objects can be a bottleneck. Each call to the `get()` method in `Picture` creates a new `Color` object. You can avoid this overhead by using the `getRGB()` method in `Picture`, which returns the color, [encoded as a 32-bit `int`](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Picture.html). The companion `setRGB()` method sets the color of a given pixel using a 32-bit `int` to encode the color.
    
- Don’t explicitly transpose the `Picture` or `int[][]` until you need to do so. For example, if you perform a sequence of 50 consecutive horizontal seam removals, you should need only two transposes (not 100).
    
- Consider using `System.arraycopy()` to shift elements within an array.
    
- Reuse the energy array and shift array elements to plug the holes left from the seam that was just removed. You will need to recalculate the energies for the pixels along the seam that was just removed, but no other energies will change.