public class RandomQuickSort {
    public RandomQuickSort(int[] a){
        sort(a, 0, a.length - 1);
    }

    private void sort(int[] a, int lo, int hi){
        if(hi <= lo)return;
        int j = partition(a, lo, hi);
        sort(a, lo, j - 1); // 中枢j的左边
        sort(a, j+1, hi); // 中枢j的右边
    }

    private int partition(int[] a, int lo, int hi){
        int i = lo, j = hi + 1;
        int v = a[lo]; // v是中枢 v左边的元素应该小，右边的元素应该大
        while(true){
            // i从左到右扫描，直到第一个大于等于v的出现
            while(a[++i] < v){
                if(i == hi){
                    break;
                }
            }
            // j从右到左扫描，直到第一个小于等于v的出现
            while(a[--j] > v){
                if(j == lo){
                    break;
                }
            }
            if(i >= j){ // i >= j表示中枢v的位置找到了，直接lo和j交换即可
                break;
            }
            exch(a, i, j);
        }
        exch(a, lo, j); // swap v and a[j]
        return j;
    }

    private void exch(int[] a, int i, int j){
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

}
