import java.util.*;
import java.io.*;
import java.math.*;
import java.util.function.*;

/**
 * Graph helpers use vertices numbered 0..n-1.
 * For an undirected graph, add each edge in both directions.
 */
class GraphAlgorithms {

    // Unweighted shortest distances from start; -1 means unreachable.
    static int[] bfs(List<List<Integer>> graph, int start) {
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, -1);

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        dist[start] = 0;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v : graph.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    queue.offer(v);
                }
            }
        }

        return dist;
    }

    // Iterative DFS avoids deep recursive-call stack issues.
    static boolean[] dfs(List<List<Integer>> graph, int start) {
        int n = graph.size();
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int u = stack.pop();

            if (visited[u]) {
                continue;
            }

            visited[u] = true;

            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    stack.push(v);
                }
            }
        }

        return visited;
    }

    // Weighted adjacency list: graph.get(u) contains {v, weight}.
    // Dijkstra requires all edge weights to be nonnegative.
    // Unreachable vertices have distance INF.
    static long[] dijkstra(List<List<long[]>> graph, int start) {
        int n = graph.size();
        final long INF = Long.MAX_VALUE / 4;

        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[start] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>(
                (a, b) -> Long.compare(a[1], b[1])
        );

        pq.offer(new long[]{start, 0});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[0];
            long currentDist = cur[1];

            if (currentDist != dist[u]) {
                continue;
            }

            for (long[] edge : graph.get(u)) {
                int v = (int) edge[0];
                long weight = edge[1];

                if (weight < 0) {
                    throw new IllegalArgumentException(
                            "Dijkstra cannot use negative edge weights."
                    );
                }

                if (currentDist <= INF - weight) {
                    long candidate = currentDist + weight;

                    if (candidate < dist[v]) {
                        dist[v] = candidate;
                        pq.offer(new long[]{v, candidate});
                    }
                }
            }
        }

        return dist;
    }

    static List<List<Integer>> newGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        return graph;
    }

    static void addUndirectedEdge(
            List<List<Integer>> graph, int u, int v
    ) {
        graph.get(u).add(v);
        graph.get(v).add(u);
    }
}


// ==========================================
// ADDITIONAL ALGORITHMS FROM EXTENDED LIBRARY
// Each class is package-private for single-folder compilation.
// ==========================================


// SOURCE: 06-Graphs/BellmanFord.java

