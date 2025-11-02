package graph.dagsp;

import java.util.*;

/**
 * Edge-weighted DAG shortest and longest path algorithms using topological order.
 */
public class DAGShortestLongest {
    public static class Edge { public final int to; public final int w; public Edge(int t, int w){to=t;w=w; this.to=t; this.w=w;} public final int w2 = w; }

    /** shortest distances from source; graph as list of edges */
    public static int[] shortest(int n, List<List<int[]>> graph, List<Integer> topoOrder, int source) {
        final int INF = Integer.MAX_VALUE / 4;
        int[] dist = new int[n]; Arrays.fill(dist, INF); dist[source] = 0;
        // topoOrder is over original nodes or components depending on use
        for (int u : topoOrder) {
            if (dist[u] == INF) continue;
            for (int[] e : graph.get(u)) {
                int v = e[0], w = e[1];
                if (dist[v] > dist[u] + w) dist[v] = dist[u] + w;
            }
        }
        return dist;
    }

    /** longest path: we invert sign or simply do DP maximizing value (works only for DAG) */
    public static long[] longest(int n, List<List<int[]>> graph, List<Integer> topoOrder, int source) {
        final long NEG = Long.MIN_VALUE / 4;
        long[] dp = new long[n]; Arrays.fill(dp, NEG); dp[source] = 0;
        for (int u : topoOrder) {
            if (dp[u] == NEG) continue;
            for (int[] e : graph.get(u)) {
                int v = e[0], w = e[1];
                if (dp[v] < dp[u] + w) dp[v] = dp[u] + w;
            }
        }
        return dp;
    }

    /** Reconstruct one longest path to a target (needs predecessor tracking) */
    public static List<Integer> reconstructLongest(int target, int n, List<List<int[]>> graph, List<Integer> topoOrder, int source) {
        final long NEG = Long.MIN_VALUE / 4;
        long[] dp = new long[n]; Arrays.fill(dp, NEG); dp[source] = 0;
        int[] parent = new int[n]; Arrays.fill(parent, -1);
        for (int u : topoOrder) {
            if (dp[u] == NEG) continue;
            for (int[] e : graph.get(u)) {
                int v = e[0], w = e[1];
                if (dp[v] < dp[u] + w) { dp[v] = dp[u] + w; parent[v] = u; }
            }
        }
        if (dp[target] == NEG) return Collections.emptyList();
        LinkedList<Integer> path = new LinkedList<>();
        int cur = target;
        while (cur != -1) { path.addFirst(cur); cur = parent[cur]; }
        return path;
    }
}
