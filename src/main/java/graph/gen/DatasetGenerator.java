package graph.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import graph.GraphLoader;

import java.io.File;
import java.util.*;

/**
 * Generates 9 datasets (small/medium/large, different densities, cyclic/DAG)
 */
public class DatasetGenerator {
    private final Random rnd = new Random(12345);
    private final ObjectMapper mapper = new ObjectMapper();

    public void generateAll(String dir) throws Exception {
        new File(dir).mkdirs();
        generateCategory(dir, "small", 6, 10, 3);
        generateCategory(dir, "medium", 10, 20, 3);
        generateCategory(dir, "large", 20, 50, 3);
    }

    private void generateCategory(String dir, String name, int minN, int maxN, int variants) throws Exception {
        for (int i = 0; i < variants; i++) {
            int n = rnd.nextInt(maxN - minN + 1) + minN;
            double density = (i+1) * 0.15; // varying density
            boolean makeCyclic = (i % 2 == 0);
            GraphLoader.GraphData d = genGraph(n, density, makeCyclic);
            String fname = String.format("%s/%s_%d.json", dir, name, i+1);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fname), d);
        }
    }

    private GraphLoader.GraphData genGraph(int n, double density, boolean cyclic) {
        GraphLoader.GraphData d = new GraphLoader.GraphData();
        d.directed = true; d.n = n; d.edges = new ArrayList<>(); d.weight_model = "edge"; d.source = 0;
        int maxEdges = (int)(n * (n-1) * density);
        for (int k = 0; k < maxEdges; k++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if (u == v) continue;
            int w = rnd.nextInt(10) + 1;
            GraphLoader.EdgeJson e = new GraphLoader.EdgeJson(); e.u = u; e.v = v; e.w = w;
            d.edges.add(e);
        }
        // ensure cyclic or acyclic
        if (!cyclic) {
            // force DAG: keep only edges u->v with u < v for some nodes
            List<GraphLoader.EdgeJson> out = new ArrayList<>();
            for (GraphLoader.EdgeJson e : d.edges) if (e.u < e.v) out.add(e);
            if (out.isEmpty() && d.n >= 2) {
                GraphLoader.EdgeJson e = new GraphLoader.EdgeJson(); e.u = 0; e.v = 1; e.w = 1; out.add(e);
            }
            d.edges = out;
        } else {
            // add a guaranteed cycle
            if (d.n >= 3) {
                GraphLoader.EdgeJson e1 = new GraphLoader.EdgeJson(); e1.u = 0; e1.v = 1; e1.w = 1;
                GraphLoader.EdgeJson e2 = new GraphLoader.EdgeJson(); e2.u = 1; e2.v = 2; e2.w = 1;
                GraphLoader.EdgeJson e3 = new GraphLoader.EdgeJson(); e3.u = 2; e3.v = 0; e3.w = 1;
                d.edges.add(e1); d.edges.add(e2); d.edges.add(e3);
            }
        }
        return d;
    }
}