/**
 * Tier 2 | shortest paths with negative edges and negative cycle flag
 * Use: weighted directed graphs
 * Time: O(VE). Space: O(V+E).
 * Edge cases: unreachable negative cycles ignored
 * Pitfalls: Do not relax from INF
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class BellmanFord {

    static final long INF = Long.MAX_VALUE / 4;

    static final class Edge {
        final int u, v;
        final long w;

        Edge(int u, int v, long w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    static final class Result {
        final long[] distance;
        final boolean reachableNegativeCycle;

        Result(long[] d, boolean neg) {
            distance = d;
            reachableNegativeCycle = neg;
        }
    }

    static Result solve(int n, List<Edge> edges, int source) {
        long[] d = new long[n];
        Arrays.fill(d, INF);
        d[source] = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean any = false;

            for (Edge e : edges) {
                if (d[e.u] != INF) {
                    long nd = safeAdd(d[e.u], e.w);

                    if (nd < d[e.v]) {
                        d[e.v] = nd;
                        any = true;
                    }
                }
            }

            if (!any) {
                break;
            }
        }

        boolean neg = false;

        for (Edge e : edges) {
            if (d[e.u] != INF && safeAdd(d[e.u], e.w) < d[e.v]) {
                neg = true;
                break;
            }
        }

        return new Result(d, neg);
    }

    static long safeAdd(long a, long b) {
        if (b > 0 && a > Long.MAX_VALUE - b) {
            return Long.MAX_VALUE;
        }

        if (b < 0 && a < Long.MIN_VALUE - b) {
            return Long.MIN_VALUE;
        }

        return a + b;
    }
}


// SOURCE: 06-Graphs/Dijkstra.java

/**
 * Tier 1 | nonnegative shortest paths
 * Use: weighted sparse graphs
 * Time: O((V+E) log V). Space: O(V+E).
 * Edge cases: unreachable nodes are INF; weights nonnegative
 * Pitfalls: Negative edges invalidate Dijkstra
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class Dijkstra {

    static final long INF = Long.MAX_VALUE / 4;

    static final class Edge {
        final int to;
        final long w;

        Edge(int to, long w) {
            if (w < 0) {
                throw new IllegalArgumentException("negative edge");
            }

            this.to = to;
            this.w = w;
        }
    }

    static List<Edge>[] graph(int n) {
        @SuppressWarnings("unchecked")
        List<Edge>[] g = new List[n];

        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }

        return g;
    }

    static long[] shortest(List<Edge>[] g, int src) {
        long[] d = new long[g.length];
        Arrays.fill(d, INF);
        d[src] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>(
                Comparator.comparingLong(x -> x[0])
        );

        pq.add(new long[]{0, src});

        while (!pq.isEmpty()) {
            long[] cur = pq.remove();
            long dist = cur[0];
            int v = (int) cur[1];

            if (dist != d[v]) {
                continue;
            }

            for (Edge e : g[v]) {
                if (dist > INF - e.w) {
                    continue;
                }

                long nd = dist + e.w;

                if (nd < d[e.to]) {
                    d[e.to] = nd;
                    pq.add(new long[]{nd, e.to});
                }
            }
        }

        return d;
    }
}


// SOURCE: 06-Graphs/FloydWarshall.java

/**
 * Tier 2 | all-pairs shortest path
 * Use: small dense graphs
 * Time: O(V^3). Space: O(V^2).
 * Edge cases: negative cycles d[i][i]<0; INF unreachable
 * Pitfalls: Do not add INF values
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class FloydWarshall {

    static final long INF = Long.MAX_VALUE / 4;

    static long[][] solve(long[][] weights) {
        int n = weights.length;
        long[][] d = new long[n][n];

        for (int i = 0; i < n; i++) {
            if (weights[i].length != n) {
                throw new IllegalArgumentException();
            }

            d[i] = weights[i].clone();
            d[i][i] = Math.min(d[i][i], 0);
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (d[i][k] >= INF) {
                    continue;
                }

                for (int j = 0; j < n; j++) {
                    if (d[k][j] >= INF) {
                        continue;
                    }

                    long nd = BellmanFord.safeAdd(d[i][k], d[k][j]);

                    if (nd < d[i][j]) {
                        d[i][j] = nd;
                    }
                }
            }
        }

        return d;
    }

    static boolean negativeCycle(long[][] d) {
        for (int i = 0; i < d.length; i++) {
            if (d[i][i] < 0) {
                return true;
            }
        }

        return false;
    }
}


// SOURCE: 06-Graphs/GraphCyclesAndTopo.java

/**
 * Tier 2 | directed cycle detection, topo sorting, bipartite test
 * Use: DAG tasks and 2-coloring
 * Time: O(V+E). Space: O(V+E).
 * Edge cases: self loops, disconnected graphs
 * Pitfalls: Topological order exists only if directed graph acyclic
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class GraphCyclesAndTopo {

    static int[] topologicalOrder(List<Integer>[] g) {
        int n = g.length;
        int[] deg = new int[n], ans = new int[n];

        for (List<Integer> list : g) {
            for (int v : list) {
                deg[v]++;
            }
        }

        ArrayDeque<Integer> q = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (deg[i] == 0) {
                q.add(i);
            }
        }

        int k = 0;

        while (!q.isEmpty()) {
            int v = q.remove();
            ans[k++] = v;

            for (int u : g[v]) {
                if (--deg[u] == 0) {
                    q.add(u);
                }
            }
        }

        return k == n ? ans : new int[0];
    }

    static boolean directedHasCycle(List<Integer>[] g) {
        int n = g.length;
        int[] deg = new int[n];

        for (List<Integer> edges : g) {
            for (int v : edges) {
                deg[v]++;
            }
        }

        ArrayDeque<Integer> q = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (deg[i] == 0) {
                q.add(i);
            }
        }

        int seen = 0;

        while (!q.isEmpty()) {
            int v = q.remove();
            seen++;

            for (int u : g[v]) {
                if (--deg[u] == 0) {
                    q.add(u);
                }
            }
        }

        return seen != n;
    }

    static boolean bipartite(List<Integer>[] g) {
        int[] color = new int[g.length];
        Arrays.fill(color, -1);

        ArrayDeque<Integer> q = new ArrayDeque<>();

        for (int s = 0; s < g.length; s++) {
            if (color[s] >= 0) {
                continue;
            }

            color[s] = 0;
            q.add(s);

            while (!q.isEmpty()) {
                int v = q.remove();

                for (int u : g[v]) {
                    if (color[u] < 0) {
                        color[u] = color[v] ^ 1;
                        q.add(u);
                    } else if (color[u] == color[v]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    static boolean undirectedHasCycle(List<Integer>[] g) {
        DisjointSetUnion d = new DisjointSetUnion(g.length);

        for (int v = 0; v < g.length; v++) {
            for (int u : g[v]) {
                if (u > v || u == v) {
                    if (!d.union(v, u)) {
                        return true;
                    }
                }
            }
        }

        return false;
    } // assumes each undirected edge mirrored; parallel edges count
}


// SOURCE: 06-Graphs/GraphTraversal.java

/**
 * Tier 1 | adjacency list, BFS, DFS, components
 * Use: unweighted paths and reachability
 * Time: O(V+E). Space: O(V+E).
 * Edge cases: disconnected graph; zero vertices
 * Pitfalls: recursive DFS stack overflow
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class GraphTraversal {

    static List<Integer>[] graph(int n) {
        @SuppressWarnings("unchecked")
        List<Integer>[] g = new List[n];

        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }

        return g;
    }

    static void undirected(List<Integer>[] g, int u, int v) {
        g[u].add(v);
        g[v].add(u);
    }

    static int[] bfs(List<Integer>[] g, int source) {
        int[] d = new int[g.length];
        Arrays.fill(d, -1);

        ArrayDeque<Integer> q = new ArrayDeque<>();
        d[source] = 0;
        q.add(source);

        while (!q.isEmpty()) {
            int v = q.remove();

            for (int u : g[v]) {
                if (d[u] == -1) {
                    d[u] = d[v] + 1;
                    q.add(u);
                }
            }
        }

        return d;
    }

    static boolean[] dfs(List<Integer>[] g, int source) {
        boolean[] seen = new boolean[g.length];
        ArrayDeque<Integer> st = new ArrayDeque<>();
        st.push(source);

        while (!st.isEmpty()) {
            int v = st.pop();

            if (seen[v]) {
                continue;
            }

            seen[v] = true;

            for (int u : g[v]) {
                if (!seen[u]) {
                    st.push(u);
                }
            }
        }

        return seen;
    }

    static int[] componentIds(List<Integer>[] g) {
        int n = g.length;
        int[] id = new int[n];
        Arrays.fill(id, -1);

        int components = 0;
        ArrayDeque<Integer> q = new ArrayDeque<>();

        for (int s = 0; s < n; s++) {
            if (id[s] != -1) {
                continue;
            }

            id[s] = components;
            q.add(s);

            while (!q.isEmpty()) {
                int v = q.remove();

                for (int u : g[v]) {
                    if (id[u] < 0) {
                        id[u] = components;
                        q.add(u);
                    }
                }
            }

            components++;
        }

        return id;
    }
}


// SOURCE: 06-Graphs/KruskalMST.java

/**
 * Tier 2 | minimum spanning forest using DSU
 * Use: weighted undirected edges
 * Time: O(E log E). Space: O(V+E).
 * Edge cases: disconnected graph returns forest
 * Pitfalls: MST assumes undirected input; weight sum may overflow long
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class KruskalMST {

    static final class Edge {
        final int u, v;
        final long w;

        Edge(int u, int v, long w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    static final class Result {
        final long weight;
        final int edgesUsed;

        Result(long w, int e) {
            weight = w;
            edgesUsed = e;
        }
    }

    static Result solve(int n, List<Edge> edges) {
        List<Edge> sorted = new ArrayList<>(edges);
        sorted.sort(Comparator.comparingLong(e -> e.w));

        DisjointSetUnion d = new DisjointSetUnion(n);
        long total = 0;
        int count = 0;

        for (Edge e : sorted) {
            if (d.union(e.u, e.v)) {
                total = Math.addExact(total, e.w);
                count++;
            }
        }

        return new Result(total, count);
    }
}


// SOURCE: 06-Graphs/PrimMST.java

/**
 * Tier 2 | minimum spanning forest using priority queue
 * Use: dense-ish undirected graphs with adjacency lists
 * Time: O((V+E) log V). Space: O(V+E).
 * Edge cases: disconnected returns forest
 * Pitfalls: Each undirected edge must be inserted in both lists
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class PrimMST {

    static final class Edge {
        final int to;
        final long w;

        Edge(int to, long w) {
            this.to = to;
            this.w = w;
        }
    }

    static List<Edge>[] graph(int n) {
        @SuppressWarnings("unchecked")
        List<Edge>[] g = new List[n];

        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }

        return g;
    }

    static long forestWeight(List<Edge>[] g) {
        boolean[] used = new boolean[g.length];

        PriorityQueue<long[]> pq = new PriorityQueue<>(
                Comparator.comparingLong(a -> a[0])
        );

        long total = 0;

        for (int s = 0; s < g.length; s++) {
            if (used[s]) {
                continue;
            }

            pq.add(new long[]{0, s});

            while (!pq.isEmpty()) {
                long[] a = pq.poll();
                int v = (int) a[1];

                if (used[v]) {
                    continue;
                }

                used[v] = true;
                total = Math.addExact(total, a[0]);

                for (Edge e : g[v]) {
                    if (!used[e.to]) {
                        pq.add(new long[]{e.w, e.to});
                    }
                }
            }
        }

        return total;
    }
}


// SOURCE: 06-Graphs/StronglyConnectedComponents.java

/**
 * Tier 3 | iterative Kosaraju SCC
 * Use: directed graph cycles and condensation
 * Time: O(V+E). Space: O(V+E).
 * Edge cases: zero vertices; disconnected graph
 * Pitfalls: Reverse graph required; order is finishing order
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class StronglyConnectedComponents {

    static int[] ids(List<Integer>[] g) {
        int n = g.length;
        List<Integer>[] rev = GraphTraversal.graph(n);

        for (int v = 0; v < n; v++) {
            for (int u : g[v]) {
                rev[u].add(v);
            }
        }

        boolean[] seen = new boolean[n];
        List<Integer> order = new ArrayList<>();

        for (int s = 0; s < n; s++) {
            if (seen[s]) {
                continue;
            }

            ArrayDeque<int[]> st = new ArrayDeque<>();
            seen[s] = true;
            st.push(new int[]{s, 0});

            while (!st.isEmpty()) {
                int[] top = st.peek();
                int v = top[0];

                if (top[1] == g[v].size()) {
                    order.add(v);
                    st.pop();
                } else {
                    int u = g[v].get(top[1]++);

                    if (!seen[u]) {
                        seen[u] = true;
                        st.push(new int[]{u, 0});
                    }
                }
            }
        }

        int[] id = new int[n];
        Arrays.fill(id, -1);

        int c = 0;

        for (int i = n - 1; i >= 0; i--) {
            int s = order.get(i);

            if (id[s] != -1) {
                continue;
            }

            ArrayDeque<Integer> st = new ArrayDeque<>();
            st.push(s);
            id[s] = c;

            while (!st.isEmpty()) {
                int v = st.pop();

                for (int u : rev[v]) {
                    if (id[u] == -1) {
                        id[u] = c;
                        st.push(u);
                    }
                }
            }

            c++;
        }

        return id;
    }
}


// SOURCE: 09-Advanced-Algorithms/DinicMaxFlow.java

/**
 * Tier 3 | Dinic max flow on directed capacity networks
 * Use: flow / minimum cut
 * Time: O(V^2 E) general worst-case. Space: O(V+E).
 * Edge cases: zero capacities, parallel edges
 * Pitfalls: Residual reverse edges and long capacity overflow
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class DinicMaxFlow {

    static final class Edge {
        int to, rev;
        long capacity;

        Edge(int t, int r, long c) {
            to = t;
            rev = r;
            capacity = c;
        }
    }

    final List<Edge>[] g;
    final int[] level, ptr;

    DinicMaxFlow(int n) {
        @SuppressWarnings("unchecked")
        List<Edge>[] graph = new List[n];

        g = graph;

        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }

        level = new int[n];
        ptr = new int[n];
    }

    void addEdge(int u, int v, long cap) {
        if (cap < 0) {
            throw new IllegalArgumentException();
        }

        Edge f = new Edge(v, g[v].size() + (u == v ? 1 : 0), cap);
        Edge b = new Edge(u, g[u].size(), 0);

        g[u].add(f);
        g[v].add(b);
    }

    boolean bfs(int s, int t) {
        Arrays.fill(level, -1);

        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(s);
        level[s] = 0;

        while (!q.isEmpty()) {
            int v = q.remove();

            for (Edge e : g[v]) {
                if (e.capacity > 0 && level[e.to] < 0) {
                    level[e.to] = level[v] + 1;
                    q.add(e.to);
                }
            }
        }

        return level[t] >= 0;
    }

    long dfs(int v, int t, long pushed) {
        if (pushed == 0) {
            return 0;
        }

        if (v == t) {
            return pushed;
        }

        for (; ptr[v] < g[v].size(); ptr[v]++) {
            Edge e = g[v].get(ptr[v]);

            if (level[e.to] != level[v] + 1 || e.capacity == 0) {
                continue;
            }

            long f = dfs(e.to, t, Math.min(pushed, e.capacity));

            if (f == 0) {
                continue;
            }

            e.capacity -= f;
            g[e.to].get(e.rev).capacity += f;

            return f;
        }

        return 0;
    }

    long maxFlow(int s, int t) {
        if (s == t) {
            return 0;
        }

        long flow = 0;

        while (bfs(s, t)) {
            Arrays.fill(ptr, 0);

            long pushed;

            while ((pushed = dfs(s, t, Long.MAX_VALUE)) != 0) {
                flow = Math.addExact(flow, pushed);
            }
        }

        return flow;
    }
}


// SOURCE: 09-Advanced-Algorithms/LowestCommonAncestor.java

/**
 * Tier 3 | binary lifting and LCA for rooted forest
 * Use: tree ancestor/distance queries
 * Time: O((V+E)log V) build, O(log V) query. Space: O(V log V).
 * Edge cases: disconnected vertices return -1; 0 vertices
 * Pitfalls: Input must be undirected forest, no cycles
 * Example: See Examples/ and tests/ for runnable samples.
 * Java 17; copy this class inside Main (remove public modifier if present).
 */
