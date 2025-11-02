package app;

import graph.GraphLoader;
import graph.metrics.SimpleMetrics;
import graph.scc.TarjanSCC;
import graph.cond.Condensation;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestLongest;
import graph.gen.DatasetGenerator;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "data/tasks.json";
        GraphLoader.GraphData d = GraphLoader.load(path);
        System.out.println("Loaded graph: n=" + d.n + ", edges=" + d.edges.size());

        // build unweighted adjacency for SCC and topo
        List<List<Integer>> gUn = GraphLoader.buildAdjListUnweighted(d);

        SimpleMetrics m = new SimpleMetrics();
        m.start();
        TarjanSCC tarjan = new TarjanSCC(d.n, gUn);
        List<List<Integer>> sccs = tarjan.run();
        m.stop();
        System.out.println("SCC count: " + sccs.size());
        for (int i = 0; i < sccs.size(); i++) System.out.println("SCC " + i + ": " + sccs.get(i));
        System.out.println("SCC time (ns): " + m.elapsedNanos());

        // condensation
        Condensation.Result c = Condensation.build(d.n, gUn, sccs);
        System.out.println("Condensed nodes: " + c.compCount);

        // convert compAdj (Set) to List<List<Integer>> for topo
        List<List<Integer>> compAdjList = new ArrayList<>();
        for (Set<Integer> s : c.compAdj) compAdjList.add(new ArrayList<>(s));

        List<Integer> topo = TopologicalSort.kahn(c.compCount, compAdjList);
        System.out.println("Topological order of components: " + topo);

        // Build weighted adjacency for DAG operations on original nodes (or components if desired)
        List<List<int[]>> gWeighted = GraphLoader.buildAdjList(d);

        // run shortest / longest on condensation DAG -- need to collapse weights between components: for simplicity, run on original graph but following topo of nodes
        // We'll compute topo order of original nodes by flattening SCCs in topological order
        List<Integer> nodeTopo = new ArrayList<>();
        for (int compId : topo) {
            List<Integer> comp = sccs.get(compId);
            // stable order inside component
            for (int v : comp) nodeTopo.add(v);
        }

        int source = d.source != null ? d.source : 0;
        int[] shortest = DAGShortestLongest.shortest(d.n, gWeighted, nodeTopo, source);
        System.out.println("Shortest distances from " + source + ":");
        for (int i = 0; i < shortest.length; i++) System.out.println(i + ": " + (shortest[i] >= Integer.MAX_VALUE/4 ? "INF" : shortest[i]));

        long[] longest = DAGShortestLongest.longest(d.n, gWeighted, nodeTopo, source);
        System.out.println("Longest distances from " + source + ":");
        for (int i = 0; i < longest.length; i++) System.out.println(i + ": " + (longest[i] == Long.MIN_VALUE/4 ? "-INF" : longest[i]));

        // dataset generation helper (optional)
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            System.out.println("Generating sample datasets under ./data ...");
            new DatasetGenerator().generateAll("data");
        }
    }
}
