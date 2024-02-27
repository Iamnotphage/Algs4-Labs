public class Dijkstra3WayQuickSort {
    public Dijkstra3WayQuickSort(int[] a){
        sort(a, 0, a.length - 1);
    }

    private void sort(int[] a, int lo, int hi){
        if(hi <= lo)return;

        int lt = lo, i = lo, gt = hi;
        int v = a[lo];
        // lt左边都是小于V，gt右边都是大于V，[lt,gt] 都是等于v
        while(i <= gt){
            if(a[i] < v){
                exch(a, lt++, i++);
            }else if(a[i] > v){
                exch(a, i, gt--);
            }else{
                i++;
            }
        }

        sort(a, lo, lt - 1); // 处理左半边
        sort(a, gt + 1, hi); // 处理右半边
    }


    private void exch(int[] a, int i, int j){
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
