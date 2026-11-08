import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    public final List<Integer> positions;
    public final long comparisons;
    public final long hashChecks;
    public final long collisions;

    public SearchResult(List<Integer> positions, long comparisons, long hashChecks, long collisions) {
        this.positions = positions;
        this.comparisons = comparisons;
        this.hashChecks = hashChecks;
        this.collisions = collisions;
    }

    public static SearchResult emptyPattern(int textLength) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i <= textLength; i++) {
            positions.add(i);
        }
        return new SearchResult(positions, 0, 0, 0);
    }
}
