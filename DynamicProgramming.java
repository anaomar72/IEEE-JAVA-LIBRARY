import java.util.*;
import java.io.*;
import java.math.*;
import java.util.function.*;

/** Basic dynamic-programming patterns. */
class DynamicProgramming {

    // O(n) time, O(1) space. long overflows for sufficiently large n.
    static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be nonnegative");
        }
        if (n <= 1) {
            return n;
        }

        long previous = 0, current = 1;

        for (int i = 2; i <= n; i++) {
            long next = previous + current;
            previous = current;
            current = next;
        }

        return current;
    }

    // 0/1 knapsack: each item can be used at most once.
    // Assumes nonnegative weights and capacity. O(n * capacity) time.
    static long knapsack01(int[] weight, int[] value, int capacity) {
        if (weight.length != value.length) {
            throw new IllegalArgumentException("Arrays must have equal lengths");
        }

        long[] dp = new long[capacity + 1];

        for (int i = 0; i < weight.length; i++) {
            if (weight[i] < 0) {
                throw new IllegalArgumentException("Weights must be nonnegative");
            }

            for (int c = capacity; c >= weight[i]; c--) {
                dp[c] = Math.max(dp[c], dp[c - weight[i]] + value[i]);
            }
        }

        return dp[capacity];
    }

    // Number of ways to make sum using unlimited copies of each coin.
    // Assumes positive coin values and nonnegative target.
    static long coinChangeWays(int[] coins, int target) {
        long[] dp = new long[target + 1];
        dp[0] = 1;

        for (int coin : coins) {
            if (coin <= 0) {
                throw new IllegalArgumentException("Coins must be positive");
            }

            for (int sum = coin; sum <= target; sum++) {
                dp[sum] += dp[sum - coin];
            }
        }

        return dp[target];
    }
}


// ==========================================
// ADDITIONAL ALGORITHMS FROM EXTENDED LIBRARY
// Each class is package-private for single-folder compilation.
// ==========================================

// SOURCE: 07-Dynamic-Programming/BasicDP.java

