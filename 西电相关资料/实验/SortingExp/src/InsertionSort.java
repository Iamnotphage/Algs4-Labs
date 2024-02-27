public class InsertionSort {
//    public InsertionSort(int[] a){
//        int N = a.length;
//        for(int i = 1 ; i < N; i++){
//            for(int j = i; j > 0; j--){
//                if(a[j] < a[j-1]){
//                    exch(a, j, j-1);
//                }
//            }
//        }
//    }

    public void exch(int[] a, int i, int j){
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
    public InsertionSort(int[] a){
        int N = a.length;
        for(int i = 1; i < N; i++){
            for(int j = i; j > 0; j--){
                if(a[j] < a[j-1]){
                    exch(a, j, j - 1);
                }
            }
        }
    }

}
