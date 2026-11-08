import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public class Benchmarks {
    private static final String ALPHABET = "abcd";
    private static final long SEED = 20261108L;
    private static final int RUNS = 5;
    private static volatile Object lastResult;

    private static String randomString(Random random, int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return result.toString();
    }

    private static double averageTimeMs(Supplier<?> operation) {
        for (int i = 0; i < 15; i++) {
            lastResult = operation.get();
        }

        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            Object result = operation.get();
            long end = System.nanoTime();
            lastResult = result;
            total += end - start;
        }
        return total / (RUNS * 1_000_000.0);
    }

    private static String row(String algorithm, String scenario, int size, int patternLength,
                              double time, long operations, long hashChecks, long collisions, int output) {
        return String.format(Locale.US, "%s,%s,%d,%d,%.6f,%d,%d,%d,%d",
                algorithm, scenario, size, patternLength, time, operations, hashChecks, collisions, output);
    }

    private static void addMatchingResults(List<String> rows, String text, String pattern,
                                           int size, String scenario) {
        SearchResult naive = NaiveMatcher.find(text, pattern);
        SearchResult kmp = KMPMatcher.find(text, pattern);
        SearchResult rabinKarp = RabinKarpMatcher.find(text, pattern);
        if (!naive.positions.equals(kmp.positions) || !naive.positions.equals(rabinKarp.positions)) {
            throw new AssertionError("Search results differ for " + scenario + ", size " + size);
        }

        double naiveTime = averageTimeMs(() -> NaiveMatcher.find(text, pattern));
        double kmpTime = averageTimeMs(() -> KMPMatcher.find(text, pattern));
        double rkTime = averageTimeMs(() -> RabinKarpMatcher.find(text, pattern));
        rows.add(row("Naive", scenario, size, pattern.length(), naiveTime,
                naive.comparisons, naive.hashChecks, naive.collisions, naive.positions.size()));
        rows.add(row("KMP", scenario, size, pattern.length(), kmpTime,
                kmp.comparisons, kmp.hashChecks, kmp.collisions, kmp.positions.size()));
        rows.add(row("Rabin-Karp", scenario, size, pattern.length(), rkTime,
                rabinKarp.comparisons, rabinKarp.hashChecks, rabinKarp.collisions,
                rabinKarp.positions.size()));
    }

    public static void main(String[] args) throws IOException {
        Random random = new Random(SEED);
        List<String> rows = new ArrayList<>();
        rows.add("algorithm,scenario,input_size,pattern_length,avg_time_ms,operations,hash_checks,collisions,output");

        for (int size : new int[]{100, 500, 1000}) {
            String first = randomString(random, size);
            String second = randomString(random, size);
            double lcsTime = averageTimeMs(() -> LCS.length(first, second));
            double editTime = averageTimeMs(() -> EditDistance.distance(first, second));
            rows.add(row("LCS", "random", size, 0, lcsTime, (long) size * size, 0, 0,
                    LCS.length(first, second)));
            rows.add(row("Edit Distance", "random", size, 0, editTime, (long) size * size, 0, 0,
                    EditDistance.distance(first, second)));
        }

        for (int size : new int[]{10000, 50000, 100000}) {
            String text = randomString(random, size);
            String present = text.substring(size / 2, size / 2 + 24);
            String absent = "z".repeat(24);
            addMatchingResults(rows, text, present, size, "present");
            addMatchingResults(rows, text, absent, size, "absent");
        }

        for (int size : new int[]{1000, 5000, 10000}) {
            String text = randomString(random, size);
            int[] suffixArray = SuffixArray.buildSimple(text);
            Kasai.Result result = Kasai.buildLcp(text, suffixArray);
            double time = averageTimeMs(() -> Kasai.buildLcp(text, suffixArray));
            rows.add(row("Kasai", "random", size, 0, time, result.comparisons, 0, 0, result.lcp.length));
        }

        Files.createDirectories(Path.of("results"));
        Files.write(Path.of("results", "results.csv"), rows);
        for (String line : rows) {
            System.out.println(line);
        }
        System.out.println("Saved results/results.csv");
    }
}
