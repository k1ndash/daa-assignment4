import java.util.Arrays;

public class SuffixArray {
    public static int[] buildSimple(String text) {
        Integer[] indexes = new Integer[text.length()];
        for (int i = 0; i < text.length(); i++) {
            indexes[i] = i;
        }

        Arrays.sort(indexes, (first, second) -> {
            int i = first;
            int j = second;
            while (i < text.length() && j < text.length()) {
                if (text.charAt(i) != text.charAt(j)) {
                    return Character.compare(text.charAt(i), text.charAt(j));
                }
                i++;
                j++;
            }
            return Integer.compare(text.length() - first, text.length() - second);
        });

        int[] suffixArray = new int[text.length()];
        for (int i = 0; i < indexes.length; i++) {
            suffixArray[i] = indexes[i];
        }
        return suffixArray;
    }
}
