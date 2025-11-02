package graph.dagsp;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DAGSPTest {
    @Test
    public void testShortestInSimpleDAG() {
        int n = 4;
        List<List<int[]>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        g.get(0).add(new int[]{1, 2});
        g.get(0).add(new int[]{2, 5});
        g.get(1).add(new int[]{2, 1});
        g.get(1).add(new int[]{3, 3});
        g.get(2).add(new int[]{3, 1});
        List<Integer> topo = Arrays.asList(0,1,2,3);
        int[] dist = DAGShortestLongest.shortest(n, g, topo, 0);
        assertEquals(0, dist[0]);
        assertEquals(2, dist[1]);
        assertEquals(3, dist[2]);
        assertEquals(4, dist[3]);
    }
}
