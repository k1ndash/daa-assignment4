import java.util.ArrayList;
import java.util.List;

public class KMPMatcher {
    public static SearchResult find(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            return SearchResult.emptyPattern(n);
        }
        if (m > n) {
            return new SearchResult(new ArrayList<>(), 0, 0, 0);
        }

        long comparisons = 0;
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
            comparisons++;
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len > 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }

        List<Integer> positions = new ArrayList<>();
        i = 0;
        int j = 0;
        while (i < n) {
            comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    positions.add(i - j);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return new SearchResult(positions, comparisons, 0, 0);
    }
}
