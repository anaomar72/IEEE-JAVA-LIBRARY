IEEE XTREME JAVA LIBRARY — QUICK REFERENCE LAYOUT
Java 17+ | No external dependencies | All files in one directory

LAYOUT (matches the uploaded reference):
  Main.java               Online judge submission template, fast input/output
  SortingSearching.java   Sorting, searching, arrays, strings, KMP, Z, rolling hash
  GraphAlgorithms.java    BFS, DFS, shortest paths, MST, SCC, LCA, max flow
  DynamicProgramming.java DP, bitmask, greedy, backtracking, meet-in-middle
  DSU.java                DSU, Fenwick, segment tree, trie, collections, compression
  MathAlgorithms.java     Number theory, mod arithmetic, combinatorics, geometry
  README.txt              This navigation guide

The six helper files retain the reference's original quick-start classes,
and include the larger library's 49 additional algorithm classes as
package-private top-level classes. Search a filename or class name inside
its corresponding Java file (e.g. KMP in SortingSearching.java).

HOW TO USE
  1. Use Main.java for every problem submission.
  2. Search for the relevant algorithm in the helper file above.
  3. If the judge only accepts Main.java, paste the desired class below
     public class Main (NOT inside a method), and add necessary imports.
  4. Keep only the code you need to avoid long submissions.

COMPILE ALL (from the ieeextreme_java_library folder):
  javac --release 17 *.java
RUN:
  java Main < input.txt
COMPILE MAIN ONLY:
  javac --release 17 Main.java

IMPORTANT NOTES
  - The original uploaded functions are retained, with added extended algorithms.
  - All files use the default package; do not add package declarations.
  - For copied nested helper classes, check whether other library classes
    are referenced and copy their dependencies too.
  - Graph vertices are usually 0-based. Dijkstra requires nonnegative weights.
  - Modular multiplication, LCM and combinatorial counts can overflow long.
  - Recursion depth, input size, and memory limits still matter.
  - This library is a reference, not a guarantee of accepted solutions.

QUICK SELECTION
  Sorted array/index -> SortingSearching.java (BinarySearchToolkit)
  Prefix/range sums -> SortingSearching.java (ArrayPatterns)
  Connectivity -> DSU.java (DSU/DisjointSetUnion)
  Range updates/queries -> DSU.java (FenwickTree/SegmentTree)
  Unweighted shortest path -> GraphAlgorithms.java (GraphAlgorithms.bfs)
  Nonnegative weighted path -> GraphAlgorithms.java (Dijkstra)
  Negative edges -> GraphAlgorithms.java (BellmanFord)
  MST -> GraphAlgorithms.java (KruskalMST/PrimMST)
  Knapsack or LCS -> DynamicProgramming.java (KnapsackDP/SequenceDP)
  Modulo computations -> MathAlgorithms.java (ModularArithmetic)
  Geometry -> MathAlgorithms.java (Geometry2D)
