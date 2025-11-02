package graph.cond;

import java.util.*;

/**
 * Builds condensation graph from SCC partition
 */
public class Condensation {
    /**
     * Returns pair: (componentCount, adjList of components)
     */
    public static class Result {
        public final int compCount;
        public final List<Set<Integer>> compAdj;
        public final int[] compId; // node -> component id
        public Result(int compCount, List<Set<Integer>> compAdj, int[] compId) {
            this.compCount = compCount; this.compAdj = compAdj; this.compId = compId;
        }
    }

    public static Result build(int n, List<List<Integer>> graph, List<List<Integer>> sccs) {
        int compCount = sccs.size();
        int[] compId = new int[n];
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) compId[v] = i;
        }
        List<Set<Integer>> compAdj = new ArrayList<>();
        for (int i = 0; i < compCount; i++) compAdj.add(new HashSet<>());
        for (int u = 0; u < n; u++) {
            int cu = compId[u];
            for (int v : graph.get(u)) {
                int cv = compId[v];
                if (cu != cv) compAdj.get(cu).add(cv);
            }
        }
        return new Result(compCount, compAdj, compId);
    }
}
