import java.util.Arrays;
import java.util.List;

public class AlgorithmTests {
    private static int testsPassed = 0;

    private static void check(String name, boolean passed) {
        if (!passed) {
            throw new AssertionError("Test failed: " + name);
        }
        testsPassed++;
    }

    private static void matchingTest(String name, String text, String pattern, List<Integer> expected) {
        List<Integer> naive = NaiveMatcher.find(text, pattern).positions;
        List<Integer> kmp = KMPMatcher.find(text, pattern).positions;
        List<Integer> rabinKarp = RabinKarpMatcher.find(text, pattern).positions;
        check(name, naive.equals(expected) && naive.equals(kmp) && naive.equals(rabinKarp));
    }

    public static void main(String[] args) {
        check("LCS identical", LCS.length("abcde", "abcde") == 5);
        check("LCS different", LCS.length("abc", "xyz") == 0);
        check("LCS empty", LCS.length("", "abc") == 0);
        check("LCS standard example", LCS.length("AGGTAB", "GXTXAYB") == 4);

        check("Edit Distance identical", EditDistance.distance("hello", "hello") == 0);
        check("Edit Distance empty", EditDistance.distance("", "hello") == 5);
        check("Edit Distance empty reverse", EditDistance.distance("hello", "") == 5);
        check("Edit Distance substitution", EditDistance.distance("cat", "cut") == 1);
        check("Edit Distance insertion", EditDistance.distance("cat", "cart") == 1);
        check("Edit Distance deletion", EditDistance.distance("cart", "cat") == 1);
        check("Edit Distance standard example", EditDistance.distance("kitten", "sitting") == 3);

        matchingTest("Matching at beginning", "abcxx", "abc", List.of(0));
        matchingTest("Matching in middle", "xxabcxx", "abc", List.of(2));
        matchingTest("Matching at end", "xxabc", "abc", List.of(2));
        matchingTest("Multiple and overlapping matches", "aaaaa", "aa", List.of(0, 1, 2, 3));
        matchingTest("Pattern absent", "hello world", "xyz", List.of());
        matchingTest("Pattern longer than text", "abc", "abcdef", List.of());
        matchingTest("Empty pattern", "abc", "", List.of(0, 1, 2, 3));
        matchingTest("Empty text and empty pattern", "", "", List.of(0));
        matchingTest("Empty text", "", "a", List.of());

        int[] bananaSa = {5, 3, 1, 0, 4, 2};
        int[] bananaLcp = {0, 1, 3, 0, 0, 2};
        check("Banana suffix array", Arrays.equals(SuffixArray.buildSimple("banana"), bananaSa));
        check("Kasai banana", Arrays.equals(Kasai.buildLcp("banana", bananaSa).lcp, bananaLcp));
        check("Kasai repeated characters", Arrays.equals(
                Kasai.buildLcp("aaaa", new int[]{3, 2, 1, 0}).lcp, new int[]{0, 1, 2, 3}));
        check("Kasai empty", Kasai.buildLcp("", new int[]{}).lcp.length == 0);
        check("Kasai one character", Arrays.equals(
                Kasai.buildLcp("a", new int[]{0}).lcp, new int[]{0}));

        System.out.println("All " + testsPassed + " tests passed.");
    }
}