/**
 * Tier 1 | Fibonacci, climbing stairs, memoization, tabulation
 * Use: simple recurrence problems
 * Time: O(n). Space: O(1) iterative; O(n) memo.
 * Edge cases: fib(93) overflows long; stairs(92) overflows long
 * Pitfalls: Base cases and overflow
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class BasicDP {

    static long fibonacci(int n) {
        if (n < 0 || n > 92) {
            throw new IllegalArgumentException("0..92");
        }

        long a = 0, b = 1;

        for (int i = 0; i < n; i++) {
            long t = a + b;
            a = b;
            b = t;
        }

        return a;
    }

    static long climbingStairs(int n) {
        if (n < 0 || n > 91) {
            throw new IllegalArgumentException("0..91");
        }

        long a = 1, b = 1;

        for (int i = 1; i <= n; i++) {
            long t = a + b;
            a = b;
            b = t;
        }

        return a;
    }

    static long fibonacciMemo(int n) {
        if (n < 0 || n > 92) {
            throw new IllegalArgumentException();
        }

        long[] memo = new long[n + 1];
        Arrays.fill(memo, -1);

        return fib(n, memo);
    }

    private static long fib(int n, long[] memo) {
        if (n < 2) {
            return n;
        }

        if (memo[n] != -1) {
            return memo[n];
        }

        return memo[n] = fib(n - 1, memo) + fib(n - 2, memo);
    }
}


// SOURCE: 07-Dynamic-Programming/BitmaskDP.java

/**
 * Tier 3 | minimum Hamiltonian path over all vertices
 * Use: small n traveling salesman path
 * Time: O(n^2 * 2^n). Space: O(n * 2^n).
 * Edge cases: n <= 20; missing edges use INF
 * Pitfalls: Exponential memory and shifts at n >=31
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class BitmaskDP {

    static final long INF = Long.MAX_VALUE / 4;

    static long minHamiltonianPath(long[][] w) {
        int n = w.length;

        if (n == 0) {
            return 0;
        }

        if (n > 20) {
            throw new IllegalArgumentException("n>20");
        }

        for (long[] row : w) {
            if (row.length != n) {
                throw new IllegalArgumentException();
            }
        }

        int states = 1 << n;
        long[] dp = new long[states * n];

        Arrays.fill(dp, INF);

        for (int i = 0; i < n; i++) {
            dp[(1 << i) * n + i] = 0;
        }

        for (int mask = 1; mask < states; mask++) {
            for (int v = 0; v < n; v++) {
                long cur = dp[mask * n + v];

                if (cur >= INF) {
                    continue;
                }

                for (int u = 0; u < n; u++) {
                    if ((mask & (1 << u)) == 0 && w[v][u] < INF) {
                        int idx = (mask | 1 << u) * n + u;
                        dp[idx] = Math.min(dp[idx], cur + w[v][u]);
                    }
                }
            }
        }

        long ans = INF;

        for (int v = 0; v < n; v++) {
            ans = Math.min(ans, dp[(states - 1) * n + v]);
        }

        return ans;
    }
}


// SOURCE: 07-Dynamic-Programming/CoinChangeDP.java

/**
 * Tier 1 | min coins and combinations with unlimited coins
 * Use: coin change
 * Time: O(n*amount). Space: O(amount).
 * Edge cases: amount 0; unreachable -1; zero/negative coin invalid
 * Pitfalls: loop direction defines combination vs permutation
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class CoinChangeDP {

    static int minCoins(int[] coins, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }

        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for (int c : coins) {
            if (c <= 0) {
                throw new IllegalArgumentException();
            }

            for (int i = c; i <= amount; i++) {
                dp[i] = Math.min(dp[i], dp[i - c] + 1);
            }
        }

        return dp[amount] > amount ? -1 : dp[amount];
    }

    static long combinations(int[] coins, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }

        long[] dp = new long[amount + 1];
        dp[0] = 1;

        for (int c : coins) {
            if (c <= 0) {
                throw new IllegalArgumentException();
            }

            for (int i = c; i <= amount; i++) {
                dp[i] = Math.addExact(dp[i], dp[i - c]);
            }
        }

        return dp[amount];
    }
}


// SOURCE: 07-Dynamic-Programming/KnapsackDP.java

/**
 * Tier 1 | 0/1 and unbounded knapsack
 * Use: maximize value under integer capacity
 * Time: O(n*capacity). Space: O(capacity).
 * Edge cases: zero weight items; negative values; capacity explosion
 * Pitfalls: 0/1 loop descending; unbounded ascending
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class KnapsackDP {

    static long zeroOne(int[] weights, int[] values, int cap) {
        check(weights, values, cap);

        long[] dp = new long[cap + 1];

        for (int i = 0; i < weights.length; i++) {
            if (weights[i] == 0) {
                if (values[i] > 0) {
                    for (int c = 0; c <= cap; c++) {
                        dp[c] += values[i];
                    }
                }
                continue;
            }

            for (int c = cap; c >= weights[i]; c--) {
                dp[c] = Math.max(dp[c], dp[c - weights[i]] + values[i]);
            }
        }

        return dp[cap];
    }

    static long unbounded(int[] weights, int[] values, int cap) {
        check(weights, values, cap);

        long[] dp = new long[cap + 1];

        for (int i = 0; i < weights.length; i++) {
            if (weights[i] == 0) {
                if (values[i] > 0) {
                    throw new IllegalArgumentException("unbounded optimum");
                }
                continue;
            }

            for (int c = weights[i]; c <= cap; c++) {
                dp[c] = Math.max(dp[c], dp[c - weights[i]] + values[i]);
            }
        }

        return dp[cap];
    }

    private static void check(int[] w, int[] v, int cap) {
        if (cap < 0 || w.length != v.length) {
            throw new IllegalArgumentException();
        }

        for (int x : w) {
            if (x < 0) {
                throw new IllegalArgumentException();
            }
        }
    }
}


// SOURCE: 07-Dynamic-Programming/SequenceDP.java

/**
 * Tier 2 | LCS length and LIS length
 * Use: subsequence problems
 * Time: O(nm) LCS; O(n log n) LIS. Space: O(min(n,m)) LCS; O(n) LIS.
 * Edge cases: empty strings and duplicates
 * Pitfalls: LIS uses lower bound for strictly increasing
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class SequenceDP {

    static int lcs(String a, String b) {
        if (b.length() > a.length()) {
            String t = a;
            a = b;
            b = t;
        }

        int[] dp = new int[b.length() + 1];

        for (int i = 1; i <= a.length(); i++) {
            int prev = 0;

            for (int j = 1; j <= b.length(); j++) {
                int tmp = dp[j];

                dp[j] = a.charAt(i - 1) == b.charAt(j - 1)
                        ? prev + 1
                        : Math.max(dp[j], dp[j - 1]);

                prev = tmp;
            }
        }

        return dp[b.length()];
    }

    static int lis(int[] a) {
        int[] tails = new int[a.length];
        int size = 0;

        for (int x : a) {
            int l = 0, r = size;

            while (l < r) {
                int m = (l + r) >>> 1;

                if (tails[m] < x) {
                    l = m + 1;
                } else {
                    r = m;
                }
            }

            tails[l] = x;

            if (l == size) {
                size++;
            }
        }

        return size;
    }
}


// SOURCE: 07-Dynamic-Programming/SubsetAndGridDP.java

/**
 * Tier 2 | subset sum and count grid paths
 * Use: boolean subset sums and obstacle grids
 * Time: O(n*target) / O(rows*cols). Space: O(target) / O(cols).
 * Edge cases: zero target; empty grids; negative weights unsupported
 * Pitfalls: descending subset sum to avoid item reuse
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class SubsetAndGridDP {

    static boolean subsetSum(int[] a, int target) {
        if (target < 0) {
            throw new IllegalArgumentException();
        }

        boolean[] dp = new boolean[target + 1];
        dp[0] = true;

        for (int x : a) {
            if (x < 0) {
                throw new IllegalArgumentException();
            }

            for (int s = target; s >= x; s--) {
                dp[s] |= dp[s - x];
            }
        }

        return dp[target];
    }

    static long gridPaths(boolean[][] blocked) {
        if (blocked.length == 0) {
            return 0;
        }

        int c = blocked[0].length;

        if (c == 0) {
            return 0;
        }

        long[] dp = new long[c];
        dp[0] = blocked[0][0] ? 0 : 1;

        for (int i = 0; i < blocked.length; i++) {
            if (blocked[i].length != c) {
                throw new IllegalArgumentException();
            }

            for (int j = 0; j < c; j++) {
                if (blocked[i][j]) {
                    dp[j] = 0;
                } else if (j > 0) {
                    dp[j] = Math.addExact(dp[j], dp[j - 1]);
                }
            }
        }

        return dp[c - 1];
    }
}


// SOURCE: 09-Advanced-Algorithms/Backtracking.java

/**
 * Tier 2 | iterative subset enumeration and N-Queens
 * Use: small search spaces
 * Time: O(2^n) subsets / exponential queens. Space: O(n) apart from output.
 * Edge cases: n must be small
 * Pitfalls: Never use exhaustive search for n=1e5
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class Backtracking {

    static List<List<Integer>> subsets(int[] a) {
        if (a.length > 20) {
            throw new IllegalArgumentException("limit 20");
        }

        List<List<Integer>> out = new ArrayList<>();

        for (int mask = 0; mask < (1 << a.length); mask++) {
            List<Integer> s = new ArrayList<>();

            for (int i = 0; i < a.length; i++) {
                if ((mask & (1 << i)) != 0) {
                    s.add(a[i]);
                }
            }

            out.add(s);
        }

        return out;
    }

    static long countNQueens(int n) {
        if (n < 0 || n > 15) {
            throw new IllegalArgumentException();
        }

        return dfs(0, n, 0, 0, 0);
    }

    private static long dfs(int row, int n, int cols, int left, int right) {
        if (row == n) {
            return 1;
        }

        int available = ((1 << n) - 1) & ~(cols | left | right);
        long total = 0;

        while (available != 0) {
            int bit = available & -available;
            available -= bit;

            total += dfs(
                    row + 1,
                    n,
                    cols | bit,
                    (left | bit) << 1,
                    (right | bit) >>> 1
            );
        }

        return total;
    }
}


// SOURCE: 09-Advanced-Algorithms/DivideAndConquer.java

/**
 * Tier 2 | inversion counting using merge technique
 * Use: count inversions efficiently
 * Time: O(n log n). Space: O(n).
 * Edge cases: duplicates are not inversions
 * Pitfalls: Avoid int counter: inversions up to n(n-1)/2
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class DivideAndConquer {

    static long inversions(int[] a) {
        int[] copy = a.clone();
        return count(copy, new int[a.length], 0, a.length);
    }

    private static long count(int[] a, int[] b, int l, int r) {
        if (r - l <= 1) {
            return 0;
        }

        int m = l + (r - l) / 2;
        long inv = count(a, b, l, m) + count(a, b, m, r);

        int i = l, j = m, k = l;

        while (i < m && j < r) {
            if (a[i] <= a[j]) {
                b[k++] = a[i++];
            } else {
                b[k++] = a[j++];
                inv += m - i;
            }
        }

        while (i < m) {
            b[k++] = a[i++];
        }

        while (j < r) {
            b[k++] = a[j++];
        }

        System.arraycopy(b, l, a, l, r - l);

        return inv;
    }
}


// SOURCE: 09-Advanced-Algorithms/GreedyPatterns.java

/**
 * Tier 1 | interval scheduling, fractional selection
 * Use: locally optimal choices with proof
 * Time: O(n log n). Space: O(n).
 * Edge cases: ties and negative endpoints
 * Pitfalls: Greedy does not apply unless exchange argument holds
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class GreedyPatterns {

    static int maxNonoverlapping(int[][] intervals) {
        int[][] a = intervals.clone();

        Arrays.sort(a, (x, y) -> Integer.compare(x[1], y[1]));

        int count = 0;
        long end = Long.MIN_VALUE;

        for (int[] seg : a) {
            if (seg[0] >= end) {
                count++;
                end = seg[1];
            }
        }

        return count;
    }

    static long minTotalMergeCost(int[] lengths) {
        PriorityQueue<Long> pq = new PriorityQueue<>();

        for (int x : lengths) {
            if (x < 0) {
                throw new IllegalArgumentException();
            }

            pq.add((long) x);
        }

        long cost = 0;

        while (pq.size() > 1) {
            long a = pq.poll();
            long b = pq.poll();
            long sum = Math.addExact(a, b);

            cost = Math.addExact(cost, sum);
            pq.add(sum);
        }

        return cost;
    }
}


// SOURCE: 09-Advanced-Algorithms/MeetInTheMiddle.java

/**
 * Tier 3 | count subsets with sum at most limit
 * Use: subset sum where n about 40
 * Time: O(n*2^(n/2) + 2^(n/2) log 2^(n/2)). Space: O(2^(n/2)).
 * Edge cases: negative values allowed; limit overflow
 * Pitfalls: Sorting both halves not necessary
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class MeetInTheMiddle {

    static long countAtMost(long[] a, long limit) {
        int n = a.length;

        if (n > 40) {
            throw new IllegalArgumentException("n>40");
        }

        long[] l = sums(a, 0, n / 2);
        long[] r = sums(a, n / 2, n);

        Arrays.sort(r);

        long ans = 0;

        for (long x : l) {
            long target;

            try {
                target = Math.subtractExact(limit, x);
            } catch (ArithmeticException ex) {
                target = x < 0 ? Long.MAX_VALUE : Long.MIN_VALUE;
            }

            ans += upper(r, target);
        }

        return ans;
    }

    static long[] sums(long[] a, int from, int to) {
        int n = to - from;
        long[] res = new long[1 << n];

        for (int mask = 1; mask < res.length; mask++) {
            int b = Integer.numberOfTrailingZeros(mask);
            res[mask] = Math.addExact(res[mask & (mask - 1)], a[from + b]);
        }

        return res;
    }

    static int upper(long[] a, long x) {
        int l = 0, r = a.length;

        while (l < r) {
            int m = (l + r) >>> 1;

            if (a[m] <= x) {
                l = m + 1;
            } else {
                r = m;
            }
        }

        return l;
    }
}