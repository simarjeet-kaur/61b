import java.util.Arrays;

/** A partition of a set of contiguous integers that allows (a) finding whether
 *  two integers are in the same partition set and (b) replacing two partitions
 *  with their union.  At any given time, for a structure partitioning
 *  the integers 1-N, each partition is represented by a unique member of that
 *  partition, called its representative.
 *  @author
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        u = new int[N+1];
        size = new int[N+1];
        for (int i = 1; i < N + 1; i++) {
            u[i] = i;
            size[i] = 1;
        }
        newArry = new int[Integer.SIZE - Integer.numberOfLeadingZeros(N)];
    }

    /** Return the representative of the partition currently containing V.
     *  Assumes V is contained in one of the partitions.  */
    public int find(int v) {
        int a = 0;
        while (u[v] != v) {
            newArry[a++] = v;
            v = u[v];
        }
        while (a > 0) {
            u[newArry[--a]] = v;
        }
        return v;  // FIXME
    }

    /** Return true iff U and V are in the same partition. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single partition, returning its representative. */
    public int union(int u1, int v) {
        u = find(u1);
        v = find(v);
        if (u1 == v) {
            return u1; //when they are the same return u1
        } else if (size[u1] < size[v]) {
            u[u1] = v;
            size[u] += size[v];
            return v;
            //when u1 is less than v, change the original u array to v at u1
        }
    }

    private int[] u;
    private int[] size;
    private int[] newArry;
}
