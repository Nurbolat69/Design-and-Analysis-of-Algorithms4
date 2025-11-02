package graph.scc;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {
    @Test
    public void testSimpleCycle() {
        int n = 3;
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        g.get(0).add(1);
        g.get(1).add(2);
        g.get(2).add(0);
        TarjanSCC t = new TarjanSCC(n, g);
        List<List<Integer>> scc = t.run();
        assertEquals(1, scc.size());
        assertEquals(3, scc.get(0).size());
    }
}