final class LowestCommonAncestor {

    final int[][] up;
    final int[] depth, component;
    final int log;

    LowestCommonAncestor(List<Integer>[] forest) {
        int n = forest.length;
        int lg = 1;

        while ((1L << lg) <= Math.max(1, n)) {
            lg++;
        }

        log = lg;
        up = new int[log][n];
        depth = new int[n];
        component = new int[n];

        Arrays.fill(component, -1);

        for (int[] a : up) {
            Arrays.fill(a, -1);
        }

        ArrayDeque<Integer> q = new ArrayDeque<>();
        int id = 0;

        for (int s = 0; s < n; s++) {
            if (component[s] != -1) {
                continue;
            }

            component[s] = id;
            q.add(s);

            while (!q.isEmpty()) {
                int v = q.remove();

                for (int u : forest[v]) {
                    if (component[u] == -1) {
                        component[u] = id;
                        depth[u] = depth[v] + 1;
                        up[0][u] = v;
                        q.add(u);
                    }
                }
            }

            id++;
        }

        for (int k = 1; k < log; k++) {
            for (int v = 0; v < n; v++) {
                int p = up[k - 1][v];
                up[k][v] = p == -1 ? -1 : up[k - 1][p];
            }
        }
    }

    int ancestor(int v, int steps) {
        if (steps < 0) {
            throw new IllegalArgumentException();
        }

        for (int k = 0; k < log && v >= 0; k++) {
            if (((steps >>> k) & 1) != 0) {
                v = up[k][v];
            }
        }

        if ((steps >>> log) != 0) {
            return -1;
        }

        return v;
    }

    int lca(int a, int b) {
        if (component[a] != component[b]) {
            return -1;
        }

        if (depth[a] < depth[b]) {
            int t = a;
            a = b;
            b = t;
        }

        a = ancestor(a, depth[a] - depth[b]);

        if (a == b) {
            return a;
        }

        for (int k = log - 1; k >= 0; k--) {
            if (up[k][a] != up[k][b]) {
                a = up[k][a];
                b = up[k][b];
            }
        }

        return up[0][a];
    }

    int distance(int a, int b) {
        int l = lca(a, b);
        return l < 0 ? -1 : depth[a] + depth[b] - 2 * depth[l];
    }
}