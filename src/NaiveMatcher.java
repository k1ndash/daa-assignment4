import java.util.ArrayList;
import java.util.List;

public class NaiveMatcher {
    public static SearchResult find(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            return SearchResult.emptyPattern(n);
        }

        List<Integer> positions = new ArrayList<>();
        long comparisons = 0;

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m) {
                comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                positions.add(i);
            }
        }
        return new SearchResult(positions, comparisons, 0, 0);
    }
}
