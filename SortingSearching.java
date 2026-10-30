import java.util.*;
import java.io.*;
import java.math.*;
import java.util.function.*;

/** Sorting and searching helpers. Class is package-private for easy reuse. */
class SortingSearching {

    // Sorts an integer array in ascending order.
    static void sort(int[] a) {
        Arrays.sort(a);
    }

    // Returns an index containing target, or -1 if not found.
    // The array must already be sorted in ascending order.
    static int binarySearch(int[] a, int target) {
        int left = 0, right = a.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (a[mid] == target) {
                return mid;
            }

            if (a[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    // First index i where a[i] >= target. Returns a.length if none.
    // The array must be sorted in ascending order.
    static int lowerBound(int[] a, int target) {
        int left = 0, right = a.length;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (a[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    // First index i where a[i] > target. Returns a.length if none.
    // The array must be sorted in ascending order.
    static int upperBound(int[] a, int target) {
        int left = 0, right = a.length;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (a[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    // Inclusive range sum [left, right], after O(n) preprocessing.
    // prefix must have been built from the array.
    static long[] prefixSums(int[] a) {
        long[] prefix = new long[a.length + 1];

        for (int i = 0; i < a.length; i++) {
            prefix[i + 1] = prefix[i] + a[i];
        }

        return prefix;
    }

    static long rangeSum(long[] prefix, int left, int right) {
        return prefix[right + 1] - prefix[left];
    }

    // Pair-sum check for an array sorted in ascending order.
    static boolean hasPairSum(int[] a, long target) {
        int left = 0, right = a.length - 1;

        while (left < right) {
            long sum = (long) a[left] + a[right];

            if (sum == target) {
                return true;
            }

            if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return false;
    }
}


// ==========================================
// ADDITIONAL ALGORITHMS FROM EXTENDED LIBRARY
// Each class is package-private for single-folder compilation.
// ==========================================


// SOURCE: 01-Input-Output/BufferedTokenReader.java
/**
 * Tier 1 | BufferedReader StringTokenizer
 * Use: moderate token input
 * Time: O(total chars). Space: O(line size).
 * Edge cases: next() returns null at EOF
 * Pitfalls: nextLineRaw after token reads starts at next physical line
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class BufferedTokenReader {

    private final BufferedReader br;
    private StringTokenizer st;

    BufferedTokenReader(Reader r) {
        br = new BufferedReader(r);
    }

    String next() throws IOException {
        while (st == null || !st.hasMoreTokens()) {
            String line = br.readLine();

            if (line == null) {
                return null;
            }

            st = new StringTokenizer(line);
        }

        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    long nextLong() throws IOException {
        return Long.parseLong(next());
    }

    double nextDouble() throws IOException {
        return Double.parseDouble(next());
    }

    String nextLineRaw() throws IOException {
        st = null;
        return br.readLine();
    }
}


// SOURCE: 01-Input-Output/FastOutput.java
/**
 * Tier 1 | BufferedWriter and StringBuilder output
 * Use: many output lines
 * Time: O(output chars). Space: O(buffered chars).
 * Edge cases: flush before program exit
 * Pitfalls: Avoid string concatenation inside large loops
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class FastOutput implements AutoCloseable {

    private final BufferedWriter writer;
    private final StringBuilder sb = new StringBuilder();

    FastOutput(OutputStream out) {
        writer = new BufferedWriter(new OutputStreamWriter(out));
    }

    FastOutput print(Object x) throws IOException {
        sb.append(x);

        if (sb.length() > 1 << 16) {
            flush();
        }

        return this;
    }

    FastOutput println(Object x) throws IOException {
        return print(x).print("\n");
    }

    void flush() throws IOException {
        writer.write(sb.toString());
        sb.setLength(0);
        writer.flush();
    }

    public void close() throws IOException {
        flush();
        writer.close();
    }
}


// SOURCE: 01-Input-Output/FastScanner.java
/**
 * Tier 1 | BufferedInputStream scanner
 * Use: large token input
 * Time: O(total bytes). Space: O(buffer + token).
 * Edge cases: EOF returns null from next(); nextInt/Long throw on missing token
 * Pitfalls: Do not mix with BufferedReader on same System.in
 * Example: Input: 2 9 10 -> nextInt(): 2, 9, 10
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class FastScanner {

    private final InputStream in;
    private final byte[] buffer = new byte[1 << 16];
    private int ptr = 0, len = 0;

    FastScanner(InputStream in) {
        this.in = in;
    }

    private int read() throws IOException {
        if (ptr >= len) {
            len = in.read(buffer);
            ptr = 0;

            if (len <= 0) {
                return -1;
            }
        }

        return buffer[ptr++];
    }

    String next() throws IOException {
        StringBuilder s = new StringBuilder();
        int c;

        do {
            c = read();
        } while (c <= 32 && c != -1);

        if (c == -1) {
            return null;
        }

        while (c > 32) {
            s.append((char) c);
            c = read();
        }

        return s.toString();
    }

    int nextInt() throws IOException {
        String s = next();

        if (s == null) {
            throw new EOFException();
        }

        return Integer.parseInt(s);
    }

    long nextLong() throws IOException {
        String s = next();

        if (s == null) {
            throw new EOFException();
        }

        return Long.parseLong(s);
    }

    double nextDouble() throws IOException {
        String s = next();

        if (s == null) {
            throw new EOFException();
        }

        return Double.parseDouble(s);
    }

    int[] ints(int n) throws IOException {
        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = nextInt();
        }

        return a;
    }

    long[] longs(int n) throws IOException {
        long[] a = new long[n];

        for (int i = 0; i < n; i++) {
            a[i] = nextLong();
        }

        return a;
    }
}


// SOURCE: 01-Input-Output/InputPatterns.java
/**
 * Tier 1 | test-case, EOF, arrays, and delimiter parsing
 * Use: input parsing
 * Time: O(input length). Space: O(input stored).
 * Edge cases: raw lines may be blank; delimiters may repeat
 * Pitfalls: Do not assume every input begins with a test count
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class InputPatterns {

    static long[] readLongArray(FastScanner fs, int n) throws IOException {
        return fs.longs(n);
    }

    static long sumUntilEOF(FastScanner fs) throws IOException {
        long sum = 0;
        String token;

        while ((token = fs.next()) != null) {
            sum += Long.parseLong(token);
        }

        return sum;
    }

    static int[] parseCommaSeparatedInts(String line) {
        if (line.trim().isEmpty()) {
            return new int[0];
        }

        String[] parts = line.trim().split("\\s*,\\s*");
        int[] a = new int[parts.length];

        for (int i = 0; i < a.length; i++) {
            a[i] = Integer.parseInt(parts[i]);
        }

        return a;
    }

    static String[] splitWhitespace(String line) {
        line = line.trim();

        return line.isEmpty() ? new String[0] : line.split("\\s+");
    }
}


// SOURCE: 03-Searching/BinarySearchOnAnswer.java
/**
 * Tier 2 | minimize feasible nonnegative capacity
 * Use: monotonic feasibility predicates
 * Time: O(log range * predicate). Space: O(1) aside from predicate.
 * Edge cases: impossible states and overflow bounds
 * Pitfalls: Do not binary search a nonmonotone predicate
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class BinarySearchOnAnswer {

    static long firstTrue(long low, long high, LongPredicate feasible) {
        // inclusive bounds; -1 if none
        long answer = -1;

        while (low <= high) {
            long mid = low + (high - low) / 2;

            if (feasible.test(mid)) {
                answer = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return answer;
    }

    static long minimumShipCapacity(int[] weights, int days) {
        if (days <= 0) {
            throw new IllegalArgumentException();
        }

        if (weights.length == 0) {
            return 0;
        }

        long lo = 0, hi = 0;

        for (int w : weights) {
            if (w < 0) {
                throw new IllegalArgumentException();
            }

            lo = Math.max(lo, w);
            hi += w;
        }

        return firstTrue(lo, hi, cap -> {
            int used = 1;
            long load = 0;

            for (int w : weights) {
                if (load + w > cap) {
                    used++;
                    load = 0;
                }

                load += w;
            }

            return used <= days;
        });
    }
}


// SOURCE: 03-Searching/BinarySearchToolkit.java
/**
 * Tier 1 | linear, binary, bounds, rotated search
 * Use: sorted arrays and indexed search
 * Time: O(n) linear; O(log n) sorted. Space: O(1).
 * Edge cases: empty arrays, duplicates, rotated ambiguity
 * Pitfalls: mid overflow; binary search needs sorted input
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class BinarySearchToolkit {

    static int linear(int[] a, int x) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == x) {
                return i;
            }
        }

        return -1;
    }

    static int binary(int[] a, int x) {
        int l = 0, r = a.length - 1;

        while (l <= r) {
            int m = l + (r - l) / 2;

            if (a[m] == x) {
                return m;
            }

            if (a[m] < x) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }

        return -1;
    }

    static int lowerBound(int[] a, int x) {
        int l = 0, r = a.length;

        while (l < r) {
            int m = l + (r - l) / 2;

            if (a[m] < x) {
                l = m + 1;
            } else {
                r = m;
            }
        }

        return l;
    }

    static int upperBound(int[] a, int x) {
        int l = 0, r = a.length;

        while (l < r) {
            int m = l + (r - l) / 2;

            if (a[m] <= x) {
                l = m + 1;
            } else {
                r = m;
            }
        }

        return l;
    }

    static int rotatedSearch(int[] a, int x) {
        int l = 0, r = a.length - 1;

        while (l <= r) {
            int m = l + (r - l) / 2;

            if (a[m] == x) {
                return m;
            }

            if (a[l] == a[m] && a[m] == a[r]) {
                l++;
                r--;
                continue;
            }

            if (a[l] <= a[m]) {
                if (a[l] <= x && x < a[m]) {
                    r = m - 1;
                } else {
                    l = m + 1;
                }
            } else {
                if (a[m] < x && x <= a[r]) {
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }
        }

        return -1;
    }
}


// SOURCE: 04-Sorting/IntegerSorts.java
/**
 * Tier 2 | counting sort and signed radix sort
 * Use: large integer arrays
 * Time: O(n+range) count; O(4n) radix. Space: O(n+range) count; O(n) radix.
 * Edge cases: negative numbers and huge range
 * Pitfalls: Counting sort only if range fits memory
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class IntegerSorts {

    static int[] countingSort(int[] a, int min, int max) {
        long range = (long) max - min + 1;

        if (range <= 0 || range > 2_000_000) {
            throw new IllegalArgumentException("range too big");
        }

        int[] counts = new int[(int) range];

        for (int x : a) {
            if (x < min || x > max) {
                throw new IllegalArgumentException();
            }

            counts[x - min]++;
        }

        int[] out = new int[a.length];
        int p = 0;

        for (int i = 0; i < counts.length; i++) {
            while (counts[i]-- > 0) {
                out[p++] = i + min;
            }
        }

        return out;
    }

    static void radixSort(int[] a) {
        int[] tmp = new int[a.length];

        for (int pass = 0; pass < 4; pass++) {
            int[] cnt = new int[256];
            int shift = 8 * pass;

            for (int x : a) {
                cnt[((x ^ Integer.MIN_VALUE) >>> shift) & 255]++;
            }

            int sum = 0;

            for (int i = 0; i < 256; i++) {
                int t = cnt[i];
                cnt[i] = sum;
                sum += t;
            }

            for (int x : a) {
                tmp[cnt[((x ^ Integer.MIN_VALUE) >>> shift) & 255]++] = x;
            }

            System.arraycopy(tmp, 0, a, 0, a.length);
        }
    }
}


// SOURCE: 04-Sorting/MergeSort.java
/**
 * Tier 2 | stable primitive merge sort
 * Use: custom stable sorting
 * Time: O(n log n). Space: O(n).
 * Edge cases: empty input
 * Pitfalls: Merge equal elements from left to retain stability
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class MergeSort {

    static void sort(int[] a) {
        int[] temp = new int[a.length];

        for (int width = 1; width < a.length; width *= 2) {
            for (int left = 0; left < a.length; left += 2 * width) {
                int mid = Math.min(left + width, a.length);
                int right = Math.min(left + 2 * width, a.length);

                int i = left, j = mid, k = left;

                while (i < mid && j < right) {
                    temp[k++] = a[i] <= a[j] ? a[i++] : a[j++];
                }

                while (i < mid) {
                    temp[k++] = a[i++];
                }

                while (j < right) {
                    temp[k++] = a[j++];
                }
            }

            System.arraycopy(temp, 0, a, 0, a.length);

            if (width > a.length / 2) {
                break;
            }
        }
    }
}


// SOURCE: 04-Sorting/QuickSort.java
/**
 * Tier 2 | iterative randomized-pivot quicksort with 3-way partition
 * Use: few extra arrays, duplicates
 * Time: O(n log n) expected; O(n^2) worst. Space: O(n) stack worst.
 * Edge cases: all equal / sorted array
 * Pitfalls: Recursive quicksort risks stack overflow; use Arrays.sort normally
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class QuickSort {

    static void sort(int[] a) {
        if (a.length < 2) {
            return;
        }

        ArrayDeque<int[]> st = new ArrayDeque<>();
        st.push(new int[]{0, a.length - 1});

        Random random = new Random(1);

        while (!st.isEmpty()) {
            int[] interval = st.pop();
            int l = interval[0], r = interval[1];

            if (l >= r) {
                continue;
            }

            int pivot = a[l + random.nextInt(r - l + 1)];
            int lt = l, i = l, gt = r;

            while (i <= gt) {
                if (a[i] < pivot) {
                    swap(a, lt++, i++);
                } else if (a[i] > pivot) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }

            if (l < lt - 1) {
                st.push(new int[]{l, lt - 1});
            }

            if (gt + 1 < r) {
                st.push(new int[]{gt + 1, r});
            }
        }
    }

    static void swap(int[] a, int x, int y) {
        int v = a[x];
        a[x] = a[y];
        a[y] = v;
    }
}


// SOURCE: 04-Sorting/SortingToolkit.java
/**
 * Tier 1 | built-in and custom sorting / pairs
 * Use: general-purpose sorting
 * Time: O(n log n) comparison. Space: O(n) worst case.
 * Edge cases: nulls and stable sorting needs
 * Pitfalls: Never subtract in comparator: overflow
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class SortingToolkit {

    static void sortNumbers(int[] a) {
        Arrays.sort(a);
    }

    static void sortList(List<Integer> a) {
        Collections.sort(a);
    }

    static final class Pair {
        final int first, second;

        Pair(int a, int b) {
            first = a;
            second = b;
        }
    }

    static void sortPairs(Pair[] a) {
        Arrays.sort(a, (x, y) -> {
            int c = Integer.compare(x.first, y.first);
            return c != 0 ? c : Integer.compare(x.second, y.second);
        });
    }

    static void descending(Integer[] a) {
        Arrays.sort(a, Comparator.reverseOrder());
    }
}


// SOURCE: 05-Arrays-Strings/ArrayPatterns.java
/**
 * Tier 1 | two pointers, sliding window, prefix, difference, frequency, Kadane
 * Use: frequent array patterns
 * Time: O(n) each. Space: O(n) typical.
 * Edge cases: negative values invalidate classic nonnegative sliding sum
 * Pitfalls: Kadane with all negatives must not return zero
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class ArrayPatterns {

    static boolean hasPairSumSorted(int[] a, long target) {
        int i = 0, j = a.length - 1;

        while (i < j) {
            long s = (long) a[i] + a[j];

            if (s == target) {
                return true;
            }

            if (s < target) {
                i++;
            } else {
                j--;
            }
        }

        return false;
    }

    static int longestAtMostKDistinct(int[] a, int k) {
        if (k < 0) {
            return 0;
        }

        Map<Integer, Integer> count = new HashMap<>();
        int left = 0, best = 0;

        for (int right = 0; right < a.length; right++) {
            count.merge(a[right], 1, Integer::sum);

            while (count.size() > k) {
                int x = a[left++];
                int v = count.get(x) - 1;

                if (v == 0) {
                    count.remove(x);
                } else {
                    count.put(x, v);
                }
            }

            best = Math.max(best, right - left + 1);
        }

        return best;
    }

    static long[] prefix(long[] a) {
        long[] p = new long[a.length + 1];

        for (int i = 0; i < a.length; i++) {
            p[i + 1] = p[i] + a[i];
        }

        return p;
    }

    static long[] rangeAdds(int n, int[][] updates) {
        long[] diff = new long[n + 1];

        for (int[] u : updates) {
            int l = u[0], r = u[1];

            if (l < 0 || l > r || r >= n) {
                throw new IllegalArgumentException();
            }

            diff[l] += u[2];
            diff[r + 1] -= u[2];
        }

        long[] a = new long[n];
        long cur = 0;

        for (int i = 0; i < n; i++) {
            cur += diff[i];
            a[i] = cur;
        }

        return a;
    }

    static long maxSubarray(int[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException("nonempty array required");
        }

        long best = a[0], cur = a[0];

        for (int i = 1; i < a.length; i++) {
            cur = Math.max((long) a[i], cur + a[i]);
            best = Math.max(best, cur);
        }

        return best;
    }

    static Map<Integer, Integer> frequency(int[] a) {
        Map<Integer, Integer> m = new HashMap<>();

        for (int x : a) {
            m.merge(x, 1, Integer::sum);
        }

        return m;
    }
}


// SOURCE: 05-Arrays-Strings/KMP.java
/**
 * Tier 2 | Knuth-Morris-Pratt exact string matching
 * Use: repeated pattern matching
 * Time: O(n+m). Space: O(m) plus matches.
 * Edge cases: empty pattern returns all boundary positions
 * Pitfalls: Incorrect prefix fallback index
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class KMP {

    static int[] prefixFunction(String s) {
        int[] pi = new int[s.length()];

        for (int i = 1; i < s.length(); i++) {
            int j = pi[i - 1];

            while (j > 0 && s.charAt(i) != s.charAt(j)) {
                j = pi[j - 1];
            }

            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }

            pi[i] = j;
        }

        return pi;
    }

    static List<Integer> findAll(String text, String pattern) {
        List<Integer> ans = new ArrayList<>();

        if (pattern.isEmpty()) {
            for (int i = 0; i <= text.length(); i++) {
                ans.add(i);
            }

            return ans;
        }

        int[] pi = prefixFunction(pattern);
        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }

            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }

            if (j == pattern.length()) {
                ans.add(i - j + 1);
                j = pi[j - 1];
            }
        }

        return ans;
    }
}


// SOURCE: 05-Arrays-Strings/RollingHash.java
/**
 * Tier 3 | double polynomial rolling hash
 * Use: many substring comparisons
 * Time: O(n) setup; O(1) hash queries. Space: O(n).
 * Edge cases: hash collisions still possible
 * Pitfalls: Use exact comparison if collision-free correctness is necessary
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class RollingHash {

    static final long M1 = 1_000_000_007L;
    static final long M2 = 1_000_000_009L;
    static final long BASE = 911382323L;

    final long[] h1, h2, p1, p2;

    RollingHash(String s) {
        int n = s.length();

        h1 = new long[n + 1];
        h2 = new long[n + 1];
        p1 = new long[n + 1];
        p2 = new long[n + 1];

        p1[0] = p2[0] = 1;

        for (int i = 0; i < n; i++) {
            long v = s.charAt(i) + 1L;

            h1[i + 1] = (h1[i] * BASE + v) % M1;
            h2[i + 1] = (h2[i] * BASE + v) % M2;

            p1[i + 1] = p1[i] * BASE % M1;
            p2[i + 1] = p2[i] * BASE % M2;
        }
    }

    long hash(int l, int r) {
        long x = (h1[r] - h1[l] * p1[r - l] % M1 + M1) % M1;
        long y = (h2[r] - h2[l] * p2[r - l] % M2 + M2) % M2;

        return (x << 32) | y;
    }
}


// SOURCE: 05-Arrays-Strings/StringPatterns.java
/**
 * Tier 1 | StringBuilder, reversal, letter frequencies, palindrome
 * Use: string basics
 * Time: O(n). Space: O(n).
 * Edge cases: UTF-16 code units vs Unicode codepoints
 * Pitfalls: Avoid repeated + in loops
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class StringPatterns {

    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    static boolean palindrome(String s) {
        for (int l = 0, r = s.length() - 1; l < r; l++, r--) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
        }

        return true;
    }

    static int[] lowercaseFrequency(String s) {
        int[] freq = new int[26];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c < 'a' || c > 'z') {
                throw new IllegalArgumentException();
            }

            freq[c - 'a']++;
        }

        return freq;
    }

    static String joinInts(int[] a) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < a.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }

            sb.append(a[i]);
        }

        return sb.toString();
    }
}


// SOURCE: 05-Arrays-Strings/ZAlgorithm.java
/**
 * Tier 2 | Z function and pattern match
 * Use: string prefix occurrences
 * Time: O(n+m). Space: O(n+m).
 * Edge cases: empty string/pattern
 * Pitfalls: Concatenation delimiter may collide; here use integer array sentinel
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class ZAlgorithm {

    static int[] z(int[] s) {
        int[] z = new int[s.length];

        for (int i = 1, l = 0, r = 0; i < s.length; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }

            while (i + z[i] < s.length && s[z[i]] == s[i + z[i]]) {
                z[i]++;
            }

            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }

        return z;
    }

    static List<Integer> findAll(String text, String pattern) {
        List<Integer> ans = new ArrayList<>();

        if (pattern.isEmpty()) {
            for (int i = 0; i <= text.length(); i++) {
                ans.add(i);
            }

            return ans;
        }

        int m = pattern.length();
        int[] a = new int[m + 1 + text.length()];

        for (int i = 0; i < m; i++) {
            a[i] = pattern.charAt(i);
        }

        a[m] = -1;

        for (int i = 0; i < text.length(); i++) {
            a[m + 1 + i] = text.charAt(i);
        }

        int[] z = z(a);

        for (int i = m + 1; i < a.length; i++) {
            if (z[i] >= m) {
                ans.add(i - m - 1);
            }
        }

        return ans;
    }
}