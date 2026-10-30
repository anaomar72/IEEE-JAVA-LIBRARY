import java.util.*;
import java.io.*;
import java.math.*;
import java.util.function.*;

/** Disjoint Set Union (Union-Find) with path compression and union by size. */
class DSU {
    private final int[] parent;
    private final int[] size;
    private int components;

    DSU(int n) {
        parent = new int[n];
        size = new int[n];
        components = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    // Returns true if two components were joined; false if already connected.
    boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) return false;

        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }

        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        components--;

        return true;
    }

    boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    int componentSize(int x) {
        return size[find(x)];
    }

    int componentsCount() {
        return components;
    }
}


// ==========================================
// ADDITIONAL ALGORITHMS FROM EXTENDED LIBRARY
// Each class is package-private for single-folder compilation.
// ==========================================

// SOURCE: 02-Data-Structures/CollectionsToolkit.java

/**
 * Tier 1 | array, list, map, set, stack, queue, deque and ordered collections
 * Use: standard Java containers
 * Time: O(1) average hash/deque; O(log n) trees/heaps. Space: O(n).
 * Edge cases: empty pop/peek and duplicate keys
 * Pitfalls: Prefer ArrayDeque to legacy Stack; comparator overflow
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class CollectionsToolkit {

    static List<Integer> asList(int[] a) {
        List<Integer> r = new ArrayList<>(a.length);

        for (int x : a) {
            r.add(x);
        }

        return r;
    }

    static Map<Integer, Integer> frequencies(int[] a) {
        Map<Integer, Integer> m = new HashMap<>();

        for (int x : a) {
            m.merge(x, 1, Integer::sum);
        }

        return m;
    }

    static Set<Integer> unique(int[] a) {
        Set<Integer> s = new HashSet<>();

        for (int x : a) {
            s.add(x);
        }

        return s;
    }

    static int[] minHeapOrder(int[] a) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int x : a) {
            pq.add(x);
        }

        int[] r = new int[a.length];

        for (int i = 0; i < r.length; i++) {
            r[i] = pq.remove();
        }

        return r;
    }

    static PriorityQueue<Integer> maxHeap() {
        return new PriorityQueue<>(Comparator.reverseOrder());
    }

    static Deque<Integer> stack() {
        return new ArrayDeque<>(); // push/pop
    }

    static Deque<Integer> queue() {
        return new ArrayDeque<>(); // addLast/removeFirst
    }

    static LinkedList<Integer> linkedList() {
        return new LinkedList<>();
    }

    static TreeSet<Integer> orderedSet() {
        return new TreeSet<>(); // floor, ceiling
    }

    static TreeMap<Integer, Integer> orderedMap() {
        return new TreeMap<>(); // floorKey, ceilingKey
    }
}


// SOURCE: 02-Data-Structures/DisjointSetUnion.java

/**
 * Tier 1 | DSU with path compression and union by size
 * Use: connectivity / Kruskal
 * Time: amortized O(alpha(n)). Space: O(n).
 * Edge cases: zero vertices; duplicate unions
 * Pitfalls: Check union return value for Kruskal
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class DisjointSetUnion {

    final int[] parent;
    final int[] size;
    int components;

    DisjointSetUnion(int n) {
        if (n < 0) throw new IllegalArgumentException();

        parent = new int[n];
        size = new int[n];
        components = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    int find(int x) {
        while (x != parent[x]) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }

        return x;
    }

    boolean union(int a, int b) {
        a = find(a);
        b = find(b);

        if (a == b) return false;

        if (size[a] < size[b]) {
            int t = a;
            a = b;
            b = t;
        }

        parent[b] = a;
        size[a] += size[b];
        components--;

        return true;
    }

    boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    int size(int x) {
        return size[find(x)];
    }
}


// SOURCE: 02-Data-Structures/FenwickTree.java

/**
 * Tier 2 | Fenwick tree of long sums
 * Use: point updates, prefix/range queries
 * Time: O(log n) per operation. Space: O(n).
 * Edge cases: empty tree supported; indices are 0 based externally
 * Pitfalls: Mixing 0-based and 1-based indexing
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class FenwickTree {

    final long[] bit;
    final int n;

    FenwickTree(int n) {
        this.n = n;
        bit = new long[n + 1];
    }

    void add(int index, long delta) {
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException();
        }

        for (int i = index + 1; i <= n; i += i & -i) {
            bit[i] += delta;
        }
    }

    long prefixSum(int endExclusive) {
        if (endExclusive < 0 || endExclusive > n) {
            throw new IndexOutOfBoundsException();
        }

        long sum = 0;

        for (int i = endExclusive; i > 0; i -= i & -i) {
            sum += bit[i];
        }

        return sum;
    }

    long sum(int left, int rightExclusive) {
        return prefixSum(rightExclusive) - prefixSum(left);
    }
}


// SOURCE: 02-Data-Structures/MonotonicStructures.java

/**
 * Tier 2 | monotonic stack and deque
 * Use: next greater / sliding min
 * Time: O(n). Space: O(n).
 * Edge cases: duplicate handling affects strictness
 * Pitfalls: Discard expired indices before window query
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class MonotonicStructures {

    static int[] nextGreaterRight(int[] a) {
        int n = a.length;
        int[] ans = new int[n];

        Arrays.fill(ans, -1);

        Deque<Integer> st = new ArrayDeque<>();

        for (int i = n - 1; i >= 0; i--) {

            while (!st.isEmpty() && a[st.peek()] <= a[i]) {
                st.pop();
            }

            if (!st.isEmpty()) {
                ans[i] = st.peek();
            }

            st.push(i);
        }

        return ans;
    }

    static int[] slidingMinimum(int[] a, int k) {
        if (k <= 0 || k > a.length) {
            throw new IllegalArgumentException();
        }

        int[] result = new int[a.length - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();

        for (int i = 0; i < a.length; i++) {

            while (!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.removeFirst();
            }

            while (!dq.isEmpty() && a[dq.peekLast()] >= a[i]) {
                dq.removeLast();
            }

            dq.addLast(i);

            if (i >= k - 1) {
                result[i - k + 1] = a[dq.peekFirst()];
            }
        }

        return result;
    }
}


// SOURCE: 02-Data-Structures/SegmentTree.java

/**
 * Tier 2 | iterative segment tree for range sums
 * Use: point assignment and range sum
 * Time: O(log n) update/query, O(n) build. Space: O(n).
 * Edge cases: empty tree and [l,r) indexing
 * Pitfalls: Using sum tree where min/max operation is required
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class SegmentTree {

    final int n;
    final int size;
    final long[] tree;

    SegmentTree(long[] a) {
        n = a.length;

        int s = 1;

        while (s < n) {
            s <<= 1;
        }

        size = s;
        tree = new long[2 * size];

        for (int i = 0; i < n; i++) {
            tree[size + i] = a[i];
        }

        for (int i = size - 1; i > 0; i--) {
            tree[i] = tree[i << 1] + tree[i << 1 | 1];
        }
    }

    void set(int i, long v) {
        if (i < 0 || i >= n) {
            throw new IndexOutOfBoundsException();
        }

        int p = i + size;
        tree[p] = v;

        for (p >>= 1; p > 0; p >>= 1) {
            tree[p] = tree[p << 1] + tree[p << 1 | 1];
        }
    }

    long sum(int l, int r) {
        if (l < 0 || r < l || r > n) {
            throw new IndexOutOfBoundsException();
        }

        long ans = 0;

        for (l += size, r += size; l < r; l >>= 1, r >>= 1) {

            if ((l & 1) == 1) {
                ans += tree[l++];
            }

            if ((r & 1) == 1) {
                ans += tree[--r];
            }
        }

        return ans;
    }
}


// SOURCE: 02-Data-Structures/Trie.java

/**
 * Tier 2 | lowercase English prefix trie
 * Use: dictionary/prefix matching
 * Time: O(word length). Space: O(total characters * alphabet) worst case.
 * Edge cases: only a-z; empty word is allowed
 * Pitfalls: Passing uppercase or Unicode without normalizing
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class Trie {

    static final class Node {
        int[] next = new int[26];
        boolean end;

        Node() {
            Arrays.fill(next, -1);
        }
    }

    final ArrayList<Node> nodes = new ArrayList<>();

    Trie() {
        nodes.add(new Node());
    }

    private int id(char c) {
        int x = c - 'a';

        if (x < 0 || x >= 26) {
            throw new IllegalArgumentException("a-z only");
        }

        return x;
    }

    void add(String s) {
        int at = 0;

        for (char c : s.toCharArray()) {
            int x = id(c);

            if (nodes.get(at).next[x] < 0) {
                nodes.get(at).next[x] = nodes.size();
                nodes.add(new Node());
            }

            at = nodes.get(at).next[x];
        }

        nodes.get(at).end = true;
    }

    boolean contains(String s) {
        int at = walk(s);
        return at >= 0 && nodes.get(at).end;
    }

    boolean startsWith(String s) {
        return walk(s) >= 0;
    }

    int walk(String s) {
        int at = 0;

        for (char c : s.toCharArray()) {
            at = nodes.get(at).next[id(c)];

            if (at < 0) {
                return -1;
            }
        }

        return at;
    }
}


// SOURCE: 09-Advanced-Algorithms/CoordinateCompression.java

/**
 * Tier 2 | map arbitrary ints to dense ranks
 * Use: large coordinate values
 * Time: O(n log n). Space: O(n).
 * Edge cases: duplicates share rank
 * Pitfalls: Do not assume compressed coordinates preserve original distance
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class CoordinateCompression {

    static int[] compress(int[] a) {
        int[] sorted = a.clone();
        Arrays.sort(sorted);

        int unique = 0;

        for (int v : sorted) {
            if (unique == 0 || sorted[unique - 1] != v) {
                sorted[unique++] = v;
            }
        }

        int[] ranks = new int[a.length];

        for (int i = 0; i < a.length; i++) {
            ranks[i] = Arrays.binarySearch(sorted, 0, unique, a[i]);
        }

        return ranks;
    }

    static int[] sortedUnique(int[] a) {
        int[] x = a.clone();
        Arrays.sort(x);

        int n = 0;

        for (int v : x) {
            if (n == 0 || x[n - 1] != v) {
                x[n++] = v;
            }
        }

        return Arrays.copyOf(x, n);
    }
}