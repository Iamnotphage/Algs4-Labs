// Boyer-Moore
// Textbook page 505; algorithm 5.7
public class BoyerMoore {
    private final int R;
    private int[] right;
    private String pat;

    public BoyerMoore(String pat) {
        this.R = 256;
        this.pat = pat;
        right = new int[R];

        for (int c = 0; c < R; c++) {
            right[c] = -1;
        }

        for (int j = 0; j < pat.length(); j++) {
            right[pat.charAt(j)] = j;
        }
    }

    public int search(String txt) {
        int m = pat.length();
        int n = txt.length();
        int skip;

        for(int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for(int j = m - 1; j >= 0; j--) {
                if(pat.charAt(j) != txt.charAt(i + j)) {
                    skip = Math.max(1, j - right[txt.charAt(i + j)]);
                    break;
                }
            }
            if (skip == 0) return i;// Found return the first number;
        }
        return -1; // Not Found returns -1;
    }



    public int searchPos(String txt, int n) {
        int count = 0;
        char[] tx = txt.toCharArray();
        for (int i = 0; i < n; i++) {
            if (tx[i] == ' ') count++;
        }
        return count;
    }

}
