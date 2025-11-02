package graph.metrics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleMetrics implements Metrics {
    private final ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<>();
    private long start = 0L;
    private long end = 0L;

    @Override
    public void start() { start = System.nanoTime(); }
    @Override
    public void stop() { end = System.nanoTime(); }
    @Override
    public void inc(String counter) { counters.computeIfAbsent(counter, k -> new AtomicLong()).incrementAndGet(); }
    @Override
    public long getCounter(String counter) { return counters.getOrDefault(counter, new AtomicLong(0)).get(); }
    @Override
    public long elapsedNanos() { return end - start; }
}
