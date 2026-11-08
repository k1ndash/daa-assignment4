import java.util.ArrayList;
import java.util.List;

public class RabinKarpMatcher {
    private static final long BASE = 256;
    private static final long MOD = 1_000_000_007;

    public static SearchResult find(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            return SearchResult.emptyPattern(n);
        }
        if (m > n) {
            return new SearchResult(new ArrayList<>(), 0, 0, 0);
        }

        long highestPower = 1;
        for (int i = 1; i < m; i++) {
            highestPower = (highestPower * BASE) % MOD;
        }

        long patternHash = 0;
        long windowHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
            windowHash = (windowHash * BASE + text.charAt(i)) % MOD;
        }

        List<Integer> positions = new ArrayList<>();
        long comparisons = 0;
        long hashChecks = 0;
        long collisions = 0;

        for (int i = 0; i <= n - m; i++) {
            hashChecks++;
            if (patternHash == windowHash) {
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
                } else {
                    collisions++;
                }
            }

            if (i < n - m) {
                windowHash = (windowHash - text.charAt(i) * highestPower % MOD + MOD) % MOD;
                windowHash = (windowHash * BASE + text.charAt(i + m)) % MOD;
            }
        }
        return new SearchResult(positions, comparisons, hashChecks, collisions);
    }
}
