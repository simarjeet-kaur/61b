import java.util.Arrays;
import java.util.Comparator;

/** Minimal spanning tree utility.
 *  @author
 */
public class MST {

    /** Given an undirected, weighted, connected graph whose vertices are
     *  numbered 1 to V, and an array E of edges, returns an array of edges
     *  in E that form a minimal spanning tree of the input graph.
     *  Each edge in E is a three-element int array of the form (u, v, w),
     *  where 0 < u < v <= V are vertex numbers, and 0 <= w is the weight
     *  of the edge. The result is an array containing edges from E.
     *  Neither E nor the arrays in it may be modified.  There may be
     *  multiple edges between vertices.  The objects in the returned array
     *  are a subset of those in E (they do not include copies of the
     *  original edges, just the original edges themselves.) */
    public static int[][] mst(int V, int[][] E) {
        int [][] copy;
        for (int i = 0; i < E.length; i ++) {
            for (int j = 0; j < E[i]; j++) {
                copy[i][j] = E[i][j];
            }
        }
        Arrays.sort(copy, EDGE_WEIGHT_COMPARATOR);
        //make a unionfind
        int a = 0;
        int b = 0;
        int[][] copy1 = new int[V-1][3];
        UnionFind unionFind = new UnionFind(V);
        while (a < V-1) {
            int[] copy2 = copy[b++];
            int k = copy2[0];
            int h = copy2[1];
            if (unionFind.samePartition(k, h) != true) {
                unionFind.union(k, h);
                copy1[a++] = copy2
            }
        }
        return copy1;  // FIXME
    }

    /** An ordering of edges by weight. */
    private static final Comparator<int[]> EDGE_WEIGHT_COMPARATOR =
        new Comparator<int[]>() {
            @Override
            public int compare(int[] e0, int[] e1) {
                return e0[2] - e1[2];
            }
        };

}
