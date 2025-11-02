package graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;

/**
 * Loads graph data from JSON file matching tasks.json structure uploaded in the assignment.
 */
public class GraphLoader {
    public static class EdgeJson { public int u; public int v; public int w; }
    public static class GraphData {
        public boolean directed;
        public int n;
        public List<EdgeJson> edges;
        public Integer source;
        public String weight_model; // "edge" or "node"
    }

    public static GraphData load(String path) throws Exception {
        ObjectMapper m = new ObjectMapper();
        return m.readValue(new File(path), GraphData.class);
    }

    /** Build adjacency list (edge-weighted) */
    public static List<List<int[]>> buildAdjList(GraphData d) {
        List<List<int[]>> g = new ArrayList<>();
        for (int i = 0; i < d.n; i++) g.add(new ArrayList<>());
        for (EdgeJson e : d.edges) {
            g.get(e.u).add(new int[]{e.v, e.w});
            if (!d.directed) g.get(e.v).add(new int[]{e.u, e.w});
        }
        return g;
    }

    /** Build simple adjacency list (unweighted) for SCC / topo */
    public static List<List<Integer>> buildAdjListUnweighted(GraphData d) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < d.n; i++) g.add(new ArrayList<>());
        for (EdgeJson e : d.edges) {
            g.get(e.u).add(e.v);
            if (!d.directed) g.get(e.v).add(e.u);
        }
        return g;
    }
}
