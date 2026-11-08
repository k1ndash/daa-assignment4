public class Kasai {
    public static class Result {
        public final int[] lcp;
        public final long comparisons;

        public Result(int[] lcp, long comparisons) {
            this.lcp = lcp;
            this.comparisons = comparisons;
        }
    }

    public static Result buildLcp(String text, int[] suffixArray) {
        int n = text.length();
        if (suffixArray.length != n) {
            throw new IllegalArgumentException("Suffix array length does not match the text length");
        }

        int[] rank = new int[n];
        boolean[] seen = new boolean[n];
        for (int i = 0; i < n; i++) {
            int index = suffixArray[i];
            if (index < 0 || index >= n || seen[index]) {
                throw new IllegalArgumentException("Invalid suffix array");
            }
            seen[index] = true;
            rank[index] = i;
        }

        int[] lcp = new int[n];
        int common = 0;
        long comparisons = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] == 0) {
                common = 0;
                continue;
            }
            int j = suffixArray[rank[i] - 1];
            while (i + common < n && j + common < n) {
                comparisons++;
                if (text.charAt(i + common) != text.charAt(j + common)) {
                    break;
                }
                common++;
            }
            lcp[rank[i]] = common;
            if (common > 0) {
                common--;
            }
        }
        return new Result(lcp, comparisons);
    }
}
