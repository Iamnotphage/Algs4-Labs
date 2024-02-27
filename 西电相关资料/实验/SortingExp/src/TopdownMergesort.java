public class TopdownMergesort {
    private int[] aux;

    public TopdownMergesort(int[] a) {
        aux = new int[a.length];
        sort(a, 0, a.length - 1);
    }


    private void sort(int[] a, int lo, int hi){
        if(hi <= lo)return;
        int mid = lo + (hi - lo)/2; // prevent overflow
        sort(a, lo, mid); // 左半边
        sort(a, mid+1, hi); // 右半边
        merge(a, lo, mid, hi); // 合并已经排序的两个半边
    }

    private void merge(int[] a, int lo, int mid, int hi) {
        int i = lo, j = mid + 1;
        // i指向左半边的起始元素， j指向右半边的起始元素
        for(int k = lo; k <= hi; k++){
            aux[k] = a[k]; // 辅助数组用于存操作ij，长度从lo到hi
        }

        // merge
        for(int k = lo; k <= hi; k++){
            // i 和 j 之间的关系
            if(i > mid){
                // i遍历完了 左半边全部取完
                a[k] = aux[j++];
            }else if(j > hi){
                // j遍历完了 右半边全部取完
                a[k] = aux[i++];
            }else if(aux[j] < aux[i]){
                a[k] = aux[j++];
            }else{
                // aux[j] >= aux[i] 保证稳定性的关键一招
                // 如果上一个else if里面是 aux[i] < aux[j]
                // 那么当 aux[i] = aux[j] 时，就会执行 aux[i] >= aux[j]
                // 的内容，那么就会导致后出现的数字跑到前面去，破坏稳定性
                a[k] = aux[i++];
            }
        }

    }
}
