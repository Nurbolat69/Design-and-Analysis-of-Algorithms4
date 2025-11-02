package graph.scc;

import java.util.*;

/**
 * Tarjan's algorithm for SCC
 */
public class TarjanSCC {
    private final int n;
    private final List<List<Integer>> g;
    private int time = 0;
    private final int[] disc, low;
    private final boolean[] inStack;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final List<List<Integer>> sccs = new ArrayList<>();

    public TarjanSCC(int n, List<List<Integer>> graph) {
        this.n = n;
        this.g = graph;
        disc = new int[n];
        low = new int[n];
        inStack = new boolean[n];
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
    }

    public List<List<Integer>> run() {
        for (int i = 0; i < n; i++) if (disc[i] == -1) dfs(i);
        return sccs;
    }

    private void dfs(int u) {
        disc[u] = low[u] = ++time;
        stack.push(u);
        inStack[u] = true;
        for (int v : g.get(u)) {
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                inStack[w] = false;
                comp.add(w);
            } while (w != u);
            sccs.add(comp);
        }
    }
}
